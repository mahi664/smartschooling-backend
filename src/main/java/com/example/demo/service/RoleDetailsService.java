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

import com.example.demo.bo.LeaveDetailsBO;
import com.example.demo.bo.RoleDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class RoleDetailsService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Transactional
	public RoleDetailsBO addNewRoleDetails(RoleDetailsBO roleDetailsBO) {
		String query = "INSERT INTO ROLES VALUES(?,?,?,?,?)";
		String nextRoleId = getNextUsableRoleId();
		roleDetailsBO.setRoleId(nextRoleId);
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, roleDetailsBO.getRoleId());
				ps.setString(2, roleDetailsBO.getRoleName());
				ps.setString(3, roleDetailsBO.getRoleDescription());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}
		});
		if (res <= 0) {
			System.out.println("Error while adding new role");
			return null;
		}
		return roleDetailsBO;
	}

	public List<RoleDetailsBO> getRolesDetails() {
		String query = "SELECT * FROM ROLES";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<RoleDetailsBO>>() {

			@Override
			public List<RoleDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<RoleDetailsBO> roleDetailsBOs = new ArrayList<>();
				while (rs.next()) {
					RoleDetailsBO role = new RoleDetailsBO();
					role.setRoleId(rs.getString("ROLE_ID"));
					role.setRoleName(rs.getString("ROLE_NAME"));
					role.setRoleDescription(rs.getString("ROLE_DESCRIPTION"));
					roleDetailsBOs.add(role);
				}
				return roleDetailsBOs;
			}
		});
	}

	private String getNextUsableRoleId() {
		int roleId = getMaxRoleId();
		return Integer.toString(++roleId);
	}

	private int getMaxRoleId() {
		String query = "SELECT COUNT(*) AS MAX_ROLE_ID FROM ROLES";
		return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxRoleId = 0;
				while (rs.next())
					maxRoleId = rs.getInt("MAX_ROLE_ID");
				return maxRoleId;
			}
		});
	}

	public List<LeaveDetailsBO> addRoleApplicableLeaveConfig(String roleId, List<LeaveDetailsBO> leaveTypes,
			boolean isUpdateOp) {
		if (isUpdateOp) {
			deleteRoleApplicableLeaveTypes(roleId);
		}

		int res[] = addRoleApplicableLeaveTypes(roleId, leaveTypes);
		if(res.length>0)
			return leaveTypes;
		return null;
	}

	private int[] addRoleApplicableLeaveTypes(String roleId, List<LeaveDetailsBO> leaveTypes) {
		String query = "INSERT INTO ROLE_APPLICABLE_LEAVES VALUES (?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, roleId);
				ps.setString(2, leaveTypes.get(i).getLeaveId());
				ps.setDate(3, DateUtils.getSqlDate(new Date()));
				ps.setDate(4, DateUtils.getSqlDate(Constants.MAX_DATE));
				ps.setDate(5, DateUtils.getSqlDate(new Date()));
				ps.setString(6, "BASE");
			}
			
			@Override
			public int getBatchSize() {
				return leaveTypes.size();
			}
		});
	}

	private void deleteRoleApplicableLeaveTypes(String roleId) {
		String query = "DELETE FROM ROLE_APPLICABLE_LEAVES WHERE ROLE_ID=?";
		jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, roleId);
			}
		});
	}

	public List<LeaveDetailsBO> getRoleApplicableLeaveConfig(String roleId) {
		String query = "select A.leave_id, A.leave_name, A.description from leave_types A, role_applicable_leaves B "
				+ "where A.leave_id = B.leave_id and B.role_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, roleId);
			}
		}, new ResultSetExtractor<List<LeaveDetailsBO>>() {

			@Override
			public List<LeaveDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<LeaveDetailsBO> leaveDetailsBOs = new ArrayList<>();
				while(rs.next()) {
					LeaveDetailsBO leaveDetailsBO = new LeaveDetailsBO();
					leaveDetailsBO.setLeaveId(rs.getString("leave_id"));
					leaveDetailsBO.setLeaveName(rs.getString("leave_name"));
					leaveDetailsBO.setLeaveDescription(rs.getString("description"));
					leaveDetailsBOs.add(leaveDetailsBO);
				}
				return leaveDetailsBOs;
			}
		});
	}
}
