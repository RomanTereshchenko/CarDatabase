package com.foxminded.javaspring.cardb.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.foxminded.javaspring.cardb.dao.CarDao;
import com.foxminded.javaspring.cardb.model.Car;

import jakarta.transaction.Transactional;
import lombok.var;
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

	public Page<Car> findPaginated(int page, int size, Sort sort) throws SQLException {
		Pageable pageable = PageRequest.of(page, size, sort);
		log.info("Page " + page + " is found" );
		return carDao.findAll(pageable);
	}
	
	public Car findCarByObjectId(String objectId) throws SQLException {
		var car = carDao.findByObjectId(objectId);
		log.info("Car found");
		return car.get();
	}
	
	public Car saveNewCar(Car car) throws SQLException {
		var savedNewCar = carDao.save(car);
		log.info("New car saved");
		return savedNewCar;
	}
	
	public Car updateCar(Car car) throws SQLException {
		var updatingCar = carDao.findByObjectId(car.getObjectId());
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
