package com.foxminded.javaspring.cardb.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.foxminded.javaspring.cardb.model.Car;
import com.foxminded.javaspring.cardb.security.MethodSecurityConfig;
import com.foxminded.javaspring.cardb.security.SecSecurityConfig;
import com.foxminded.javaspring.cardb.service.CarService;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ CarController.class, MethodSecurityConfig.class, SecSecurityConfig.class })
class CarControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CarService carService;

	@Test
	@WithMockUser(username = "test", roles = { "MANAGER" })
	void whenFindCars_thenStatus200() throws Exception {
		List<Car> cars = new ArrayList<>();
		Mockito.when(carService.findAllCars()).thenReturn(cars);
		Page<Car> page = new PageImpl<>(cars);
		Mockito.when(carService.findAllCars(anyInt(), anyInt(), any(Sort.class))).thenReturn(page);
		mockMvc.perform(get("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).param("page", "1")
				.param("size", "5")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles = "MANAGER")
	void whenFindCarByObjectId_thenStatus200() throws Exception {
		Car car = new Car();
		Mockito.when(carService.findCarByObjectId(anyString())).thenReturn(car);
		mockMvc.perform((get("/api/v1/cars/{objectId}", "qqq")).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles = "MANAGER")
	void whenfindCarsByYears_thenStatus200() throws Exception {
		List<Car> cars = new ArrayList<>();
		Mockito.when(carService.findCars(anyString(), anyString(), any(Integer.class), any(Integer.class), anyString()))
				.thenReturn(cars);
		mockMvc.perform((get("/api/v1/cars/{make}/{model}/{category}", "toyota", "corolla", "SUV")
				.param("minYear", "2000").param("maxYear", "2023"))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles = { "MANAGER" })
	void whenCreateCar_thenStatus200() throws Exception {
		mockMvc.perform(post("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).
				content("{\"objectId\":\"aaa\",\"make\":\"qqq\",\"year\":\"2024\",\"model\":\"zzz\",\"category\":\"xxx\"}"))
				.andDo(print()).andExpect(status().isCreated());
	}
	
	@Test
	@WithMockUser(username = "test", roles = { "MANAGER" })
	void whenUpdateCarFull_thenStatus200() throws Exception {
		Car car = new Car();
		Mockito.when(carService.findCarByObjectId(anyString())).thenReturn(car);
		mockMvc.perform(put("/api/v1/cars").contentType(MediaType.APPLICATION_JSON)
				.content("{\"objectId\":\"aaa\",\"make\":\"qqq\",\"year\":\"2024\",\"model\":\"zzz\",\"category\":\"xxx\"}"))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles = { "MANAGER" })
	void whenUpdateCarPart_thenStatus200() throws Exception {
		Car car = new Car();
		Mockito.when(carService.findCarByObjectId(anyString())).thenReturn(car);
		mockMvc.perform(patch("/api/v1/cars").contentType(MediaType.APPLICATION_JSON)
				.content("{\"objectId\":\"aaa\", \"year\":\"2024\",\"model\":\"zzz\"}"))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "test", roles = { "MANAGER" })
	void whenDeleteCar_thenStatus200() throws Exception {
		Car car = new Car();
		car.setObjectId("qqq");
		car.setMake("aaa");
		car.setYear(2023);
		car.setModel("www");
		car.setCategory("ppp");
		carService.saveNewCar(car);
		mockMvc.perform(delete("/api/v1/cars").contentType(MediaType.APPLICATION_JSON).param("objectId", "qqq"))
				.andDo(print()).andExpect(status().isOk());
	}

}
