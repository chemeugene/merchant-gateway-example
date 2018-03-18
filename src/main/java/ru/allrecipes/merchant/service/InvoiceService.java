package ru.allrecipes.merchant.service;

import java.util.List;

import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;

public interface InvoiceService {

  List<Invoice> getInvoicesByCustomerUsername(String username);
  
  void payInvoice(InvoicePaymentRequest request);
}
