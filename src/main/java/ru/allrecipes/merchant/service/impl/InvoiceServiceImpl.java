package ru.allrecipes.merchant.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.InvoicePaymentRequest;
import ru.allrecipes.merchant.repository.InvoiceRepository;
import ru.allrecipes.merchant.service.InvoiceService;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

  private InvoiceRepository invoiceRepository;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Invoice> getInvoicesByCustomerUsername(String username) {
    return invoiceRepository.findByCustomerUsername(username);
  }

  @Override
  public void payInvoice(InvoicePaymentRequest request) {
    
  }

}
