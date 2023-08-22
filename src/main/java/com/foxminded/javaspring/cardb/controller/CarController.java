package com.foxminded.javaspring.cardb.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.javaspring.cardb.dto.CarCreateUpdateDto;
import com.foxminded.javaspring.cardb.dto.CarUpdatePartDto;
import com.foxminded.javaspring.cardb.model.Car;
import com.foxminded.javaspring.cardb.service.CarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Data;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

	private CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@Operation(summary = "Get all cars paginated", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Found all cars")
	@GetMapping
	public List<Car> findCars(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) throws SQLException {
		if (page == null && size == null) {
			return carService.findAllCars();
		}
		Page<Car> resultPage = carService.findAllCars(page, size,
				Sort.by("make").descending().and(Sort.by("model").descending()));
		return resultPage.getContent();
	}

	@Operation(summary = "Get car by objectId", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Found the car")
	@GetMapping("/{objectId}")
	public Car findCarByObjectId(@PathVariable("objectId") String objectId) throws SQLException {
		return carService.findCarByObjectId(objectId);
	}

	@Operation(summary = "Get all cars of selected make, model and category", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Found the cars")
	@GetMapping("/{make}/{model}/{category}")
	public List<Car> findCarsByYears(@PathVariable("make") String make, @PathVariable("model") String model,
			@PathVariable("category") String category, @RequestParam("minYear") Integer minYear,
			@RequestParam("maxYear") Integer maxYear) {
		return carService.findCars(make, model, minYear, maxYear, category);
	}

	@Operation(summary = "Create new car", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Car created")
	@PostMapping
	public ResponseEntity<String> createCar(@RequestBody CarCreateUpdateDto carDto) throws SQLException {
		Car car = new Car();
		car.setObjectId(carDto.getObjectId());
		car.setMake(carDto.getMake());
		car.setYear(carDto.getYear());
		car.setModel(carDto.getModel());
		car.setCategory(carDto.getCategory());
		carService.saveNewCar(car);
		return new ResponseEntity<>("Car saved", HttpStatus.CREATED);
	}

	@Operation(summary = "Update all parameters of the selected car", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Car fully updated")
	@PutMapping
	public ResponseEntity<String> updateCarFull(@RequestBody CarCreateUpdateDto carDto) throws SQLException {
		Car car = carService.findCarByObjectId(carDto.getObjectId());
		car.setObjectId(carDto.getObjectId());
		car.setMake(carDto.getMake());
		car.setYear(carDto.getYear());
		car.setModel(carDto.getModel());
		car.setCategory(carDto.getCategory());
		carService.updateCar(car);
		return new ResponseEntity<>("Car updated", HttpStatus.OK);
	}

	@Operation(summary = "Update the selected car's year and model", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Car's year and model updated")
	@PatchMapping
	public ResponseEntity<String> updateCarPart(@RequestBody CarUpdatePartDto carDto)
			throws SQLException {
		Car car = carService.findCarByObjectId(carDto.getObjectId());
		car.setYear(carDto.getYear());
		car.setModel(carDto.getModel());
		carService.updateCar(car);
		return new ResponseEntity<>("Car updated", HttpStatus.OK);
	}

	@Operation(summary = "Delete car by objectId", security = @SecurityRequirement(name = "Auth0"))
	@ApiResponse(responseCode = "200", description = "Car deleted")
	@DeleteMapping
	public ResponseEntity<String> deleteCar(@RequestParam String objectId) {
		carService.deleteCar(objectId);
		return new ResponseEntity<>("Car updated", HttpStatus.OK);
	}

}
