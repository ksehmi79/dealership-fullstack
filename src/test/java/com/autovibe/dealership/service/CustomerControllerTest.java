package com.autovibe.dealership.service;


import com.autovibe.dealership.CustomerController;
import com.autovibe.dealership.CustomerNotFoundException;
import com.autovibe.dealership.CustomerService;

import com.autovibe.dealership.dto.CustomerRequest;
import com.autovibe.dealership.dto.CustomerResponse;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.checkerframework.checker.units.qual.m;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void shouldReturnAllCustomers() throws Exception {

        // ARRANGE
        CustomerResponse customer1 =
            new CustomerResponse(1L, "John", "john@test.com");

        CustomerResponse customer2 =
            new CustomerResponse(2L, "Jane", "jane@test.com");

        when(customerService.getAllCustomers())
            .thenReturn(List.of(customer1, customer2));

        // ACT + ASSERT
        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("John"))
            .andExpect(jsonPath("$[0].email").value("john@test.com"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("Jane"))
            .andExpect(jsonPath("$[1].email").value("jane@test.com"));

        // VERIFY
        verify(customerService).getAllCustomers();
    }

    @Test
void shouldReturnCustomerById() throws Exception {

    // ARRANGE
    CustomerResponse customer =
    new CustomerResponse(1L, "John", "john@test.com");
    when(customerService.getCustomerById(1L)).thenReturn(customer);

    // ACT + ASSERT
    mockMvc.perform(get("/customers/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("John"))
        .andExpect(jsonPath("$.email").value("john@test.com"));

    // VERIFY
    verify(customerService).getCustomerById(1L);
}

    @Autowired
    private ObjectMapper objectMapper;
@Test
    void shouldCreateCustomerSuccessfully() throws Exception {

        // ARRANGE
        CustomerRequest request = new CustomerRequest();
        request.setName("John");
        request.setEmail("john@test.com");

        CustomerResponse response = new CustomerResponse(
            1L,
            "John",
            "john@test.com"
        );

        when(customerService.createCustomer(any(CustomerRequest.class)))
            .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("John"))
            .andExpect(jsonPath("$.email").value("john@test.com"));

            verify(customerService)
                .createCustomer(any(CustomerRequest.class));
    }

    @Test
    void shouldReturn400ForInvalidCustomerRequest() throws Exception {

        // ARRANGE
        CustomerRequest request = new CustomerRequest();
        request.setName("");
        request.setEmail("invalid-email");

        // ACT + ASSERT
        mockMvc.perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());

        // VERIFY
        verify(customerService, never())
            .createCustomer(any(CustomerRequest.class));
    }

 @Test
    void shouldUpdateCustomerSuccessfully() throws Exception {

        // ARRANGE
        CustomerRequest request = new CustomerRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@test.com");

        CustomerResponse response = new CustomerResponse(
            1L,
            "John Doe",
            "john.doe@test.com"
        );

        when(customerService.updateCustomer(
                eq(1L),
                any(CustomerRequest.class)
        )).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                put("/customers/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john.doe@test.com"));

        // VERIFY
        verify(customerService).updateCustomer(
            eq(1L),
            any(CustomerRequest.class)
        );
    }

    @Test
void shouldReturn400ForInvalidUpdateCustomerRequest() throws Exception {

    // ARRANGE
    CustomerRequest request = new CustomerRequest();
    request.setName("");
    request.setEmail("invalid-email");

    // ACT + ASSERT
    mockMvc.perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

    // VERIFY
    verify(customerService, never())
        .updateCustomer(eq(1L), any(CustomerRequest.class));
}

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {

        // ACT + ASSERT
        mockMvc.perform(delete("/customers/2"))
            .andExpect(status().isNoContent());

        // VERIFY
        verify(customerService).deleteCustomer(2L);
    }

    @Test
void shouldReturn404WhenCustomerDoesNotExist() throws Exception {

    // ARRANGE
    when(customerService.getCustomerById(99L))
        .thenThrow(new CustomerNotFoundException(
            "Customer not found with id: 99"
        ));

    // ACT + ASSERT
    mockMvc.perform(get("/customers/99"))
        .andExpect(status().isNotFound());

    // VERIFY
    verify(customerService).getCustomerById(99L);
}

@Test 
void shouldReturn404WhenDeletingNonExistentCustomer() throws Exception {

    // ARRANGE
    
        doThrow(new CustomerNotFoundException("Customer not found with id: 99"))
            .when(customerService)
            .deleteCustomer(99L);

    // ACT + ASSERT
    mockMvc.perform(delete("/customers/99"))
        .andExpect(status().isNotFound());

    // VERIFY
    verify(customerService).deleteCustomer(99L);
}
}