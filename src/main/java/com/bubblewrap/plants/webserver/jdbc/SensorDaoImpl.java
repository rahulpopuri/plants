package com.bubblewrap.plants.webserver.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;
import com.bubblewrap.plants.webserver.util.DateUtils;

@Repository
public class SensorDaoImpl implements SensorDao {

	private static Logger log = Logger.getLogger(SensorDaoImpl.class);

	@Value("${sql.retrieveSensors}")
	private String retrieveSensors;

	@Value("${sql.retrieveSensorData}")
	private String retrieveSensorData;
	
	@Value("${sql.retrieveRecentSensorData}")
	private String retrieveRecentSensorData;

	@Value("${sql.retrieveLatestSensorData}")
	private String retrieveLatestSensorData;
	
	@Value("${sql.retrieveSensorThreshold}")
	private String retrieveSensorThreshold;

	@Value("${sql.writeSensorData}")
	private String writeSensorData;
	
	@Value("${graphs.daysToShow}")
	private int daysToShow;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Override
	public List<Sensor> getAllSensors() {
		List<Sensor> result = new ArrayList<Sensor>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(retrieveSensors);
		for (Map<String, Object> row : rows) {
			Sensor sensor = new Sensor();
			sensor.setId((Integer) row.get("id"));
			sensor.setName((String) row.get("name"));
			sensor.setType(Sensors.valueOf((String) row.get("type")));
			sensor.setArduinoPin((Integer) row.get("arduino_pin"));
			sensor.setAnalog((Boolean) row.get("is_analog"));
			sensor.setData(getSensorData(sensor.getId()));
			result.add(sensor);
		}
		return result;
	}

	private List<SensorData> getSensorData(int sensorId) {
		List<SensorData> data = new ArrayList<SensorData>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(retrieveSensorData, sensorId);
		for (Map<String, Object> row : rows) {
			SensorData sd = new SensorData();
			sd.setValue((Double) row.get("value"));
			sd.setDate((Date) row.get("as_of_date"));
			data.add(sd);
		}
		return data;
	}
	
	private List<SensorData> getRecentSensorData(int sensorId){
		List<SensorData> data = new ArrayList<SensorData>();
		
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(retrieveRecentSensorData, sensorId, DateUtils.getNDaysBehind(daysToShow), new Date());
		for (Map<String, Object> row : rows) {
			SensorData sd = new SensorData();
			sd.setValue((Double) row.get("value"));
			sd.setDate((Date) row.get("as_of_date"));
			data.add(sd);
		}
		return data;
	}

	@Override
	public void writeSensorData(int sensorId, double value) {
		log.info("Inserting data: " + sensorId + " - " + value);
		jdbcTemplate.update(writeSensorData, sensorId, value);
	}

	@Override
	public List<Sensor> getAllSensorsByType(Sensors type) {
		List<Sensor> result = new ArrayList<Sensor>();
		for(Sensor s : getAllSensors()){
			if(s.getType().equals(type)){
				result.add(s);
			}
		}
		return result;
	}

	@Override
	public List<SensorData> getLightSensorData() {
		return getSensorData(1);
	}

	@Override
	public List<SensorData> getRecentLightSensorData() {
		return getRecentSensorData(1);
	}

	@Override
	public List<SensorData> getRecentTempSensorData() {
		return getRecentSensorData(2);
	}

	@Override
	public List<SensorData> getRecentMoistureSensorData(int sensorId) {
		return getRecentSensorData(sensorId);
	}
	
	@Override
	public Float getSensorThreshold(int sensorId) {
		try {
			return jdbcTemplate.queryForObject(retrieveSensorThreshold, Float.class,sensorId);
		} catch (EmptyResultDataAccessException e){
			return null;
		}
	}

}
