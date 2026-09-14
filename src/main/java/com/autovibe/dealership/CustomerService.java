package com.autovibe.dealership;

import com.autovibe.dealership.dto.CustomerRequest;
import com.autovibe.dealership.dto.CustomerResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  private final CustomerRepository customerRepository;
private final SaleRepository saleRepository;

  public CustomerService(CustomerRepository customerRepository, SaleRepository saleRepository) {
    this.customerRepository = customerRepository;
    this.saleRepository = saleRepository;
  }

    public CustomerResponse getCustomerById(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    return toResponse(customer);
  }

  public CustomerResponse createCustomer(CustomerRequest request) {

  if (customerRepository.existsByEmailIgnoreCase(request.getEmail())) {
    throw new CustomerEmailAlreadyExistsException(
        "Customer with email " + request.getEmail() + " already exists."
    );
  }

  Customer savedCustomer =
      customerRepository.save(
          new Customer(
              request.getName(),
              request.getEmail()
          )
      );

  return toResponse(savedCustomer);
}

  public List<CustomerResponse> getAllCustomers() {
    return customerRepository
        .findAll()
        .stream()
        .map(this::toResponse)
        .toList();
  }

  public void deleteCustomer(Long id) {
    Customer existingCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

            if (saleRepository.existsByCustomerId(id)) {
                throw new CustomerInUseException("Customer with id " + id + " is currently in use and cannot be deleted.");
            }
    customerRepository.delete(existingCustomer);
  }

  public CustomerResponse updateCustomer(Long id, CustomerRequest request) {

    Customer existingCustomer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

    existingCustomer.setName(request.getName());
    existingCustomer.setEmail(request.getEmail());

    Customer savedCustomer = customerRepository.save(existingCustomer);
    return toResponse(savedCustomer);
  }

  private CustomerResponse toResponse(Customer customer) {
    return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail());
  }
}
