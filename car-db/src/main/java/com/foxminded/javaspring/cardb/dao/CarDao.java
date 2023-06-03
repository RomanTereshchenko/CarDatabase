package com.foxminded.javaspring.cardb.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foxminded.javaspring.cardb.model.Car;

@Repository
public interface CarDao extends JpaRepository<Car, Long> {
	
	Page<Car> findAll (Pageable pageable);
	
	Optional<Car> findByObjectId (String objectId);
	
	Optional<Car> deleteByObjectId (String objectId);

}
