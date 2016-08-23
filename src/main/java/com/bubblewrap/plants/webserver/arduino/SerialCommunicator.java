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

import com.bubblewrap.notifications.NotificationManager;
import com.bubblewrap.notifications.message.Message;
import com.bubblewrap.notifications.message.text.TextMessage;
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
	
	@Value("${notification.text}")
	private String notificationText;
	
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
		checkSensorThresholds();
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
