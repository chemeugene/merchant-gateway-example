package ru.allreciepes.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.allreciepes.merchant.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}
