package com.mohanty.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mohanty.entity.Car;
import com.mohanty.repository.CarRepository;

@Service
public class CarService {

	private static final Logger LOGGER = LoggerFactory.getLogger("CarService.class");

	@Autowired
	CarRepository repository;

	@Async
	public CompletableFuture<List<Car>> saveAll(MultipartFile file) throws Exception {
		final long START = System.currentTimeMillis();

		List<Car> cars = parseCSVFile(file);

		LOGGER.info("Saving a list of cars of size {} records", cars.size());

		cars = repository.saveAll(cars);

		LOGGER.info("Elapsed time {}", (System.currentTimeMillis() - START));
		return CompletableFuture.completedFuture(cars);
	}

	@Async
	public CompletableFuture<List<Car>> getAllCars() {

		LOGGER.info("Request to get a list of cars");
		
		List<Car> cars = repository.findAll();
		
		return CompletableFuture.completedFuture(cars);
	}

	private List<Car> parseCSVFile(MultipartFile file) throws Exception {

		final List<Car> cars = new ArrayList<Car>();

		try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(";");
				Car car = new Car();
				car.setManufacturer(data[0]);
				car.setModel(data[1]);
				car.setType(data[2]);
				cars.add(car);
			}

			return cars;

		} catch (IOException e) {
			LOGGER.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}

	}
}
