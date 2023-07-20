package com.foxminded.javaspring.cardb.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.foxminded.javaspring.cardb.dao.CarDao;
import com.foxminded.javaspring.cardb.model.Car;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CarService {

	private CarDao carDao;

	@Autowired
	public CarService(CarDao carDao) {
		this.carDao = carDao;
	}

	public List<Car> findAllCars() {
		log.info("All cars found");
		return carDao.findAll();
	}

	public Page<Car> findAllCars(int page, int size, Sort sort) throws SQLException {
		Pageable pageable = PageRequest.of(page, size, sort);
		log.info("Page " + page + " is found");
		return carDao.findAll(pageable);
	}

	public Car findCarByObjectId(String objectId) throws SQLException {
		Optional<Car> car = carDao.findByObjectId(objectId);
		if (car.isPresent()) {
			log.info("Car found");
			return car.get();
		}
		log.info("Car not found");
		return null;
	}

	public List<Car> findCars(String make, String model, Integer minYear, Integer maxYear, String category) {
		List<Car> cars = carDao.findCars(make, model, minYear, maxYear, category);
		log.info("Cars found");
		return cars;
	}

	public Car saveNewCar(Car car) throws SQLException {
		Car savedNewCar = carDao.save(car);
		log.info("New car saved");
		return savedNewCar;
	}

	public Car updateCar(Car car) throws SQLException {
		Optional<Car> updatingCar = carDao.findByObjectId(car.getObjectId());
		if (updatingCar.isPresent()) {
			carDao.save(car);
			log.info("Car is updated");
			return car;
		}
		log.info("This car does not exist in the database");
		return null;
	}

	public void deleteCar(String objectId) {
		log.info("Car with ObjectId " + objectId + " deleted");
		carDao.deleteByObjectId(objectId);
	}
	
}
