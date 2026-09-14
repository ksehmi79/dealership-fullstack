package com.autovibe.dealership;

import com.autovibe.dealership.dto.CarRequest;
import com.autovibe.dealership.dto.CarResponse;

import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.checkerframework.checker.units.qual.m;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.autovibe.dealership.dto.CarRequest;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;

    @Test
    void shouldReturnCarWhenCarExists() throws Exception {

        // ARRANGE
        CarResponse response = new CarResponse(
            2L,
            "Honda",
            "Civic",
            2021,
            new BigDecimal("22000.00")
        );

        when(carService.getCarById(2L))
            .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(get("/cars/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.make").value("Honda"))
            .andExpect(jsonPath("$.model").value("Civic"))
            .andExpect(jsonPath("$.year").value(2021))
            .andExpect(jsonPath("$.price").value(22000));
    }   

    @Test
    void shouldReturn404WhenCarDoesNotExist() throws Exception {

        // ARRANGE
        when(carService.getCarById(99L))
            .thenThrow(
                new CarNotFoundException(
                    "Car not found with id: 99"
                )
            );

        // ACT + ASSERT
        mockMvc.perform(get("/cars/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenCarDoesNotExistWithMessage() throws Exception {

        // ARRANGE
        when(carService.getCarById(99L))
            .thenThrow(
                new CarNotFoundException(
                    "Car not found with id: 99"
                )
            );

        // ACT + ASSERT
        mockMvc.perform(get("/cars/99"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(
                "Car not found with id: 99"
            ));
    }


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCarSuccessfully() throws Exception {

        // ARRANGE
        CarRequest request = new CarRequest();
        request.setMake("Honda");
        request.setModel("Accord");
        request.setYear(2024);
        request.setPrice(new BigDecimal("30000.00"));

        CarResponse response = new CarResponse(
            10L,
            "Honda",
            "Accord",
            2024,
            new BigDecimal("30000")
        );

        when(carService.addCar(any(CarRequest.class)))
            .thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                post("/cars")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.make").value("Honda"))
            .andExpect(jsonPath("$.model").value("Accord"))
            .andExpect(jsonPath("$.year").value(2024))
            .andExpect(jsonPath("$.price").value(30000));
    }

    @Test
    void shouldReturn400WhenCarRequestIsInvalid() throws Exception {

        // ARRANGE
        CarRequest request = new CarRequest();
        request.setMake("");
        request.setModel("");
        request.setYear(1800);
        request.setPrice(new BigDecimal("-5000.00"));

        // ACT + ASSERT
        mockMvc.perform(
                post("/cars")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isBadRequest());

        // VERIFY
        verify(carService, never())
            .addCar(any(CarRequest.class));
    }


    @Test
    void shouldDeleteCarSuccessfully() throws Exception {

        // ACT + ASSERT
        mockMvc.perform(delete("/cars/2"))
            .andExpect(status().isNoContent());

        // VERIFY
        verify(carService).deleteCar(2L);
    }


    @Test
    void shouldUpdateCarSuccessfully() throws Exception {

        // ARRANGE
        CarRequest request = new CarRequest();
        request.setMake("Honda");
        request.setModel("Civic");
        request.setYear(2024);
        request.setPrice(new BigDecimal("27000"));

        CarResponse response = new CarResponse(
            2L,
            "Honda",
            "Civic",
            2024,
            new BigDecimal("27000")
        );

        when(carService.updateCar(
                eq(2L),
                any(CarRequest.class)
        )).thenReturn(response);

        // ACT + ASSERT
        mockMvc.perform(
                put("/cars/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.make").value("Honda"))
            .andExpect(jsonPath("$.model").value("Civic"))
            .andExpect(jsonPath("$.year").value(2024))
            .andExpect(jsonPath("$.price").value(27000));

        // VERIFY
        verify(carService).updateCar(
            eq(2L),
            any(CarRequest.class)
        );
    }
}