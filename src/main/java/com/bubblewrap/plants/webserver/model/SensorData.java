package com.bubblewrap.plants.webserver.model;

import java.sql.Timestamp;
import java.util.Date;

public class SensorData {

	private double value;
	private Timestamp date;

	public SensorData() {

	}

	public SensorData(double value, Date date) {
		this.value = value;
		this.date = new Timestamp(date.getTime());
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SensorData [value=" + value + ", date=" + date + "]";
	}

}
