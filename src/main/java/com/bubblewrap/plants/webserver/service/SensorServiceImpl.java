package com.bubblewrap.plants.webserver.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.bubblewrap.plants.webserver.jdbc.SensorDao;
import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;

@Service
public class SensorServiceImpl implements SensorService {

	@Inject
	private SensorDao sensorDao;

	@Override
	public List<Sensor> getAllSensors() {
		return sensorDao.getAllSensors();
	}

	@Override
	public List<Sensor> getLightSensors() {
		return sensorDao.getAllSensorsByType(Sensors.LIGHT);
	}

	@Override
	public List<Sensor> getTempSensors() {
		return sensorDao.getAllSensorsByType(Sensors.TEMPERATURE);
	}

	@Override
	public List<SensorData> getLightSensorData() {
		return sensorDao.getLightSensorData();
	}

	@Override
	public List<Sensor> getAllSensorsByType(Sensors type) {
		return sensorDao.getAllSensorsByType(type);
	}

	@Override
	public List<SensorData> getRecentLightSensorData() {
		return sensorDao.getRecentLightSensorData();
	}

	@Override
	public List<SensorData> getRecentTempSensorData() {
		return sensorDao.getRecentTempSensorData();
	}

}
