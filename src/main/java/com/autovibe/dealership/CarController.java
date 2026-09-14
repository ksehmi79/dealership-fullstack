  package com.autovibe.dealership;

  import com.autovibe.dealership.dto.CarRequest;
  import com.autovibe.dealership.dto.CarResponse;

  import io.swagger.v3.oas.annotations.Operation;
  import io.swagger.v3.oas.annotations.tags.Tag;

  import java.math.BigDecimal;
  import java.util.List;
  import org.springframework.data.domain.Page;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.validation.annotation.Validated;
  import org.springframework.web.bind.annotation.DeleteMapping;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.PutMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;

  @RestController
  @RequestMapping("/cars")
  @Tag(
      name = "Cars",
      description = "APIs for managing dealership vehicle inventory"
  )
  public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
      this.carService = carService;
    }

    @Operation(summary = "Get all cars")
    @GetMapping
    public List<CarResponse> getCars() {

      return carService.getCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
      CarResponse response = carService.getCarById(id);

      return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a new car")
    @PostMapping
    public ResponseEntity<CarResponse> addCar(@Validated @RequestBody CarRequest request) {
      CarResponse response = carService.addCar(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing car")
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
        @PathVariable Long id, @Validated @RequestBody CarRequest request) {

      CarResponse response = carService.updateCar(id, request);
      return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a car")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
      carService.deleteCar(id);

      return ResponseEntity.noContent().build();
    }

    @GetMapping("/price-above/{price}")
    public List<CarResponse> getCarsAbovePrice(@PathVariable BigDecimal price) {

      return carService.getCarsAbovePrice(price);
    }

    @GetMapping("/search/{make}/{year}")
    public List<CarResponse> getCarsByMakeAndYear(@PathVariable String make, @PathVariable int year) {

      return carService.getCarsByMakeAndYear(make, year);
    }


    @GetMapping("/page")
    public ResponseEntity<Page<CarResponse>> getCarsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

      Page<CarResponse> result = carService.getCarsPage(page, size, sortBy, direction);

      return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> searchCars(
        @RequestParam String make,
        @RequestParam int year,
        @RequestParam BigDecimal minPrice,
        @RequestParam BigDecimal maxPrice) {

      List<CarResponse> cars = carService.searchCars(make, year, minPrice, maxPrice);

      return ResponseEntity.ok(cars);
    }
  }
