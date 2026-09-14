package com.autovibe.dealership.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaleRequest {

  @NotNull(message = "Customer ID is required")
  private Long customerId;
  
  @NotNull(message = "Sale price is required")
  @Positive(message = "Sale price must be greater than 0")
  private BigDecimal  salePrice;

  @NotNull(message = "Car ID is required")
  private Long carId;

  public Long getCarId() {
    return carId;
  }

  public void setCarId(Long carId) {
    this.carId = carId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public BigDecimal  getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(BigDecimal  salePrice) {
    this.salePrice = salePrice;
  }
}
