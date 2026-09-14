package com.autovibe.dealership;

import com.autovibe.dealership.dto.CarRequest;
import com.autovibe.dealership.dto.CarResponse;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CarService {

  private final CarRepository carRepository;
  private final SaleRepository saleRepository;

  public CarService(CarRepository carRepository, SaleRepository saleRepository) {
    this.carRepository = carRepository;
    this.saleRepository = saleRepository;
  }

  public List<CarResponse> getCars() {
    return carRepository.findAll().stream().map(this::toResponse).toList();
  }

  public CarResponse getCarById(Long id) {
    Car car =
        carRepository
            .findById(id)
            .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));
    return toResponse(car);
  }

  public CarResponse addCar(CarRequest request) {

    Car car = toEntity(request);

    Car savedCar = carRepository.save(car);

    return toResponse(savedCar);
  }

  public CarResponse updateCar(Long id, CarRequest request) {

    Car existingCar =
        carRepository
            .findById(id)
            .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));

    existingCar.setMake(request.getMake());
    existingCar.setModel(request.getModel());
    existingCar.setYear(request.getYear());
    existingCar.setPrice(request.getPrice());

    Car savedCar = carRepository.save(existingCar);
    return toResponse(savedCar);
  }

  public void deleteCar(Long id) {

    Car existingCar =
        carRepository
            .findById(id)
            .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));

            if (saleRepository.existsByCarId(id)) {
              throw new CarInUseException("Car with id " + id + " is currently in use and cannot be deleted.");
            }

    carRepository.delete(existingCar);
  }

  public List<CarResponse> getCarsAbovePrice(BigDecimal price) {
    return carRepository.findByPriceGreaterThan(price)
      .stream().map(this::toResponse).toList();
  }

  public List<CarResponse> getCarsBelowPrice(BigDecimal price) {
    return carRepository.findByPriceLessThan(price)
      .stream().map(this::toResponse).toList();
  }

  public List<CarResponse> getCarsByMakeAndYear(String make, int year) {
    return carRepository.findByMakeIgnoreCaseAndYear(make, year)
      .stream().map(this::toResponse).toList()  ;
  }


  private CarResponse toResponse(Car car) {
    return new CarResponse(
        car.getId(), car.getMake(), car.getModel(), car.getYear(), car.getPrice());
  }

  private Car toEntity(CarRequest request) {

    return new Car(request.getMake(), request.getModel(), request.getYear(), request.getPrice());
  }

  public Page<CarResponse> getCarsPage(int page, int size, String sortBy, String direction) {

    Sort sort =
        direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    return carRepository.findAll(pageable).map(this::toResponse);
  }

  public List<CarResponse> searchCars(String make, int year, BigDecimal minPrice, BigDecimal maxPrice) {

    List<CarResponse> searchedCars =
        carRepository
            .searchCars(make, year, minPrice, maxPrice)
            .stream()
            .map(this::toResponse)
            .toList();

    return searchedCars;
  }
}
