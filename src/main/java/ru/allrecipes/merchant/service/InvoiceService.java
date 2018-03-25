package ru.allrecipes.merchant.service;

import java.util.List;

import ru.allrecipes.merchant.domain.Card;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.domain.InvoicePaymentResponse;
import ru.paymentgate.engine.webservices.merchant.PaymentOrderResult;
import ru.paymentgate.engine.webservices.merchant.RegisterOrderResponse;

public interface InvoiceService {

  List<Invoice> getInvoicesByCustomerUsername(String username);
  
  InvoicePaymentResponse payInvoice(InvoicePaymentRequest request);
  
  List<PaymentOrderResult> performPayment(Card card, List<RegisterOrderResponse> orders);
}
