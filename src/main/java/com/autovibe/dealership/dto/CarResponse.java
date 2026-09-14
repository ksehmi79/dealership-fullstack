package com.autovibe.dealership.dto;

import java.math.BigDecimal;

public class CarResponse {
  private Long id;
  private String make;
  private String model;
  private int year;
  private BigDecimal price;

  public CarResponse(Long id, String make, String model, int year, BigDecimal price) {
    this.id = id;
    this.make = make;
    this.model = model;
    this.year = year;
    this.price = price;
  }

  public Long getId() {
    return id;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public int getYear() {
    return year;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
