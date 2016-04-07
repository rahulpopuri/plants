package com.bubblewrap.plants.webserver.model;

import java.util.Date;

public class SensorData {

	private double value;
	private Date date;

	public SensorData(){
		
	}
	
	public SensorData(double value, Date date) {
		this.value = value;
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SensorData [value=" + value + ", date=" + date + "]";
	}

}
