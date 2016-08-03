package com.bubblewrap.plants.webserver.arduino;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bubblewrap.plants.webserver.jdbc.SensorDao;
import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.service.PushBulletService;

import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

@Service
public class SerialCommunicator implements SerialPortEventListener {

	private static Logger log = Logger.getLogger(SerialCommunicator.class);
	
	@Value("${serial.port}")
	private String portName;

	private NRSerialPort serialPort;

	private InputStream input;
	private OutputStream output;

	private List<Sensor> sensors;
	
	@Autowired
	private SensorDao sensorDao;
	
	@Inject
	private PushBulletService pushBulletService;

	@PostConstruct
	private void init() {
		updateSensors();
		connect();
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (SerialPortEvent.DATA_AVAILABLE == event.getEventType()) {
			try {
				String data = null;
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(input));
				while (reader.ready() && (data = reader.readLine()) != null) {
					if (!data.isEmpty()) {
						// Parse Data and update the appropriate sensor
						int sensorId = Integer.valueOf(data.split(":")[0]);
						double sensorValue = Double.valueOf(data.split(":")[1]);
						
						for(Sensor s : sensors){
							if(s.getId() == sensorId){
								SensorData sd = new SensorData();
								sd.setValue(sensorValue);
								sd.setDate(new Timestamp(new Date().getTime()));
								s.addData(sd);
								s.setLastUpdated(new Date());
								sensorDao.writeSensorData(sensorId, sensorValue);
								updateSensors();
							}
						}
						checkSensorThresholds();
					}
				}
			} catch (Exception e) {
				log.error("Failed to read data: " + e.toString());
			}
		}
	}
	
	public void updateSensors(){
		this.sensors = sensorDao.getAllSensors();
	}
	
	private void checkSensorThresholds() {
		for(Sensor s : this.sensors){
			Float threshold = sensorDao.getSensorThreshold(s.getId()); 
			if(threshold != null){
				if(s.getData().get(s.getData().size()-1).getValue() >= threshold){
					// Time to send an email
					// Pushbullet code goes here
				}
			}
		}
	}
	
	private void connect() {
		for (String s : NRSerialPort.getAvailableSerialPorts()) {
			if (s.equals(portName)) {
				log.info("Found serial port: " + s);
			}
		}
		serialPort = new NRSerialPort(portName, 9600);
		serialPort.connect();

		input = new DataInputStream(serialPort.getInputStream());
		output = new DataOutputStream(serialPort.getOutputStream());
		initListener();
		log.info("Serial port is connected");
	}

	private void initListener() {
		try {
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (TooManyListenersException e) {
			log.error("Too many listeners: " + e.toString());
		}
	}

}
