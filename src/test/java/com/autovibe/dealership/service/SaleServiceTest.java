package com.autovibe.dealership.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.autovibe.dealership.Car;
import com.autovibe.dealership.CarNotFoundException;
import com.autovibe.dealership.CarRepository;
import com.autovibe.dealership.Customer;
import com.autovibe.dealership.CustomerNotFoundException;
import com.autovibe.dealership.CustomerRepository;
import com.autovibe.dealership.CustomerService;
import com.autovibe.dealership.Sale;
import com.autovibe.dealership.SaleRepository;
import com.autovibe.dealership.SaleService;
import com.autovibe.dealership.dto.CarResponse;
import com.autovibe.dealership.dto.CustomerResponse;
import com.autovibe.dealership.dto.SaleResponse;

class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSaleSuccessfully() {

        
        // ARRANGE
       Customer customer = new Customer(1L, "John Doe", "A@g.com");
        Car car = new Car(1L, "Toyota", "Camry", 2020, new BigDecimal("20000.00"));
 
        Sale savedSale = new Sale();
            savedSale.setCustomer(customer);
            savedSale.setCar(car);
            savedSale.setSalePrice(new BigDecimal("25000.00"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);   

        // ACT
        SaleResponse response = saleService.createSale(1L, 1L, new BigDecimal("25000.00"));


        
        // ASSERT

        assertEquals(1L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());

        assertEquals(1L, response.getCarId());
        assertEquals("Toyota", response.getCarMake());
        assertEquals("Camry", response.getCarModel());

        assertEquals(
            new BigDecimal("25000.00"),
            response.getSalePrice()
        );

        // VERIFY

        verify(customerRepository).findById(1L);
        verify(carRepository).findById(1L);
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void shouldNotCreateSaleWhenCustomerDoesNotExist() {

        // ARRANGE
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(CustomerNotFoundException.class, () -> saleService.createSale(99L, 1L, new BigDecimal("25000.00")));

        // VERIFY
        verify(customerRepository).findById(99L);
        verify(carRepository, never()).findById(any(Long.class));
        verify(saleRepository, never()).save(any(Sale.class));

    }

    @Test
    void shouldNotCreateSaleWhenCarDoesNotExist() {
        // ARRANGE
        Customer customer = new Customer(1L, "John Doe", "A@g.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(CarNotFoundException.class, () -> saleService.createSale(1L, 99L, new BigDecimal("25000.00")));

        // VERIFY
        verify(customerRepository).findById(1L);
        verify(carRepository).findById(99L);
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void shouldReturnAllSales() {

        // ARRANGE
        Customer customer1 = new Customer(1L, "John Doe", "john.doe@example.com");
        Customer customer2 = new Customer(2L, "Jane Smith", "jane.smith@example.com");
        Car car1 = new Car(1L, "Toyota", "Camry", 2020, new BigDecimal("20000.00"));
        Car car2 = new Car(2L, "Honda", "Civic", 2021, new BigDecimal("22000.00"));
        Sale sale1 = new Sale(
            new BigDecimal("25000.00"),
            customer1,
            car1
        );

        Sale sale2 = new Sale(
            new BigDecimal("27000.00"),
            customer2,
            car2
        );
        when(saleRepository.findAll()).thenReturn(List.of(sale1, sale2));

        // ACT
        List<SaleResponse> responses = saleService.getAllSales();

        // ASSERT
        assertEquals("John Doe", responses.get(0).getCustomerName());
        assertEquals("Toyota", responses.get(0).getCarMake());
        assertEquals("Camry", responses.get(0).getCarModel());

        assertEquals("Jane Smith", responses.get(1).getCustomerName());
        assertEquals("Honda", responses.get(1).getCarMake());
        assertEquals("Civic", responses.get(1).getCarModel());
        // VERIFY
        verify(saleRepository).findAll();
}       
}