package com.bubblewrap.plants.webserver.arduino;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bubblewrap.plants.webserver.jdbc.SensorDao;
import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.SensorData;
import com.bubblewrap.plants.webserver.model.Sensors;

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
								sd.setDate(new Date());
								s.addData(sd);
								s.setLastUpdated(new Date());
								sensorDao.writeSensorData(sensorId, sensorValue);
								updateSensors();
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("Failed to read data: " + e.toString());
			}
		}
	}
	
	public List<Sensor> getAllSensorsByType(Sensors type){
		List<Sensor> result = new ArrayList<Sensor>();
		for(Sensor s : sensors){
			if(s.getType().equals(type)){
				result.add(s);
			}
		}
		return result;
	}
	
	public void updateSensors(){
		this.sensors = sensorDao.getAllSensors();
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
