package com.foxminded.javaspring.cardb.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.javaspring.cardb.model.Car;
import com.foxminded.javaspring.cardb.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {

	private CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@GetMapping("/all")
	public List<Car> findAllCars() {
		return carService.findAllCars();
	}
	
	@GetMapping(value = "/paginated", params = {"page", "size"})
	public List<Car> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) throws SQLException {
		Page<Car> resultPage = carService.findPaginated(page, size, Sort.by("make").descending().and(Sort.by("model").descending()));
		return resultPage.getContent();
	}
	
	@GetMapping("/byObjectId")
	public Car findCarByObjectId(@RequestParam("objectId") String objectId) throws SQLException {
		return carService.findCarByObjectId(objectId);
	}
	
	@PostMapping("/create")
	public Car createCar(@RequestParam Map<String, String> carParams) throws SQLException {
		Car car = new Car();
		car.setObjectId(carParams.get("objectId"));
		car.setMake(carParams.get("make"));
		car.setYear(Integer.parseInt(carParams.get("year")));
		car.setModel(carParams.get("model"));
		car.setCategory(carParams.get("category"));
		carService.saveNewCar(car);
		return car;
	}
	
	@PostMapping("/update")
	public Car updateCar(@RequestParam Map<String, String> carParams) throws SQLException {
		Car car = carService.findCarByObjectId(carParams.get("objectId"));
		Car updatedCar = new Car();
		updatedCar.setId(car.getId());
		updatedCar.setObjectId(carParams.get("objectId"));
		updatedCar.setMake(carParams.get("make"));
		updatedCar.setYear(Integer.parseInt(carParams.get("year")));
		updatedCar.setMake(carParams.get("make"));
		updatedCar.setCategory(carParams.get("category"));
		carService.updateCar(updatedCar);
		return updatedCar;
	}
	
	@PostMapping("/delete")
	public void deleteCar(@RequestParam String objectId) throws SQLException {
		carService.deleteCar(objectId);
	}

}
