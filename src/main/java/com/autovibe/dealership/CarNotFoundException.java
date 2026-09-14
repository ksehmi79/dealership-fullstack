package com.autovibe.dealership;

public class CarNotFoundException extends RuntimeException {
  public CarNotFoundException(String message) {
    super(message);
  }
}
