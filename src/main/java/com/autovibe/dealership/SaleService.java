package com.autovibe.dealership;

import com.autovibe.dealership.dto.SaleResponse;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {
  private final SaleRepository saleRepository;
  private final CustomerRepository customerRepository;
  private final CarRepository carRepository;

  public SaleService(
      SaleRepository saleRepository,
      CustomerRepository customerRepository,
      CarRepository carRepository) {

    this.saleRepository = saleRepository;
    this.customerRepository = customerRepository;
    this.carRepository = carRepository;
  }

  @Transactional
  public SaleResponse createSale(Long customerId, Long carId, BigDecimal  salePrice) {

    Customer customer =
        customerRepository
            .findById(customerId)
            .orElseThrow(
                () -> new CustomerNotFoundException("Customer not found with id: " + customerId));

    Car car =
        carRepository
            .findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + carId));

    if (saleRepository.existsByCarId(carId)) {
      throw new CarAlreadySoldException("Car with id " + carId + " has already been sold.");
    }
    Sale sale = new Sale(salePrice, customer, car);

    Sale newSale = saleRepository.save(sale);

    return toResponse(newSale);
  }

  private SaleResponse toResponse(Sale sale) {
    return new SaleResponse(
        sale.getId(),
        sale.getSalePrice(),
        sale.getCustomer().getId(),
        sale.getCustomer().getName(),
        sale.getCar().getId(),
        sale.getCar().getMake(),
        sale.getCar().getModel());
  }

  public List<SaleResponse> getSalesByCustomer(Long customerId) {

    customerRepository
        .findById(customerId)
        .orElseThrow(
            () -> new CustomerNotFoundException(
              "Customer not found with id: " + customerId
            )
          );

    return saleRepository
        .findByCustomerId(customerId)
        .stream()
        .map(this::toResponse)
        .toList();
  }

  public List<SaleResponse> getAllSales() {
    return saleRepository.findAll().stream().map(this::toResponse).toList();
  }
}
