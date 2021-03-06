package ru.allrecipes.merchant.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
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
@Slf4j
@Transactional
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

    List<Invoice> invoices = invoiceRepository.findByInvoiceIdIn(request.getInvoiceIds());
    List<InvoicePaymentStatus> errors = new ArrayList<>();
    List<RegisterOrderResponse> orders = new ArrayList<>();
    Double totalAmount = 0d;
    for (Invoice invoice : invoices) {
      try {
        OrderParams params = new OrderParams();
        params.setMerchantOrderNumber(invoice.getInvoiceId().toString());
        params.setAmount(invoice.getAmount().longValue());
        params.setReturnUrl(
            ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        RegisterOrderResponse response = merchantService.getMerchantServiceImplPort()
            .registerOrder(params);
        if (response.getErrorCode() != 0) {
          errors.add(InvoicePaymentStatus.builder().invoiceId(invoice.getId())
              .operationResult(OperationResult.MERCHANT_ERROR).build());
        } else {
          orders.add(response);
          totalAmount += invoice.getAmount();
        }
      } catch (Exception e) {
        String errorDescription = "Unable to register invoice in payment gateway";
        log.error(errorDescription, e);
        errors.add(InvoicePaymentStatus.builder().invoiceId(invoice.getId())
            .operationResult(OperationResult.MERCHANT_ERROR).errorDescription(errorDescription)
            .build());
      }
    }
    if (!errors.isEmpty()) {
      return InvoicePaymentResponse.builder().bulkStatus(BulkStatus.REJECTED).statuses(errors)
          .build();
    }
    invoiceSession.addOrders(request.hashCode(), orders);

    return InvoicePaymentResponse.builder().totalAmount(totalAmount).bulkStatus(BulkStatus.OK)
        .build();
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
    if (!errors.isEmpty()) {
      return errors;
    }
    return null;
  }

}
