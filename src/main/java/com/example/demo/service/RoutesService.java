package com.example.demo.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.RouteDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class RoutesService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public List<RouteDetailsBO> addNewRoutes(List<RouteDetailsBO> routeDetailsBOs) {
		// TODO Auto-generated method stub
		try {
//			int nextRouteId = getMaxRouteId() + 1;
			String query = "INSERT INTO ROUTES VALUES(?,?,?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
//				int rid = nextRouteId;
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					routeDetailsBOs.get(i).setRouteId(routeDetailsBOs.get(i).getSource()+"-"+routeDetailsBOs.get(i).getDestination());
					ps.setString(1, routeDetailsBOs.get(i).getRouteId());
					ps.setString(2, routeDetailsBOs.get(i).getSource());
					ps.setString(3, routeDetailsBOs.get(i).getDestination());
					ps.setDouble(4, routeDetailsBOs.get(i).getDistance());
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setString(6, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return routeDetailsBOs.size();
				}
			});
			if(res.length<=0) {
				System.out.println("Problem in adding routes details");
				return null;
			}
			return routeDetailsBOs;
		} catch (Exception e) {
			System.out.println("Error in adding routes details");
			throw e;
		}
	}

	private int getMaxRouteId() {
		String query = "SELECT MAX(ROUTE_ID) as MAX_ROUTE_ID FROM ROUTES";
		int maxRouteId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				int maxRouteId = 0;
				while(rs.next()) {
					maxRouteId = rs.getInt("MAX_ROUTE_ID");
				}
				return maxRouteId;
			}
		});
		return maxRouteId;
	}

	public List<RouteDetailsBO> getRoutes() {
		// TODO Auto-generated method stub
		try {
			String query = "SELECT * FROM ROUTES";
			List<RouteDetailsBO> routes = jdbcTemplate.query(query, new ResultSetExtractor<List<RouteDetailsBO>>() {

				@Override
				public List<RouteDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<RouteDetailsBO> routes = new ArrayList<RouteDetailsBO>();
					while(rs.next()) {
						RouteDetailsBO routeDetailsBO = new RouteDetailsBO();
						routeDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
						routeDetailsBO.setSource(rs.getString("SOURCE"));
						routeDetailsBO.setDestination(rs.getString("DESTINATION"));
						routeDetailsBO.setDistance(rs.getDouble("DISTANCE"));
						routes.add(routeDetailsBO);
					}
					return routes;
				}
			});
			if(routes==null) {
				System.out.println("Problem in fetching routes details");
				return null;
			}
			return routes;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while fetching routes");
			e.printStackTrace();
		}
		return null;
	}

}
