package com.bubblewrap.plants.webserver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Sensor {

	private int id;
	private Sensors type;
	private String name;
	private Date lastUpdated;
	private int arduinoPin;
	private boolean isAnalog;
	private List<SensorData> data;

	public Sensor() {
		this.data = new ArrayList<SensorData>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Sensors getType() {
		return type;
	}

	public void setType(Sensors type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public int getArduinoPin() {
		return arduinoPin;
	}

	public void setArduinoPin(int arduinoPin) {
		this.arduinoPin = arduinoPin;
	}

	public boolean isAnalog() {
		return isAnalog;
	}

	public void setAnalog(boolean isAnalog) {
		this.isAnalog = isAnalog;
	}

	public void addData(SensorData data) {
		this.data.add(data);
	}

	public List<SensorData> getData() {
		return data;
	}

	public void setData(List<SensorData> data) {
		this.data = data;
	}
	
	public SensorData getMostRecentData() {
		Collections.sort(this.data);
		return this.data.get(this.data.size()-1);
	}

	@Override
	public String toString() {
		return "Sensor [id=" + id + ", type=" + type + ", name=" + name + ", lastUpdated=" + lastUpdated
				+ ", arduinoPin=" + arduinoPin + ", isAnalog=" + isAnalog + ", data=" + data + "]";
	}

}
