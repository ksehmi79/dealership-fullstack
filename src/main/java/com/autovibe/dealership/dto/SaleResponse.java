package com.autovibe.dealership.dto;

import java.math.BigDecimal;

public class SaleResponse {

  private Long id;
  private BigDecimal  salePrice;
  private Long customerId;
  private String customerName;

  private Long carId;
  private String carMake;
  private String carModel;

  public SaleResponse(
      Long id,
      BigDecimal  salePrice,
      Long customerId,
      String customerName,
      Long carId,
      String carMake,
      String carModel) {

    this.id = id;
    this.salePrice = salePrice;
    this.customerId = customerId;
    this.customerName = customerName;
    this.carId = carId;
    this.carMake = carMake;
    this.carModel = carModel;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal  getSalePrice() {
    return salePrice;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public Long getCarId() {
    return carId;
  }

  public String getCarMake() {
    return carMake;
  }

  public String getCarModel() {
    return carModel;
  }
}
