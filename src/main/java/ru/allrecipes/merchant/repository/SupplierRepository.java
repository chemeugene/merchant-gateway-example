package ru.allrecipes.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.allrecipes.merchant.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
