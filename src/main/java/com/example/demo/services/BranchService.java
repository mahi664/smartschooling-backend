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

import com.example.demo.bo.BranchDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class BranchService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public BranchDetailsBO addNewBranch(String instituteId, BranchDetailsBO branchDetailsBO) {
		try {
			
			int nextBranchId = getMaxBranchId() + 1;
			String query = "INSERT INTO INSTITUTE_BRANCH_DET VALUES(?,?,?,?,?,?,?)";
			int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, Integer.toString(nextBranchId));
					ps.setString(2, instituteId);
					ps.setString(3,branchDetailsBO.getBranchName());
					ps.setDate(4, DateUtils.getSqlDate(branchDetailsBO.getFoundationDate()));
					ps.setString(5, branchDetailsBO.getBranchAddress());
					ps.setDate(6, DateUtils.getSqlDate(new Date()));
					ps.setString(7, "BASE");
				}
			});
			
			if(res<=0) {
				System.out.println("problem in adding new branch details");
				return null;
			}
			branchDetailsBO.setBranchId(Integer.toString(nextBranchId));
			return branchDetailsBO;
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in Adding new branch");
			e.printStackTrace();
		}
		return null;
	}

	private int getMaxBranchId() {
		String query = "SELECT MAX(BRANCH_ID) as MAX_BRANCH_ID FROM INSTITUTE_BRANCH_DET";
		int maxBranchId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxInstId = 0;
				while(rs.next()) {
					maxInstId = rs.getInt("MAX_BRANCH_ID");
				}
				return maxInstId;
			}
		});
		return maxBranchId;
	}

	public List<BranchDetailsBO> getAllBranches() {
		// TODO Auto-generated method stub
		try {
			
			String query = "SELECT * FROM INSTITUTE_BRANCH_DET";
			List<BranchDetailsBO> branches = jdbcTemplate.query(query, new ResultSetExtractor<List<BranchDetailsBO>>() {

				@Override
				public List<BranchDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<BranchDetailsBO> branches = new ArrayList<BranchDetailsBO>();
					while(rs.next()) {
						BranchDetailsBO branchDetailsBO = new BranchDetailsBO();
						branchDetailsBO.setBranchId(rs.getString("BRANCH_ID"));
						branchDetailsBO.setBranchName(rs.getString("BRANCH_NAME"));
						branchDetailsBO.setFoundationDate(DateUtils.getDate(rs.getDate("FOUNDATION_DATE")));
						branchDetailsBO.setBranchAddress(rs.getString("ADDRESS"));
						branches.add(branchDetailsBO);
					}
					return branches;
				}
			});
			
			if(branches==null) {
				System.out.println("No record found");
			}
			return branches;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while fetching branches");
			e.printStackTrace();
		}
		return null;
	}

}
