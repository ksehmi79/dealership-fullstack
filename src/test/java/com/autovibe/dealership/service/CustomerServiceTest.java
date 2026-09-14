  package com.autovibe.dealership.service;

  import static org.junit.jupiter.api.Assertions.assertEquals;
  import static org.junit.jupiter.api.Assertions.assertThrows;
  import static org.mockito.ArgumentMatchers.any;
  import static org.mockito.Mockito.never;
  import static org.mockito.Mockito.verify;
  import static org.mockito.Mockito.when;

  import java.util.List;
  import java.util.Optional;

  import org.junit.jupiter.api.BeforeEach;
  import org.junit.jupiter.api.Test;
  import org.mockito.InjectMocks;
  import org.mockito.Mock;
  import org.mockito.MockitoAnnotations;

  import com.autovibe.dealership.Car;
  import com.autovibe.dealership.CarNotFoundException;
  import com.autovibe.dealership.Customer;
import com.autovibe.dealership.CustomerEmailAlreadyExistsException;
import com.autovibe.dealership.CustomerInUseException;
  import com.autovibe.dealership.CustomerNotFoundException;
  import com.autovibe.dealership.CustomerRepository;
  import com.autovibe.dealership.CustomerService;
  import com.autovibe.dealership.SaleRepository;
  import com.autovibe.dealership.dto.CustomerRequest;
  import com.autovibe.dealership.dto.CustomerResponse;

  class CustomerServiceTest {

      @Mock
      private CustomerRepository customerRepository;
      @Mock
      private SaleRepository saleRepository;

      @InjectMocks
      private CustomerService customerService;

      @BeforeEach
      void setUp() {
          MockitoAnnotations.openMocks(this);
      }

      @Test
      void shouldReturnAllCustomers() {

          // ARRANGE
          Customer customer1 =
              new Customer("John", "john@test.com");

          Customer customer2 =
              new Customer("Mike", "mike@test.com");

          
      when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

      // Act
      List<CustomerResponse> responses = customerService.getAllCustomers();

      // Assert
    
      assertEquals(2, responses.size());

      assertEquals("John", responses.get(0).getName());
      assertEquals("john@test.com", responses.get(0).getEmail());
      
      assertEquals("Mike", responses.get(1).getName());
      assertEquals("mike@test.com", responses.get(1).getEmail());

      // VERIFY
      verify(customerRepository).findAll();

      }


  @Test
  void shouldCreateCustomer() {

      // ARRANGE
      CustomerRequest request = new CustomerRequest();
      request.setName("John");
      request.setEmail("j@gmail.com");

      Customer savedCustomer = new Customer("John", "j@gmail.com");

      when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
      when(customerRepository.existsByEmailIgnoreCase("j@gmail.com"))
      .thenReturn(false);
      // ACT

      CustomerResponse response = customerService.createCustomer(request);

      // ASSERT
      assertEquals("John", response.getName());
      assertEquals("j@gmail.com", response.getEmail());

      // VERIFY
      verify(customerRepository).save(any(Customer.class));
      verify(customerRepository).existsByEmailIgnoreCase("j@gmail.com");  
  }

  @Test
  void shouldUpdateCustomer() {

      // ARRANGE
      Customer existingCustomer = new Customer(1L, "John", "john@test.com");

      CustomerRequest request = new CustomerRequest();
      request.setName("Mike");
      request.setEmail("m@gmail.com");

      when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
      when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

      // ACT

      CustomerResponse response = customerService.updateCustomer(1L, request);
      // ASSERT

      assertEquals("Mike", response.getName());
      assertEquals("m@gmail.com", response.getEmail());

      // VERIFY
      verify(customerRepository).findById(1L);
      verify(customerRepository).save(any(Customer.class));
  }





  ////////

    @Test
    void shouldNotUpdateWhenCustomerDoesNotExist() {
      // ARRANGE

      CustomerRequest request = new CustomerRequest();
      request.setName("Mike");
      request.setEmail("m@gmail.com");


      when(customerRepository.findById(99L)).thenReturn(Optional.empty());

      // ACT + ASSERT

      assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(99L, request));

      // VERIFY
      verify(customerRepository).findById(99L);
      verify(customerRepository, never()).save(any(Customer.class));
    }



    @Test
    void shouldReturnCustomerWhenCustomerExists() {

      // Arrange
      Customer customer = new Customer();
      customer.setId(2L);
      customer.setName("John");
      customer.setEmail("john@test.com");

      when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));

      // Act
      CustomerResponse response = customerService.getCustomerById(2L);

      // Assert
      assertEquals(2L, response.getId());
      assertEquals("John", response.getName());
      assertEquals("john@test.com", response.getEmail());

      // VERIFY
      verify(customerRepository).findById(2L);
    }

      @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

      // Arrange
      when(customerRepository.findById(99L)).thenReturn(Optional.empty());

      // Act + Assert
      assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(99L));
    }

    @Test
  void shouldDeleteCustomerWhenCustomerExists() {
      
      // ARRANGE
      Customer customer = new Customer();
      customer.setId(2L);
      customer.setName("John");
      customer.setEmail("john@test.com");

      when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
      when(saleRepository.existsByCustomerId(2L))
      .thenReturn(false);   

      // ACT
      customerService.deleteCustomer(2L);

      // VERIFY
      verify(customerRepository).findById(2L);
      verify(saleRepository).existsByCustomerId(2L);
      verify(customerRepository).delete(customer);
  }

    @Test
    void shouldNotDeleteWhenCustomerDoesNotExist() {
      // ARRANGE
      when(customerRepository.findById(99L)).thenReturn(Optional.empty());

      // ACT + ASSERT

      assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(99L));

      // VERIFY
      verify(customerRepository).findById(99L);
      verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
  void shouldNotDeleteCustomerWhenCustomerIsInUse() {

    // ARRANGE
    Customer customer =
        new Customer(2L, "John", "john@test.com");

    when(customerRepository.findById(2L))
        .thenReturn(Optional.of(customer));

    when(saleRepository.existsByCustomerId(2L))
        .thenReturn(true);

    // ACT + ASSERT
    assertThrows(
        CustomerInUseException.class,
        () -> customerService.deleteCustomer(2L)
    );

    // VERIFY
    verify(customerRepository).findById(2L);
    verify(saleRepository).existsByCustomerId(2L);
    verify(customerRepository, never()).delete(any(Customer.class));
  }


  @Test
void shouldNotCreateCustomerWhenEmailAlreadyExists() {

  // ARRANGE
  CustomerRequest request = new CustomerRequest();
  request.setName("John");
  request.setEmail("j@gmail.com");

  when(customerRepository.existsByEmailIgnoreCase("j@gmail.com"))
      .thenReturn(true);

  // ACT + ASSERT
  assertThrows(
      CustomerEmailAlreadyExistsException.class,
      () -> customerService.createCustomer(request)
  );

  // VERIFY
  verify(customerRepository).existsByEmailIgnoreCase("j@gmail.com");
  verify(customerRepository, never()).save(any(Customer.class));
}

    }