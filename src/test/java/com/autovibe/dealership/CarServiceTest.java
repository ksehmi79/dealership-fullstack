package com.autovibe.dealership;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.autovibe.dealership.dto.CarRequest;
import com.autovibe.dealership.dto.CarResponse;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CarServiceTest {

  @Mock private CarRepository carRepository;
  @Mock private SaleRepository saleRepository;

  private CarService carService;

  @BeforeEach
  void setUp() {

    MockitoAnnotations.openMocks(this);

    carService = new CarService(carRepository, saleRepository);
  }

  @Test
  void shouldReturnCarWhenCarExists() {

    // Arrange
    Car car = new Car();
    car.setId(2L);
    car.setMake("Honda");
    car.setModel("Civic");
    car.setYear(2021);
    car.setPrice(new BigDecimal("22000.00"));

    when(carRepository.findById(2L)).thenReturn(Optional.of(car));

    // Act
    CarResponse response = carService.getCarById(2L);

    // Assert
    assertEquals(2L, response.getId());
    assertEquals("Honda", response.getMake());
    assertEquals("Civic", response.getModel());
    assertEquals(2021, response.getYear());
    assertEquals(new BigDecimal("22000.00"), response.getPrice());

    // VERIFY
    verify(carRepository).findById(2L);
  }

  @Test
  void shouldThrowExceptionWhenCarDoesNotExist() {

    // Arrange
    when(carRepository.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(CarNotFoundException.class, () -> carService.getCarById(99L));
  }

  @Test
  void shouldAddCarSuccessfully() {

    // ARRANGE
    CarRequest request = new CarRequest();
    request.setMake("Honda");
    request.setModel("Accord");
    request.setYear(2023);
    request.setPrice(new BigDecimal("29000.00"));

    Car savedCar = new Car(20L, "Honda", "Accord", 2023, new BigDecimal("29000.00"));

    when(carRepository.save(any(Car.class))).thenReturn(savedCar);

    // ACT
    CarResponse response = carService.addCar(request);

    // ASSERT
    assertEquals(20L, response.getId());
    assertEquals("Honda", response.getMake());
    assertEquals("Accord", response.getModel());
    assertEquals(2023, response.getYear());
    assertEquals(new BigDecimal("29000.00"), response.getPrice());

    // VERIFY
    verify(carRepository).save(any(Car.class));
  }

  @Test
  void shouldSaveCorrectCarData() {

    // ARRANGE
    CarRequest request = new CarRequest();
    request.setMake("Toyota");
    request.setModel("RAV4");
    request.setYear(2024);
    request.setPrice(new BigDecimal("36000.00"));

    Car savedCar = new Car(25L, "Toyota", "RAV4", 2024, new BigDecimal("36000.00"));

    when(carRepository.save(any(Car.class))).thenReturn(savedCar);

    // ACT
    CarResponse response = carService.addCar(request);

    // CAPTURE what CarService sent to repository.save()
    ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);

    verify(carRepository).save(carCaptor.capture());

    Car capturedCar = carCaptor.getValue();

    // ASSERT what was sent to repository
    assertEquals("Toyota", capturedCar.getMake());
    assertEquals("RAV4", capturedCar.getModel());
    assertEquals(2024, capturedCar.getYear());
    assertEquals(new BigDecimal("36000.00"), capturedCar.getPrice());

    // ASSERT returned response
    assertEquals(25L, response.getId());
    assertEquals("Toyota", response.getMake());
    assertEquals("RAV4", response.getModel());
  }

  @Test
  void shouldDeleteCarWhenCarExists() {

    // ARRANGE
    Car car = new Car();
    car.setId(2L);
    car.setMake("Honda");
    car.setModel("Civic");
    car.setYear(2021);
    car.setPrice(new BigDecimal("22000.00"));

    when(carRepository.findById(2L)).thenReturn(Optional.of(car));
    when(saleRepository.existsByCarId(2L)).thenReturn(false);

    // ACT
    carService.deleteCar(2L);

    // VERIFY
    verify(carRepository).findById(2L);
    verify(saleRepository).existsByCarId(2L);
    verify(carRepository).delete(car);
  }

  @Test
  void shouldNotDeleteWhenCarDoesNotExist() {
    // ARRANGE
    when(carRepository.findById(99L)).thenReturn(Optional.empty());

    // ACT + ASSERT

    assertThrows(CarNotFoundException.class, () -> carService.deleteCar(99L));

    // VERIFY
    verify(carRepository).findById(99L);
    verify(carRepository, never()).delete(any(Car.class));
  }

  @Test
  void shouldUpdateCarSuccessfully() {

    // ARRANGE
    Car existingCar = new Car(2L, "Honda", "Civic", 2021, new BigDecimal("22000.00"));

    CarRequest request = new CarRequest();
    request.setMake("Honda");
    request.setModel("Civic");
    request.setYear(2024);
    request.setPrice(new BigDecimal("27000.00"));

    when(carRepository.findById(2L)).thenReturn(Optional.of(existingCar));

    when(carRepository.save(any(Car.class))).thenReturn(existingCar);

    // ACT
    CarResponse response = carService.updateCar(2L, request);

    // ASSERT
    assertEquals(request.getYear(), response.getYear());
    assertEquals(request.getPrice(), response.getPrice());

    // veryfy

    verify(carRepository).findById(2L);
    verify(carRepository).save(any(Car.class));
  }

  @Test
  void shouldNotUpdateWhenCarDoesNotExist() {
    // ARRANGE

    CarRequest request = new CarRequest();
    request.setMake("Honda");
    request.setModel("Civic");
    request.setYear(2024);
    request.setPrice(new BigDecimal("27000.00"));

    when(carRepository.findById(99L)).thenReturn(Optional.empty());

    // ACT + ASSERT

    assertThrows(CarNotFoundException.class, () -> carService.updateCar(99L, request));

    // VERIFY
    verify(carRepository).findById(99L);
    verify(carRepository, never()).save(any(Car.class));
  }

  @Test
void shouldNotDeleteCarWhenCarIsInUse() {

  // ARRANGE
  Car car = new Car(
      2L,
      "Honda",
      "Civic",
      2021,
      new BigDecimal("22000.00")
  );

  when(carRepository.findById(2L)).thenReturn(Optional.of(car));
  when(saleRepository.existsByCarId(2L)).thenReturn(true);

  // ACT + ASSERT
  assertThrows(
      CarInUseException.class,
      () -> carService.deleteCar(2L)
  );

  // VERIFY
  verify(carRepository).findById(2L);
  verify(saleRepository).existsByCarId(2L);
  verify(carRepository, never()).delete(any(Car.class));
}
}


