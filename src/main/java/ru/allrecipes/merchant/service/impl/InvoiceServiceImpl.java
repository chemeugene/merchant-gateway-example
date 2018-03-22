package ru.allrecipes.merchant.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.repository.InvoiceRepository;
import ru.allrecipes.merchant.service.InvoiceService;
import ru.bpc.phoenix.web.api.merchant.soap.MerchantServiceImplService;
import ru.paymentgate.engine.webservices.merchant.OrderParams;
import ru.paymentgate.engine.webservices.merchant.RegisterOrderResponse;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

  private InvoiceRepository invoiceRepository;

  private MerchantServiceImplService merchantService;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
      MerchantServiceImplService merchantService) {
    this.invoiceRepository = invoiceRepository;
    this.merchantService = merchantService;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Invoice> getInvoicesByCustomerUsername(String username) {
    return invoiceRepository.findByCustomerUsername(username);
  }

  @Override
  public void payInvoice(InvoicePaymentRequest request) {
    OrderParams params = new OrderParams();
    params.setMerchantOrderNumber("1");
    params.setAmount(10L);
    params.setReturnUrl("http://all-recipes.ru/success");
    RegisterOrderResponse response = merchantService.getMerchantServiceImplPort()
        .registerOrder(params);
    System.out.println(response);
  }

}
