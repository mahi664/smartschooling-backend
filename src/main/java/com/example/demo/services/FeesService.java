package com.example.demo.services;

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

import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class FeesService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public List<FeesDetailsBO> addFeeTypes(List<FeesDetailsBO> feesDetailsBOs) {
		try {
			
			int nextFeeId = getMaxFeeId() + 1;
			String query = "INSERT INTO FEE_TYPES VALUES(?,?,?,?,?)";
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
		String query = "SELECT MAX(FEE_ID) AS MAX_FEE_ID FROM FEE_TYPES";
		int maxFeeId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				int maxFeeId = 0;
				while(rs.next()) {
					maxFeeId = rs.getInt("MAX_FEE_ID");
				}
				return maxFeeId;
			}
		});
		
		return maxFeeId;
	}

	public List<FeesDetailsBO> getFeeTypes() {
		try {
			
			String query = "SELECT * FROM FEE_TYPES";
			List<FeesDetailsBO> feeTypes = jdbcTemplate.query(query, new ResultSetExtractor<List<FeesDetailsBO>>() {

				@Override
				public List<FeesDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					List<FeesDetailsBO> feeTypes = new ArrayList<FeesDetailsBO>();
					while(rs.next()) {
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("FEE_ID"));
						feesDetailsBO.setFeeName(rs.getString("FEE_NAME"));
						feesDetailsBO.setFeeDiscription(rs.getString("FEE_DISCRIPTION"));
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
			
			String query = "INSERT INTO FEE_DETAILS VALUES(?,?,?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, feesDetailsBOs.get(i).getFeeId());
					ps.setString(2, feesDetailsBOs.get(i).getClassId()!=null ? feesDetailsBOs.get(i).getClassId() : "");
					ps.setString(3, feesDetailsBOs.get(i).getRouteId()!=null ? feesDetailsBOs.get(i).getRouteId() : "");
					ps.setDouble(4, feesDetailsBOs.get(i).getAmount());
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setDate(6, DateUtils.getSqlDate(new Date(2099, 11, 30)));
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

}
