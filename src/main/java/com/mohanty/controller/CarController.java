package com.mohanty.controller;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mohanty.service.CarService;

@RestController
@RequestMapping(value = "/api/car")
public class CarController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);

	@Autowired
	CarService carservice;

	private Function<Throwable, ? extends ResponseEntity> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

	public @ResponseBody ResponseEntity uploadFile(@RequestParam(value = "files") MultipartFile[] files) {

		try {
			for (MultipartFile file : files)
				carservice.saveAll(file);

			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception exp) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	
	
	 @RequestMapping (method = RequestMethod.GET, consumes={MediaType.APPLICATION_JSON_VALUE},
	            produces={MediaType.APPLICATION_JSON_VALUE})
	 
	public @ResponseBody CompletableFuture<ResponseEntity> getAllCars() {
		 return carservice.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok)
	                .exceptionally(handleGetCarFailure );
	}

	 
}
