package com.mohanty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mohanty.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {

		
}
