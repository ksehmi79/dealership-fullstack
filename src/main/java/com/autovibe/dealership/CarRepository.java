package com.autovibe.dealership;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByPriceGreaterThan(BigDecimal price);
    List<Car> findByPriceLessThan(BigDecimal price);
    List<Car> findByMakeIgnoreCaseAndYear(String make, int year);

    @Query("""
    SELECT c
    FROM Car c
    WHERE LOWER(c.make) = LOWER(:make)
    AND c.year >= :year
    AND c.price BETWEEN :minPrice AND :maxPrice
""")
List<Car> searchCars(
        @Param("make") String make,
        @Param("year") int year,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice
);
}   