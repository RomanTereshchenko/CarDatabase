package com.foxminded.javaspring.cardb.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foxminded.javaspring.cardb.model.Car;

@Repository
public interface CarDao extends JpaRepository<Car, Long> {
	
	Page<Car> findAll (Pageable pageable);
	
	Optional<Car> findByObjectId (String objectId);
	
	Optional<Car> deleteByObjectId (String objectId);

	@Query("SELECT c FROM Car c WHERE c.make=?1 AND c.model=?2 AND c.year>=?3 AND c.year<=?4 AND c.category=?5")
	List<Car> findCars(String make, String model, Integer minYear, Integer maxYear, String category);

}
