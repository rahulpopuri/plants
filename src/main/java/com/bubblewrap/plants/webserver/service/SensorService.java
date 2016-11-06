package com.bubblewrap.plants.webserver.service;

import java.util.List;

import org.springframework.context.annotation.Bean;

import com.bubblewrap.plants.webserver.model.JsonSensorData;
import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;

public interface SensorService {

	@Bean
	public List<Sensor> getAllSensors();
	
	@Bean
	public List<Sensor> getAllSensorsByType(Sensors type);

	@Bean
	public List<Sensor> getLightSensors();

	@Bean
	public List<Sensor> getTempSensors();

	@Bean
	public List<SensorData> getLightSensorData();
	
	@Bean
	public List<SensorData> getRecentLightSensorData();

	@Bean
	public List<SensorData> getRecentTempSensorData();

	@Bean
	public List<SensorData> getRecentMoistureSensorData(int sensorId);

	@Bean
	public List<Sensor> getCurrentSensorValues();

	@Bean
	public void writeSensorData(JsonSensorData data);

}
