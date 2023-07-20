package com.foxminded.javaspring.cardb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class CarDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarDbApplication.class, args);
	}

}
