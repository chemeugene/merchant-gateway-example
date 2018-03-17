package ru.allrecipes.merchant.service;

import java.util.List;

import ru.allrecipes.merchant.domain.Invoice;

public interface InvoiceService {

  List<Invoice> getInvoicesByCustomerUsername(String username);
}
