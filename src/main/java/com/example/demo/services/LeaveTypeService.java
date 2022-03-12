package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.LeaveDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class LeaveTypeService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Transactional
	public LeaveDetailsBO addNewLeaveType(LeaveDetailsBO leaveDetailsBO) {
		String nextLeaveId = getNextUsabelLeaveId();
		leaveDetailsBO.setLeaveId(nextLeaveId);
		String query = "INSERT INTO LEAVE_TYPES VALUES(?,?,?,?,?)";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, leaveDetailsBO.getLeaveId());
				ps.setString(2, leaveDetailsBO.getLeaveName());
				ps.setString(3, leaveDetailsBO.getLeaveDescription());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}
		});
		
		if(res>0) {
			query = "INSERT INTO LEAVE_ACCRUAL_DETAILS VALUES(?,?,?,?,?,?,?,?)";
			res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, leaveDetailsBO.getLeaveId());
					ps.setString(2, leaveDetailsBO.getAccrualFreq());
					ps.setInt(3, leaveDetailsBO.getAccrualDay());
					ps.setDouble(4, leaveDetailsBO.getAmount());
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setDate(6, DateUtils.getSqlDate(Constants.MAX_DATE));
					ps.setDate(7, DateUtils.getSqlDate(new Date()));
					ps.setString(8, "BASE");
				}
			});
		}
		if(res>0)
			return leaveDetailsBO;
		return null;
	}

	private String getNextUsabelLeaveId() {
		int leaveId = getMaxLeaveId();
		return Integer.toString(++leaveId);
	}

	private int getMaxLeaveId() {
		String query = "SELECT COUNT(*) AS MAX_LEAVE_ID FROM LEAVE_TYPES";
		return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxLeaveId = 0;
				while(rs.next())
					maxLeaveId = rs.getInt("MAX_LEAVE_ID");
				return maxLeaveId;
			}
		});
	}
	
	public List<LeaveDetailsBO> getLeaveTypes(){
		String query = "select A.leave_id, A.leave_name, A.description, B.accrual_frequency, B.accrual_day, B.amount, B.start_date, B.end_date "
				+ "from leave_types A, leave_accrual_details B where A.leave_id = B.leave_id";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<LeaveDetailsBO>>() {

			@Override
			public List<LeaveDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<LeaveDetailsBO> leaveDetailsBOs = new ArrayList<>();
				while(rs.next()) {
					LeaveDetailsBO leaveDetailsBO = new LeaveDetailsBO();
					leaveDetailsBO.setLeaveId(rs.getString("LEAVE_ID"));
					leaveDetailsBO.setLeaveName(rs.getString("LEAVE_NAME"));
					leaveDetailsBO.setLeaveDescription(rs.getString("DESCRIPTION"));
					leaveDetailsBO.setAccrualFreq(rs.getString("ACCRUAL_FREQUENCY"));
					leaveDetailsBO.setAccrualDay(rs.getInt("ACCRUAL_DAY"));
					leaveDetailsBO.setAmount(rs.getDouble("AMOUNT"));
					leaveDetailsBO.setStartDate(DateUtils.getDate(rs.getDate("START_DATE")));
					leaveDetailsBO.setEndDate(DateUtils.getDate(rs.getDate("END_DATE")));
					
					leaveDetailsBOs.add(leaveDetailsBO);
				}
				return leaveDetailsBOs;
			}
		});
	}
}
