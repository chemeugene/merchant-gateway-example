package ru.allrecipes.merchant.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.Invoice.State;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse.BulkStatus;
import ru.allrecipes.merchant.domain.InvoicePaymentStatus;
import ru.allrecipes.merchant.domain.InvoicePaymentStatus.OperationResult;
import ru.allrecipes.merchant.repository.InvoiceRepository;
import ru.allrecipes.merchant.service.InvoiceService;
import ru.allrecipes.merchant.system.HeaderHandlerResolver;
import ru.allrecipes.merchant.system.InvoiceSession;
import ru.bpc.phoenix.web.api.merchant.soap.MerchantServiceImplService;
import ru.paymentgate.engine.webservices.merchant.OrderParams;
import ru.paymentgate.engine.webservices.merchant.PaymentOrderParams;
import ru.paymentgate.engine.webservices.merchant.PaymentOrderResult;
import ru.paymentgate.engine.webservices.merchant.RegisterOrderResponse;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

  private InvoiceRepository invoiceRepository;

  private MerchantServiceImplService merchantService;

  private InvoiceSession invoiceSession;

  /**
   * Creates {@link InvoiceServiceImpl} instance.
   * 
   * @param invoiceRepository
   *          - invoiceRepository
   * @param merchantService
   *          - merchantService
   * @param headerHandlerResolver
   *          - headerHandlerResolver
   * @param invoiceSession
   *          - invoiceSession
   */
  public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
      MerchantServiceImplService merchantService, HeaderHandlerResolver headerHandlerResolver,
      InvoiceSession invoiceSession) {
    this.invoiceRepository = invoiceRepository;
    this.merchantService = merchantService;
    this.merchantService.setHandlerResolver(headerHandlerResolver);
    this.invoiceSession = invoiceSession;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Invoice> getInvoicesByCustomerUsername(String username) {
    return invoiceRepository.findByCustomerUsername(username);
  }

  @Override
  public InvoicePaymentResponse payInvoice(InvoicePaymentRequest request) {

    List<Invoice> invoices = invoiceRepository.findAllById(request.getInvoiceIds());
    List<InvoicePaymentStatus> errors = invoices.stream()
        .filter(invoice -> State.PAID == invoice.getState())
        .map(invoice -> InvoicePaymentStatus.builder().invoiceId(invoice.getId())
            .operationResult(OperationResult.ALREADY_PAID).build())
        .collect(Collectors.toList());
    errors.addAll(invoices.stream().filter(invoice -> State.LOCKED == invoice.getState())
        .map(invoice -> InvoicePaymentStatus.builder().invoiceId(invoice.getId())
            .operationResult(OperationResult.LOCKED).build())
        .collect(Collectors.toList()));

    if (!errors.isEmpty()) {
      return InvoicePaymentResponse.builder().bulkStatus(BulkStatus.REJECTED).statuses(errors)
          .build();
    } else {
      invoices.forEach(invoice -> invoice.setState(State.LOCKED));
    }
    List<RegisterOrderResponse> orders = new ArrayList<>();
    for (Invoice invoice : invoices) {
      try {
        OrderParams params = new OrderParams();
        params.setMerchantOrderNumber(invoice.getId().toString());
        params.setAmount(invoice.getAmount().longValue());
        params.setReturnUrl(
            ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        RegisterOrderResponse response = merchantService.getMerchantServiceImplPort()
            .registerOrder(params);
        if (response.getErrorCode() != 0) {
          errors.add(InvoicePaymentStatus.builder().invoiceId(invoice.getId())
              .operationResult(OperationResult.MERCHANT_ERROR).build());
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } else {
          orders.add(response);
        }
      } catch (Exception e) {
        String errorDescription = "Unable to register invoice in payment gateway";
        log.error(errorDescription, e);
        errors.add(InvoicePaymentStatus.builder().invoiceId(invoice.getId())
            .operationResult(OperationResult.MERCHANT_ERROR).errorDescription(errorDescription)
            .build());
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      }
    }
    if (!errors.isEmpty()) {
      return InvoicePaymentResponse.builder().bulkStatus(BulkStatus.REJECTED).statuses(errors)
          .build();
    } else {
      invoiceSession.addOrders(request.hashCode(), orders);
    }
    return null;
  }

  @Override
  public List<PaymentOrderResult> performPayment(Card card, List<RegisterOrderResponse> orders) {
    List<PaymentOrderResult> errors = new ArrayList<>();
    for (RegisterOrderResponse order : orders) {
      PaymentOrderParams params = new PaymentOrderParams();
      params.setCardholderName(card.getCardHolder());
      params.setPan(card.getCardNumber());
      params.setYear(card.getValidThrough());
      params.setCvc(card.getCvc());
      params.setOrderId(order.getOrderId());
      PaymentOrderResult result = merchantService.getMerchantServiceImplPort().paymentOrder(params);
      if (result.getErrorCode() != 0) {
        errors.add(result);
      }
    }
    if (errors.isEmpty()) {
      return errors;
    }
    return null;
  }

}
