package com.foxminded.javaspring.cardb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarUpdatePartDto {
	
	private String objectId;
	private Integer year;
	private String model;

}
