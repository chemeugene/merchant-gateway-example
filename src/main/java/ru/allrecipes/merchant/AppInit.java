package ru.allrecipes.merchant;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.allrecipes.merchant.domain.Customer;
import ru.allrecipes.merchant.domain.Invoice;
import ru.allrecipes.merchant.domain.Supplier;
import ru.allrecipes.merchant.repository.CustomerRepository;
import ru.allrecipes.merchant.repository.InvoiceRepository;
import ru.allrecipes.merchant.repository.SupplierRepository;

@Component
@Slf4j
public class AppInit implements ApplicationRunner {

  private SupplierRepository supplierRepository;

  private InvoiceRepository invoiceRepository;

  private CustomerRepository customerRepository;

  public AppInit(SupplierRepository supplierRepository, InvoiceRepository invoiceRepository,
      CustomerRepository customerRepository) {
    this.supplierRepository = supplierRepository;
    this.invoiceRepository = invoiceRepository;
    this.customerRepository = customerRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Supplier suplier1 = new Supplier();
    suplier1.setName("Supplier 1");

    Supplier suplier2 = new Supplier();
    suplier2.setName("Supplier 2");

    supplierRepository.save(suplier1);
    supplierRepository.save(suplier2);

    Customer customer = new Customer();
    customer.setUsername("customer");
    customer.setEmail("test@gmail.com");

    customerRepository.save(customer);
    Invoice invoice1 = new Invoice();
    
    invoice1.setAmount(100d);
    invoice1.setSupplier(suplier1);
    invoice1.setCustomer(customer);

    Invoice invoice2 = new Invoice();
    invoice2.setAmount(150d);
    invoice2.setSupplier(suplier2);
    invoice2.setCustomer(customer);

    invoiceRepository.save(invoice1);
    invoiceRepository.save(invoice2);
    
    log.info("Invoice 1 id {}", invoice1.getInvoiceId());
    log.info("Invoice 2 id {}", invoice2.getInvoiceId());
  }

}
