package com.foxminded.javaspring.cardb.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.foxminded.javaspring.cardb.dao.CarDao;
import com.foxminded.javaspring.cardb.model.Car;
import com.foxminded.javaspring.cardb.service.CarService;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
	
	@Mock
	private CarDao carDao;
	
	@InjectMocks
	private CarService carService;
	
	@Test
	void testFindAllCars() throws SQLException {
		List<Car> cars = new ArrayList<>();
		cars.add(new Car (0L, "test", "mmm", 2023, "mmm", "ccc"));
		Mockito.when(carDao.findAll()).thenReturn(cars);
		List<Car> foundCars = carService.findAllCars();
		assertNotNull(foundCars);
		assertEquals("test", foundCars.get(0).getObjectId());
	}
	
	@Test
	void testFindPaginated() throws SQLException {
		List<Car> cars = new ArrayList<>();
		Car car = new Car (0L, "test", "mmm", 2023, "mmm", "ccc");
		cars.add(car);
		Page<Car> page = new PageImpl<>(cars);
		Mockito.when(carDao.findAll(any(Pageable.class))).thenReturn(page);
		Page <Car> foundPage = carService.findPaginated(0, 5, Sort.by("make"));
		assertNotNull(foundPage);
		assertEquals("test", foundPage.getContent().get(0).getObjectId());
	}
	
	@Test
	void testFindCarByObjectId() throws SQLException {
		Car car = new Car (0L, "test", "mmm", 2023, "mmm", "ccc");
		carDao.save(car);
		Mockito.when(carDao.findByObjectId(anyString())).thenReturn(Optional.of(car));
		Car foundCar = carService.findCarByObjectId("test");
		assertNotNull(foundCar);
		assertEquals("test", foundCar.getObjectId());
	}
	
	@Test
	void testCreateNewCar() throws SQLException {
		Car car = new Car (0L, "test", "mmm", 2023, "mmm", "ccc");
		Mockito.when(carDao.save(any(Car.class))).thenReturn(car);
		Car savedCar = carService.createNewCar(car);
		assertNotNull(savedCar);
		assertEquals("test", savedCar.getObjectId());	
	}
	
	@Test
	void testUpdateCar() throws SQLException {
		Car dbCar = new Car (0L, "dbCar", "mmm", 2023, "mmm", "ccc");
		Car updatingCar = new Car (0L, "updatedCar", "mmm", 2023, "mmm", "ccc");
		Mockito.when(carDao.findByObjectId(anyString())).thenReturn(Optional.of(dbCar));
		Car updetedCar = carService.updateCar(updatingCar);
		assertNotNull(updetedCar);
		assertEquals("updatedCar", updetedCar.getObjectId());	
	}

	@Test
	void testDeleteCar() throws SQLException {
		carService.deleteCar(anyString());
		Mockito.verify(carDao).deleteByObjectId(anyString());
	}
}
