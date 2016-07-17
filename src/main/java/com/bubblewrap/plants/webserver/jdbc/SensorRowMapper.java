package com.bubblewrap.plants.webserver.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bubblewrap.plants.webserver.model.Sensor;
import com.bubblewrap.plants.webserver.model.Sensors;

public class SensorRowMapper  implements RowMapper<Sensor> {

	@Override
	public Sensor mapRow(ResultSet rs, int arg1) throws SQLException {
		Sensor sensor = new Sensor();
		sensor.setId(rs.getInt("id"));
		sensor.setName(rs.getString("name"));
		sensor.setType(Sensors.valueOf(rs.getString("type")));
		sensor.setArduinoPin(rs.getInt("arduino_pin"));
		sensor.setAnalog(rs.getBoolean("is_analog"));
		return sensor;
	}

}
