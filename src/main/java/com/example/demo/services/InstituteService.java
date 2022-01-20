package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.InstituteDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class InstituteService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public InstituteDetailsBO addNewInstitue(InstituteDetailsBO instituteDetailsBO) {
		try {
			int nextInstituteId = getMaxInstitueId() + 1;
			String query = "INSERT INTO INSTITUTE_DET values(?,?,?,?,?,?)";
			int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, Integer.toString(nextInstituteId));
					ps.setString(2, instituteDetailsBO.getInstituteName());
					ps.setDate(3, DateUtils.getSqlDate(instituteDetailsBO.getInstituteFoundationDate()));
					ps.setString(4,instituteDetailsBO.getInstituteAddress());
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setString(6, "BASE");
				}
			});
			if(res<=0)
				return null;
			instituteDetailsBO.setInstituteId(Integer.toString(nextInstituteId));
			return instituteDetailsBO;
		}catch (Exception e) {
			System.out.println("Error in adding new institute");
		}
		return null;
	}

	private int getMaxInstitueId() {
		String query = "SELECT MAX(institute_id) as max_inst_id FROM INSTITUTE_DET";
		int maxInstId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxInstId = 0;
				while(rs.next()) {
					maxInstId = rs.getInt("max_inst_id");
				}
				return maxInstId;
			}
		});
		return maxInstId;
	}

	public InstituteDetailsBO getInstituteDetails() {
		try {
			
			String query = "SELECT * FROM INSTITUTE_DET";
			InstituteDetailsBO instituteDetailsBO = jdbcTemplate.query(query, new ResultSetExtractor<InstituteDetailsBO>() {

				@Override
				public InstituteDetailsBO extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					InstituteDetailsBO instituteDetailsBO = new InstituteDetailsBO();
					while(rs.next()) {
						instituteDetailsBO.setInstituteId(rs.getString("INSTITUTE_ID"));
						instituteDetailsBO.setInstituteName(rs.getString("INSTITUTE_NAME"));
						instituteDetailsBO.setInstituteFoundationDate(DateUtils.getDate(rs.getDate("FOUNDATION_DATE")));
						instituteDetailsBO.setInstituteAddress(rs.getString("ADDRESS"));
					}
					return instituteDetailsBO;
				}
			});
			if(instituteDetailsBO==null) {
				System.out.println("No institute records found");
				return null;
			}
			return instituteDetailsBO;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in getting institute details");
			e.printStackTrace();
		}
		return null;
	}

}
