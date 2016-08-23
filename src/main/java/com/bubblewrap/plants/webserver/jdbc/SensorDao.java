package com.bubblewrap.plants.webserver.jdbc;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;

import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;

public interface SensorDao {

	@Bean
	public List<Sensor> getAllSensors();

	@Bean
	void writeSensorData(int sensorId, double value);

	@Bean
	public List<Sensor> getAllSensorsByType(Sensors type);

	@Bean
	public List<SensorData> getLightSensorData();

	@Bean
	public List<SensorData> getRecentLightSensorData();
	
	@Bean
	public List<SensorData> getRecentTempSensorData();

	@Bean
	public List<SensorData> getRecentMoistureSensorData(int sensorId);
	
	@Bean
	public Float getSensorThreshold(int sensorId);

	@Bean
	public List<Sensor> getCurrentSensorValues();

	@Bean
	public void markNotified(int id);

	@Bean
	public Date getLastNotified(int id);

}
