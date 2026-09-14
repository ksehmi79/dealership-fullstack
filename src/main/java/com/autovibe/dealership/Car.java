package com.autovibe.dealership;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Car {
  @Id @GeneratedValue private Long id;

  private String make;
  private String model;
  private int year;
  
  @Column(precision = 12, scale = 2)
  private BigDecimal price;

  public Car() {}

  public Car(String make, String model, int year, BigDecimal price) {

    this.make = make;
    this.model = model;
    this.year = year;
    this.price = price;
  }

  public Car(Long id, String make, String model, int year, BigDecimal price) {
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
