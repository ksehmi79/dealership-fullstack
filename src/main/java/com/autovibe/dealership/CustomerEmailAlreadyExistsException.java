package com.autovibe.dealership;

public class CustomerEmailAlreadyExistsException extends RuntimeException {

  public CustomerEmailAlreadyExistsException(String message) {
    super(message);
  }
}