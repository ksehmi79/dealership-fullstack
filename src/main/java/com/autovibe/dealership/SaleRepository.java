package com.autovibe.dealership;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

  List<Sale> findByCustomerId(Long customerId);

  boolean existsByCarId(Long carId);

  boolean existsByCustomerId(Long customerId);
}