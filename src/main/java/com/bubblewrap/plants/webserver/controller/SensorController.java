package com.bubblewrap.plants.webserver.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.service.SensorService;

@RestController
@RequestMapping("/rest/sensors")

public class SensorController {
	
	@Inject
	private SensorService service;

	@RequestMapping("/all")
	public ResponseEntity<List<Sensor>> getSensors(){
		return new ResponseEntity<List<Sensor>>(service.getAllSensors(),HttpStatus.OK);
	}

	@RequestMapping("/temp")
	public ResponseEntity<List<Sensor>> getTemp(){
		return new ResponseEntity<List<Sensor>>(service.getTempSensors(),HttpStatus.OK);
	}	
	
	@RequestMapping("/light")
	public ResponseEntity<List<Sensor>> getLight(){
		return new ResponseEntity<List<Sensor>>(service.getLightSensors(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/light/data" , produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<SensorData>> getLightData(){
		return new ResponseEntity<List<SensorData>>(service.getLightSensorData(),HttpStatus.OK);
	}
	

}
