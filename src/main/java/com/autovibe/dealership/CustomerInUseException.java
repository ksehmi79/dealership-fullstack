package com.autovibe.dealership;

public class CustomerInUseException extends RuntimeException {
  public CustomerInUseException(String message) {
    super(message);
  }
    
}
