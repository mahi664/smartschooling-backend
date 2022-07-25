package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
		String query = "SELECT * FROM routes WHERE route_id = ?";
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
	
	/**
	 * 
	 * @param routeIDs
	 * @return
	 * @throws StudentException
	 */
	public List<Routes> getRoutes(List<String>routeIDs) throws StudentException{
		log.info("Fetching route details for route id {}", routeIDs.toString());
		String query = "SELECT * FROM routes WHERE route_id in ("+getInClauseForRouteDetails(routeIDs)+")";
		log.info("query {}", query);
		List<Routes> routes;
		try {
			routes = jdbcTemplate.query(query, ps->setInClaue(ps, routeIDs), (rs, rowNum) -> getRouteDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error while retreiving route details for route id {}", routeIDs.toString());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return routes;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Routes getRouteDetailsMapper(ResultSet rs) throws SQLException {
		log.info("mapping result set to routes details entity");
		return Routes.builder().destination(rs.getString("DESTINATION")).distance(rs.getDouble("DISTANCE"))
				.routeId(rs.getString("ROUTE_ID")).source(rs.getString("SOURCE")).build();
	}

	/**
	 * 
	 * @param ps
	 * @param routeIDs
	 * @throws SQLException
	 */
	private void setInClaue(PreparedStatement ps, List<String> routeIDs) throws SQLException {
		log.info("Setting in cluase for get route details");
		for (int i = 0; i < routeIDs.size(); i++) {
			ps.setString(i + 1, routeIDs.get(i));
		}
	}

	/**
	 * 
	 * @param routeIDs
	 * @return
	 */
	private String getInClauseForRouteDetails(List<String> routeIDs) {
		String inClause = "";
		for (int i = 0; i < routeIDs.size() - 1; i++) {
			inClause += "?,";
		}
		inClause += "?";
		return inClause;
	}
}
