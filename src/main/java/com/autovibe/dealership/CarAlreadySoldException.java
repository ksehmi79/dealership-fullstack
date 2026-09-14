package com.autovibe.dealership;

public class CarAlreadySoldException extends RuntimeException {
  public CarAlreadySoldException(String message) {
    super(message);
  } 
    
}
