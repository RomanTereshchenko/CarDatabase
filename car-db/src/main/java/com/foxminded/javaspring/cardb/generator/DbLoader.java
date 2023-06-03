package com.foxminded.javaspring.cardb.generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxminded.javaspring.cardb.dao.CarDao;
import com.foxminded.javaspring.cardb.model.Car;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DbLoader {

	private CarDao carDao;

	@Autowired
	public DbLoader(CarDao carDao) {
		this.carDao = carDao;
	}

	public void loadDataToDb() throws IOException {
		carDao.deleteAll();
		List<Car> cars = readDataAsObjects();
		for (Car car : cars) {
			carDao.save(car);
		}
		log.info("Cars loaded");
	}

	private List<Car> readDataAsObjects() throws IOException {
		List<List<String>> records = readDataFromFile();
		List<Car> cars = new ArrayList<>();
		for (List<String> carRecord : records) {
			Car car = new Car();
			car.setObjectId(carRecord.get(0));
			car.setMake(carRecord.get(1));
			car.setYear(Integer.parseInt(carRecord.get(2)));
			car.setModel(carRecord.get(3));
			car.setCategory(carRecord.get(4));
			cars.add(car);
		}
		log.info("Records converted to Car objects");
		return cars;
	}

	private List<List<String>> readDataFromFile() throws IOException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/file.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				records.add(Arrays.asList(values));
			}
			log.info("Data loaded from the file");
			return records;
		}
	}

}
