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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class FeesService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public List<FeesDetailsBO> addFeeTypes(List<FeesDetailsBO> feesDetailsBOs) {
		try {
			
			int nextFeeId = getMaxFeeId() + 1;
			String query = "INSERT INTO fee_types VALUES(?,?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				int feeId = nextFeeId;
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					feesDetailsBOs.get(i).setFeeId(Integer.toString(feeId++));
					ps.setString(1, feesDetailsBOs.get(i).getFeeId());
					ps.setString(2, feesDetailsBOs.get(i).getFeeName());
					ps.setString(3, feesDetailsBOs.get(i).getFeeDiscription());
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return feesDetailsBOs.size();
				}
			});
			if(res.length<=0) {
				System.out.println("Problem while adding fee types");
				return null;
			}
			return feesDetailsBOs;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while adding fee types");
			throw e;
		}
	}

	private int getMaxFeeId() {
		String query = "SELECT COUNT(fee_id) AS max_fee_id FROM fee_types";
		int maxFeeId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				int maxFeeId = 0;
				while(rs.next()) {
					maxFeeId = rs.getInt("max_fee_id");
				}
				return maxFeeId;
			}
		});
		
		return maxFeeId;
	}

	public List<FeesDetailsBO> getFeeTypes() {
		try {
			
			String query = "SELECT * FROM fee_types";
			List<FeesDetailsBO> feeTypes = jdbcTemplate.query(query, new ResultSetExtractor<List<FeesDetailsBO>>() {

				@Override
				public List<FeesDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					List<FeesDetailsBO> feeTypes = new ArrayList<FeesDetailsBO>();
					while(rs.next()) {
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("fee_id"));
						feesDetailsBO.setFeeName(rs.getString("fee_name"));
						feesDetailsBO.setFeeDiscription(rs.getString("fee_description"));
						feeTypes.add(feesDetailsBO);
					}
					return feeTypes;
				}
			});
			
			if(feeTypes==null) {
				System.out.println("Problem in fetching fee types");
				return null;
			}
			return feeTypes;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while fetching fee types");
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public List<FeesDetailsBO> addFeeDetails(List<FeesDetailsBO> feesDetailsBOs) {
		// TODO Auto-generated method stub
		try {
			
			String query = "INSERT INTO fee_details VALUES(?,?,?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, feesDetailsBOs.get(i).getFeeId());
					ps.setString(2, feesDetailsBOs.get(i).getClassId()!=null ? feesDetailsBOs.get(i).getClassId() : "");
					ps.setString(3, feesDetailsBOs.get(i).getRouteId()!=null ? feesDetailsBOs.get(i).getRouteId() : "");
					ps.setDouble(4, feesDetailsBOs.get(i).getAmount());
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setDate(6, DateUtils.getSqlDate(Constants.MAX_DATE));
					
					feesDetailsBOs.get(i).setEffDate(new Date());
					feesDetailsBOs.get(i).setEndDate(Constants.MAX_DATE);
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return feesDetailsBOs.size();
				}
			});
			if(res.length<=0) {
				System.out.println("Problem while adding fee details");
				return null;
			}
			return feesDetailsBOs;
		} catch (Exception e) {
			System.out.println("Error while adding fee details");
			throw e;
		}
	}

	public List<FeesDetailsBO> getFeeDetails(String feeId) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM fee_details ";
		if(feeId!=null) {
			query += "WHERE FEE_ID = ?";
		}
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				if(feeId!=null)
					ps.setString(1, feeId);
			}
		}, new ResultSetExtractor<List<FeesDetailsBO>>() {

			@Override
			public List<FeesDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				List<FeesDetailsBO> feeDetailsList = new ArrayList<>();
				while(rs.next()) {
					FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
					feesDetailsBO.setFeeId(rs.getString("fee_id"));
					feesDetailsBO.setClassId(rs.getString("class_id"));
					feesDetailsBO.setRouteId(rs.getString("route_id"));
					feesDetailsBO.setAmount(rs.getDouble("amount"));
					feesDetailsBO.setEffDate(DateUtils.getDate(rs.getDate("eff_date")));
					feesDetailsBO.setEndDate(DateUtils.getDate(rs.getDate("end_date")));
					
					feeDetailsList.add(feesDetailsBO);
				}
				return feeDetailsList;
			}
		});
	}

}
