package com.autovibe.dealership.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.autovibe.dealership.SaleController;
import com.autovibe.dealership.SaleService;
import com.autovibe.dealership.dto.CarRequest;
import com.autovibe.dealership.dto.CustomerRequest;
import com.autovibe.dealership.dto.CustomerResponse;
import com.autovibe.dealership.dto.SaleRequest;
import com.autovibe.dealership.dto.SaleResponse;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(SaleController.class)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleService saleService;

    @Test
    void shouldCreateSaleSuccessfully() throws Exception {

        // ARRANGE
        SaleRequest request = new SaleRequest();
        request.setCustomerId(1L);
        request.setCarId(1L);
        request.setSalePrice(new BigDecimal("28000.00"));

        SaleResponse response = new SaleResponse(
            1L,
            new BigDecimal("28000.00"),
            1L,
            "John",
            1L,
            "Toyota",
            "Camry"
        );

        when(saleService.createSale(
                any(Long.class),
                any(Long.class),
                any(BigDecimal.class)
        )).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                post("/sales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.salePrice").value(28000.00))
            .andExpect(jsonPath("$.customerId").value(1L))
            .andExpect(jsonPath("$.customerName").value("John"))
            .andExpect(jsonPath("$.carId").value(1L))
            .andExpect(jsonPath("$.carMake").value("Toyota"))
            .andExpect(jsonPath("$.carModel").value("Camry"));

        // VERIFY
        verify(saleService).createSale(
            any(Long.class),
            any(Long.class),
            any(BigDecimal.class)
        );
    }



    @Test
    void shouldReturn400ForInvalidSaleRequest() throws Exception {
        
        // ARRANGE
        SaleRequest request = new SaleRequest();
        request.setCustomerId(null);
        request.setCarId(null);
        request.setSalePrice(new BigDecimal("-1000.00"));

        // ACT + ASSERT
        mockMvc.perform(
                post("/sales")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());

        // VERIFY
        verify(saleService, never()).createSale(
            any(Long.class),
            any(Long.class),
            any(BigDecimal.class)
        );
    }


    @Test
    void shouldReturnAllSales() throws Exception {
        //ARRANGE
        SaleResponse sale1 = new SaleResponse(
            1L,
            new BigDecimal("25000.00"),
            1L,
            "John",
            1L,
            "Toyota",
            "Camry"
        );
        SaleResponse sale2 = new SaleResponse(
            2L,
            new BigDecimal("30000.00"),
            2L,
            "Jane",
            2L,
            "Honda",
            "Accord"
        );

        when(saleService.getAllSales()).thenReturn(List.of(sale1, sale2));

        // ACT + ASSERT
        mockMvc.perform(get("/sales"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].salePrice").value(25000.00))
            .andExpect(jsonPath("$[0].customerId").value(1L))
            .andExpect(jsonPath("$[0].customerName").value("John"))
            .andExpect(jsonPath("$[0].carId").value(1L))
            .andExpect(jsonPath("$[0].carMake").value("Toyota"))
            .andExpect(jsonPath("$[0].carModel").value("Camry"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].salePrice").value(30000.00))
            .andExpect(jsonPath("$[1].customerId").value(2L))
            .andExpect(jsonPath("$[1].customerName").value("Jane"))
            .andExpect(jsonPath("$[1].carId").value(2L))
            .andExpect(jsonPath("$[1].carMake").value("Honda"))
            .andExpect(jsonPath("$[1].carModel").value("Accord"));

        // VERIFY
        verify(saleService).getAllSales();
    }   


    @Test
    void shouldReturnSalesByCustomer() throws Exception {

        // ARRANGE
        SaleResponse sale1 = new SaleResponse(
            1L,
            new BigDecimal("25000.00"),
            1L,
            "John",
            1L,
            "Toyota",
            "Camry"
        );
        when(saleService.getSalesByCustomer(1L)).thenReturn(List.of(sale1));

        // ACT + ASSERT
        mockMvc.perform(get("/sales/customer/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].salePrice").value(25000.00))
            .andExpect(jsonPath("$[0].customerId").value(1L))
            .andExpect(jsonPath("$[0].customerName").value("John"))
            .andExpect(jsonPath("$[0].carId").value(1L))
            .andExpect(jsonPath("$[0].carMake").value("Toyota"))
            .andExpect(jsonPath("$[0].carModel").value("Camry"));

        // VERIFY
        verify(saleService).getSalesByCustomer(1L);
    }

}