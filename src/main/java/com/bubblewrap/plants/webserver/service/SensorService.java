package com.bubblewrap.plants.webserver.service;

import java.util.List;

import org.springframework.context.annotation.Bean;

import com.bubblewrap.plants.webserver.model.Sensor;

public interface SensorService {

	@Bean
	public List<Sensor> getAllSensors();

	@Bean
	public List<Sensor> getLightSensors();

	@Bean
	public List<Sensor> getTempSensors();

}
