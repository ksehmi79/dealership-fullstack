  package com.autovibe.dealership.dto;

  import java.math.BigDecimal;

  import jakarta.validation.constraints.Min;
  import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

  public class CarRequest {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1900, message = "Year must be 1900 or newer")
    private int year;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    public String getMake() {
      return make;
    }

    public void setMake(String make) {
      this.make = make;
    }

    public String getModel() {
      return model;
    }

    public void setModel(String model) {
      this.model = model;
    }

    public int getYear() {
      return year;
    }

    public void setYear(int year) {
      this.year = year;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public void setPrice(BigDecimal price) {
      this.price = price;
    }
  }
