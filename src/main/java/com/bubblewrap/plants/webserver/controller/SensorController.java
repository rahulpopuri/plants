package com.bubblewrap.plants.webserver.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.service.SensorService;

@RestController
@RequestMapping("/rest/sensors")

public class SensorController {
	
	@Inject
	private SensorService service;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> getSensors(){
		return new ResponseEntity<List<Sensor>>(service.getAllSensors(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> getCurrentSensors(){
		return new ResponseEntity<List<Sensor>>(service.getCurrentSensorValues(),HttpStatus.OK);
	}

	@RequestMapping(value = "/temp", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> getTemp(){
		return new ResponseEntity<List<Sensor>>(service.getTempSensors(),HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/light", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> getLight(){
		return new ResponseEntity<List<Sensor>>(service.getLightSensors(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/light/data" , produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<List<SensorData>> getLightData(){
		return new ResponseEntity<List<SensorData>>(service.getLightSensorData(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/light/recentData" , produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<List<SensorData>> getRecentLightData(){
		return new ResponseEntity<List<SensorData>>(service.getRecentLightSensorData(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/temp/recentData" , produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<List<SensorData>> getRecentTempData(){
		return new ResponseEntity<List<SensorData>>(service.getRecentTempSensorData(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/moisture/recentData/{sensor_id}" , produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<List<SensorData>> getRecentMoistureData(@PathVariable("sensor_id") int sensorId){
		return new ResponseEntity<List<SensorData>>(service.getRecentMoistureSensorData(sensorId),HttpStatus.OK);
	}
	

}
