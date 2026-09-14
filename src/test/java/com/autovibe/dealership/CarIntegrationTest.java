package com.autovibe.dealership;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.autovibe.dealership.dto.CarRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CarIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JsonMapper objectMapper;

  private ResultActions perform;

  @Test
  void shouldCreateCarThroughFullApplication() throws Exception {

    // ARRANGE
    CarRequest request = new CarRequest();
    request.setMake("Toyota");
    request.setModel("RAV4");
    request.setYear(2025);
    request.setPrice(new BigDecimal("38000.00"));

    // ACT + ASSERT
    mockMvc
        .perform(
            post("/cars")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.make").value("Toyota"))
        .andExpect(jsonPath("$.model").value("RAV4"))
        .andExpect(jsonPath("$.year").value(2025))
        .andExpect(jsonPath("$.price").value(38000.00));
  }

  @Test
  void shouldCreateAndRetrieveCar() throws Exception {

    // ARRANGE
    CarRequest request = new CarRequest();
    request.setMake("Honda");
    request.setModel("CR-V");
    request.setYear(2025);
    request.setPrice(new BigDecimal("36000.00"));

    // CREATE
    String responseBody =
        mockMvc
            .perform(
                post("/cars")
                    .with(user("admin").roles("ADMIN"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.make").value("Honda"))
            .andExpect(jsonPath("$.model").value("CR-V"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    // We will extract the ID next
    Long carId = objectMapper.readTree(responseBody).get("id").asLong();

    mockMvc
        .perform(get("/cars/" + carId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(carId))
        .andExpect(jsonPath("$.make").value("Honda"))
        .andExpect(jsonPath("$.model").value("CR-V"))
        .andExpect(jsonPath("$.year").value(2025))
        .andExpect(jsonPath("$.price").value(36000.00));
  }

  @Test
  void shouldUpdateCarThroughFullApplication() throws Exception {

    // ARRANGE - create the original car
    CarRequest originalRequest = new CarRequest();
    originalRequest.setMake("Honda");
    originalRequest.setModel("Civic");
    originalRequest.setYear(2021);
    originalRequest.setPrice(new BigDecimal("22000.00"));

    String responseBody =
        mockMvc
            .perform(
                post("/cars")
                    .with(user("admin").roles("ADMIN"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(originalRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Long carId = objectMapper.readTree(responseBody).get("id").asLong();

    CarRequest updatedRequest = new CarRequest();
    updatedRequest.setMake("Honda");
    updatedRequest.setModel("Civic");
    updatedRequest.setYear(2025);
    updatedRequest.setPrice(new BigDecimal("29000.00"));

    mockMvc
        .perform(
            put("/cars/" + carId)
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.make").value("Honda"))
        .andExpect(jsonPath("$.model").value("Civic"))
        .andExpect(jsonPath("$.year").value(2025))
        .andExpect(jsonPath("$.price").value(29000));

    mockMvc
        .perform(get("/cars/" + carId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(carId))
        .andExpect(jsonPath("$.make").value("Honda"))
        .andExpect(jsonPath("$.model").value("Civic"))
        .andExpect(jsonPath("$.year").value(2025))
        .andExpect(jsonPath("$.price").value(29000));
  }

  @Test
  void salespersonCannotCreateCar() throws Exception {

    CarRequest request = new CarRequest();
    request.setMake("Toyota");
    request.setModel("Corolla");
    request.setYear(2025);
    request.setPrice(new BigDecimal("28000.00"));

    mockMvc
        .perform(
            post("/cars")
                .with(user("sales").roles("SALESPERSON"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

  @Test
  void unauthenticatedUserCannotCreateCar() throws Exception {

    CarRequest request = new CarRequest();
    request.setMake("Toyota");
    request.setModel("Corolla");
    request.setYear(2025);
    request.setPrice(new BigDecimal("28000.00"));

    mockMvc
        .perform(
            post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void adminCanCreateCarUsingRealJwt() throws Exception {

    // ARRANGE - login as real admin
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("admin");
    loginRequest.setPassword("admin123");

    String loginResponse =
        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract the real JWT returned by /auth/login
    String token = objectMapper.readTree(loginResponse).get("token").asString();

    CarRequest carRequest = new CarRequest();
    carRequest.setMake("BMW");
    carRequest.setModel("X3");
    carRequest.setYear(2025);
    carRequest.setPrice(new BigDecimal("55000.00"));

    // ACT + ASSERT - use the real JWT
    mockMvc
        .perform(
            post("/cars")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.make").value("BMW"))
        .andExpect(jsonPath("$.model").value("X3"));
  }

  @Test
  void salespersonCannotCreateCarUsingRealJwt() throws Exception {

    // ARRANGE - login as real salesperson
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("sales");
    loginRequest.setPassword("sales123");

    String loginResponse =
        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Extract real JWT
    String token = objectMapper.readTree(loginResponse).get("token").asString();

    CarRequest carRequest = new CarRequest();
    carRequest.setMake("Audi");
    carRequest.setModel("Q5");
    carRequest.setYear(2025);
    carRequest.setPrice(new BigDecimal("50000.00"));

    // ACT + ASSERT
    mockMvc
        .perform(
            post("/cars")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  void tamperedJwtShouldReturnUnauthorized() throws Exception {

    // ARRANGE - login and get a real valid token
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("admin");
    loginRequest.setPassword("admin123");

    String loginResponse =
        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String token = objectMapper.readTree(loginResponse).get("token").asString();

    // Change the token so the signature becomes invalid
    String tamperedToken = token + "abc";

    CarRequest carRequest = new CarRequest();
    carRequest.setMake("Tesla");
    carRequest.setModel("Model Y");
    carRequest.setYear(2025);
    carRequest.setPrice(new BigDecimal("60000.00"));

    // ACT + ASSERT
    mockMvc
        .perform(
            post("/cars")
                .header("Authorization", "Bearer " + tamperedToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
    void salespersonCannotDeleteCustomer() throws Exception {

    mockMvc
        .perform(
            delete("/customers/1")
                .with(user("sales").roles("SALESPERSON"))
        )
        .andExpect(status().isForbidden());
    }

    @Test
void adminCanDeleteCustomer() throws Exception {

  mockMvc
      .perform(
          delete("/customers/999")
              .with(user("admin").roles("ADMIN"))
      )
      .andExpect(status().isNotFound());
}
}
