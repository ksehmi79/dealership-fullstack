package com.autovibe.dealership;

import com.autovibe.dealership.dto.CustomerRequest;
import com.autovibe.dealership.dto.CustomerResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@Tag(
    name = "Customers",
    description = "APIs for managing dealership customers"
)
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customer) {

    CustomerResponse savedCustomer = customerService.createCustomer(customer);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> getAllCustomers() {

    List<CustomerResponse> customers = customerService.getAllCustomers();

    return ResponseEntity.ok(customers);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

    customerService.deleteCustomer(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponse> updateCustomer(
      @PathVariable Long id, @Valid @RequestBody CustomerRequest request) {

    CustomerResponse response = customerService.updateCustomer(id, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {

    CustomerResponse response =
        customerService.getCustomerById(id);

    return ResponseEntity.ok(response);
}
}
