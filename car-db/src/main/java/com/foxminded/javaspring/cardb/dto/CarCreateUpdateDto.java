package com.foxminded.javaspring.cardb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCreateUpdateDto {
	
	private String objectId;
	private String make;
	private Integer year;
	private String model;
	private String category;

}
