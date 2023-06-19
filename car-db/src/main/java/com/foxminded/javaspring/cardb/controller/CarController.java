package com.foxminded.javaspring.cardb.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.javaspring.cardb.model.Car;
import com.foxminded.javaspring.cardb.service.CarService;

@RestController
@RequestMapping("/api/v1/car-db")
public class CarController {

	private CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@GetMapping("/cars")
	public List<Car> findCars(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) throws SQLException {
		if (page == null && size == null) {
			return carService.findAllCars();
		}
		Page<Car> resultPage = carService.findAllCars(page, size,
				Sort.by("make").descending().and(Sort.by("model").descending()));
		return resultPage.getContent();
	}

	@GetMapping("/cars/{objectId}")
	public Car findCarByObjectId(@PathVariable("objectId") String objectId) throws SQLException {
		return carService.findCarByObjectId(objectId);
	}

	@GetMapping("/cars/{make}/{model}/{category}")
	public List<Car> findCarsByYears(@PathVariable("make") String make, @PathVariable("model") String model,
			@PathVariable("category") String category, @RequestParam("minYear") Integer minYear,
			@RequestParam("maxYear") Integer maxYear) {
		return carService.findCars(make, model, minYear, maxYear, category);
	}

	@PostMapping("/cars")
	public ResponseEntity<String> createCar(@RequestParam Map<String, String> carParams) throws SQLException {
		Car car = new Car();
		car.setObjectId(carParams.get("objectId"));
		car.setMake(carParams.get("make"));
		car.setYear(Integer.parseInt(carParams.get("year")));
		car.setModel(carParams.get("model"));
		car.setCategory(carParams.get("category"));
		carService.saveNewCar(car);
		return new ResponseEntity<>("Car saved", HttpStatus.CREATED);
	}

	@PutMapping("/cars")
	public ResponseEntity<String> updateCar(@RequestParam Map<String, String> carParams) throws SQLException {
		Car car = carService.findCarByObjectId(carParams.get("objectId"));
		car.setObjectId(carParams.get("objectId"));
		car.setMake(carParams.get("make"));
		car.setYear(Integer.parseInt(carParams.get("year")));
		car.setMake(carParams.get("make"));
		car.setCategory(carParams.get("category"));
		carService.updateCar(car);
		return new ResponseEntity<>("Car updated", HttpStatus.OK);
	}

	@PatchMapping("/cars")
	public ResponseEntity<String> updateCarCategory(@RequestParam String objectId, @RequestParam String category)
			throws SQLException {
		Car car = carService.findCarByObjectId(objectId);
		car.setCategory(category);
		carService.updateCar(car);
		return new ResponseEntity<>("Car category updated", HttpStatus.OK);
	}

	@DeleteMapping("/cars")
	public ResponseEntity<String> deleteCar(@RequestParam String objectId) {
		carService.deleteCar(objectId);
		return new ResponseEntity<>("Car updated", HttpStatus.OK);
	}

}
