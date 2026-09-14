package com.autovibe.dealership;

import com.autovibe.dealership.dto.SaleRequest;
import com.autovibe.dealership.dto.SaleResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
@Tag(
    name = "Sales",
    description = "APIs for managing vehicle sales"
)
public class SaleController {

  private final SaleService saleService;

  public SaleController(SaleService saleService) {
    this.saleService = saleService;
  }

  @PostMapping
  public ResponseEntity<SaleResponse> createSale(@Valid @RequestBody SaleRequest request) {

    SaleResponse saleResponse =
        saleService.createSale(request.getCustomerId(), request.getCarId(), request.getSalePrice());

    return ResponseEntity.status(HttpStatus.CREATED).body(saleResponse);
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<SaleResponse>> getSalesByCustomer(@PathVariable Long customerId) {

    List<SaleResponse> sales = saleService.getSalesByCustomer(customerId);

    return ResponseEntity.ok(sales);
  }

  @GetMapping
  public ResponseEntity<List<SaleResponse>> getAllSales() {

    List<SaleResponse> sales = saleService.getAllSales();

    return ResponseEntity.ok(sales);
  }
}
