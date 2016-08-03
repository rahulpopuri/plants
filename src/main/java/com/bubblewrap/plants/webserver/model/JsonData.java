package com.bubblewrap.plants.webserver.model;

public class JsonData {

	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonData [data=" + data + "]";
	}

}
