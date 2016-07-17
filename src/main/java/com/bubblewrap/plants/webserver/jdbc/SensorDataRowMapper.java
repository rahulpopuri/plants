package com.bubblewrap.plants.webserver.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bubblewrap.plants.webserver.model.SensorData;

public class SensorDataRowMapper implements RowMapper<SensorData> {

	@Override
	public SensorData mapRow(ResultSet rs, int arg1) throws SQLException {
		SensorData sd = new SensorData();
		sd.setValue(rs.getDouble("value"));
		sd.setDate(rs.getDate("as_of_date"));
		return sd;
	}

}
