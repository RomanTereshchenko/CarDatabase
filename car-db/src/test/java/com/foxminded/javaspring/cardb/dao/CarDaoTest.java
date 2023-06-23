package com.foxminded.javaspring.cardb.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.javaspring.cardb.dao.CarDao;
import com.foxminded.javaspring.cardb.model.Car;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class CarDaoTest {
	
	private CarDao carDao;

	@Autowired
	public CarDaoTest(CarDao carDao) {
		this.carDao = carDao;
		
	}

	@BeforeEach
	public void cleanDB() {
		carDao.deleteAll();
		log.info("Database cleaned");
	}
	
	@Test
	@Transactional
	void testFindAllPageable() {
		Pageable pageable = PageRequest.of(0, 5);
		Car car = new Car();
		car.setObjectId("qqq");
		carDao.save(car);
		Page<Car> page = carDao.findAll(pageable);
		System.out.println(page.toString());
		List<Car> cars = page.getContent();
		Car savedCar = cars.get(0);
		assertNotNull(savedCar);
		assertEquals("qqq", savedCar.getObjectId());
	}
	
	@Test
	@Transactional
	void testFindByObjectId() {
		Car car = new Car();
		car.setObjectId("qqq");
		carDao.save(car);
		Car foundCar = carDao.findByObjectId("qqq").get();
		assertNotNull(foundCar);
		assertEquals("qqq", foundCar.getObjectId());
	}

	@Test
	@Transactional
	void testDeleteByObjectId() {
		Car car = new Car();
		String objectId = "qqq";
		car.setObjectId(objectId);
		carDao.save(car);
		Car foundCar = carDao.findByObjectId(objectId).get();
		assertNotNull(foundCar);
		assertEquals("qqq", foundCar.getObjectId());
		carDao.deleteByObjectId(objectId);
		Optional<Car> deletedCar = carDao.findByObjectId(objectId);
		assertEquals((Object)deletedCar, Optional.empty());
	}
	
	@Test
	@Transactional
	void testfindCars() {
		Car car = new Car (0L, "ooo", "mmm", 2022, "mmm", "ccc");
		carDao.save(car);
		Car foundCar = carDao.findCars("mmm", "mmm", 2021, 2023, "ccc").get(0);
		assertNotNull(foundCar);
		assertEquals("ooo", foundCar.getObjectId());
	}
}
