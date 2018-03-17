package ru.allreciepes.merchant.service;

import java.util.List;

import ru.allreciepes.merchant.domain.Invoice;

public interface InvoiceService {

  List<Invoice> getInvoicesByCustomerUsername(String username);
}
