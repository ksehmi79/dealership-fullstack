package com.autovibe.dealership;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CarNotFoundException.class)
  public ResponseEntity<String> handleCarNotFound(CarNotFoundException e) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException e) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler (CarAlreadySoldException.class)
  public ResponseEntity<String> handleCarAlreadySold(CarAlreadySoldException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler (UsernameAlreadyExistsException.class)
  public ResponseEntity<String> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {   
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  } 

  @ExceptionHandler (CarInUseException.class)
  public ResponseEntity<String> handleCarInUse(CarInUseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

    @ExceptionHandler (CustomerInUseException.class)
  public ResponseEntity<String> handleCustomerInUse(CustomerInUseException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  @ExceptionHandler(CustomerEmailAlreadyExistsException.class)
  public ResponseEntity<String> handleCustomerEmailAlreadyExists(
      CustomerEmailAlreadyExistsException e
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}
