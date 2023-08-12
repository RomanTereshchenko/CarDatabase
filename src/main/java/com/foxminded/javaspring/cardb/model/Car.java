package com.foxminded.javaspring.cardb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars", schema = "car_db")
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "object_id")
	private String objectId;
	
	@Column(name = "make")
	private String make;
	
	@Column(name = "year")
	private Integer year;
	
	@Column(name = "model")
	private String model;
	
	@Column(name = "category")
	private String category;
}
