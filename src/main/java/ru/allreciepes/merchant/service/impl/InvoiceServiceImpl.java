package ru.allreciepes.merchant.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.allreciepes.merchant.domain.Invoice;
import ru.allreciepes.merchant.repository.InvoiceRepository;
import ru.allreciepes.merchant.service.InvoiceService;

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

}
