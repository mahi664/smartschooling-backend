package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.Routes;
import com.example.demo.exception.StudentException;

@Repository
public class RoutesRepository {

	private final Logger log = LoggerFactory.getLogger(GeneralRegisterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public Routes getRoute(String routeId) throws StudentException {
		log.info("Fetching route details for route id {}", routeId);
		String query = "SELECT * FROM ROUTES WHERE ROUTE_ID = ?";
		log.info("query {}", query);
		Routes route = null;
		try {
			route = jdbcTemplate.query(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, routeId);
				}
			}, new ResultSetExtractor<Routes>() {

				@Override
				public Routes extractData(ResultSet rs) throws SQLException, DataAccessException {
					Routes route = null;
					while (rs.next()) {
						route = Routes.builder().destination(rs.getString("DESTINATION"))
								.distance(rs.getDouble("DISTANCE")).routeId(rs.getString("ROUTE_ID"))
								.source(rs.getString("SOURCE")).build();
					}
					return route;
				}
			});
		} catch (Exception ex) {
			log.error("Error while retreiving route details for route id {}", routeId);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return route;
	}
}
