package ru.allrecipes.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.allrecipes.merchant.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  
}
