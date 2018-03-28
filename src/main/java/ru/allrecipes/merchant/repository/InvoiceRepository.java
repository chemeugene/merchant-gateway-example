package ru.allrecipes.merchant.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.allrecipes.merchant.domain.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
  
  List<Invoice> findByCustomerUsername(String username);
  
  List<Invoice> findByInvoiceIdIn(Set<Integer> invoiceId);
}
