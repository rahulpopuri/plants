package com.bubblewrap.plants.webserver.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bubblewrap.notifications.NotificationManager;
import com.bubblewrap.notifications.message.Message;
import com.bubblewrap.notifications.message.text.TextMessage;
import com.bubblewrap.plants.webserver.jdbc.SensorDao;
import com.bubblewrap.plants.webserver.model.JsonSensorData;
import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;

@Service
public class SensorServiceImpl implements SensorService {

	private List<Sensor> sensors;
	
	@Value("${notification.text}")
	private String notificationText;
	
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

	@Override
	public List<SensorData> getRecentMoistureSensorData(int sensorId) {
		return sensorDao.getRecentMoistureSensorData(sensorId);
	}

	@Override
	public List<Sensor> getCurrentSensorValues() {
		return sensorDao.getCurrentSensorValues();
	}

	@Override
	public void writeSensorData(JsonSensorData data) {
		sensorDao.writeSensorData(data.getId(), Double.valueOf(data.getValue()));
		updateSensors();
		checkSensorThresholds();
	}
	
	private void updateSensors(){
		this.sensors = sensorDao.getAllSensors();
	}
	
	private void checkSensorThresholds() {
		for(Sensor s : this.sensors){
			Float threshold = sensorDao.getSensorThreshold(s.getId());
			Date last_notified = sensorDao.getLastNotified(s.getId());
			if(threshold != null){
				double currentValue = s.getMostRecentData().getValue();
				
				if( currentValue >= threshold && toNotify(last_notified)){
					Message text = new TextMessage(notificationText,s.getName() + " needs to be watered. Current moisture level = " + currentValue + ", threshold = " + threshold);
					NotificationManager.getInstance().addMessage(text);
					this.sensorDao.markNotified(s.getId());
				}
			}
		}
	}
	
	/**
	 * Checks if a notification has been sent for the sensor in the past 24 hours
	 * @param lastNotified
	 * @return true if notification needs to be sent out, false otherwise
	 */
	private boolean toNotify(Date lastNotified) {
		long timeDiff = new Date().getTime() - lastNotified.getTime();
		return (timeDiff/1000) > 86400;
	}

}
