package com.example.demo.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class UtilService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Map<String, String> getRefTableTypes() {
		String query = "SELECT * FROM REF_TABLE_TYPES";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, String>>() {

			@Override
			public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, String> refTableTypesMap = new HashMap<>();
				while(rs.next()) {
					refTableTypesMap.put(rs.getString("REF_TABLE_NAME"), rs.getString("REF_TABLE_TYPE"));
				}
				return refTableTypesMap;
			}
		});
	}
}
