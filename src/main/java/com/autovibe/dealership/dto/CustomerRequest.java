package com.autovibe.dealership.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CustomerRequest {

  @NotBlank(message = "Customer Name is required")
  private String name;

  @NotBlank(message = "EMail ID is required")
  @Email (message = "Invalid Email ID")
  private String email;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
