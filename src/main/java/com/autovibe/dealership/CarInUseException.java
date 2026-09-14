package com.autovibe.dealership;

public class CarInUseException extends RuntimeException {

  public CarInUseException(String message) {
    super(message);
  }
}