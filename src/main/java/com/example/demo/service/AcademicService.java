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

import com.example.demo.bo.AcademicDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class AcademicService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<AcademicDetailsBO> getAcademicDetails() {
		// TODO Auto-generated method stub
		try {
			String query = "SELECT * FROM ACADEMIC_DETAILS";
			return jdbcTemplate.query(query, new ResultSetExtractor<List<AcademicDetailsBO>>() {

				@Override
				public List<AcademicDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					List<AcademicDetailsBO> academicList = new ArrayList<AcademicDetailsBO>();
					while(rs.next()) {
						AcademicDetailsBO academicDetailsBO = new AcademicDetailsBO();
						academicDetailsBO.setAcademicId(rs.getString("ACADEMIC_ID"));
						academicDetailsBO.setAcademicYear(rs.getString("ACADEMIC_YEAR"));
						academicDetailsBO.setDisplayName(rs.getString("DISPLAY_NAME"));
						academicDetailsBO.setAcademicStartDate(DateUtils.getDate(rs.getDate("ACADEMIC_START_DATE")));
						academicDetailsBO.setAcademicEndDate(DateUtils.getDate(rs.getDate("ACADEMIC_END_DATE")));
						academicList.add(academicDetailsBO);
					}
					return academicList;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error While fetching academic details");
		}
		return null;
	}
	
	public List<AcademicDetailsBO> addNewAcademicDetails(List<AcademicDetailsBO> academicDetailsBOs){
		try {
			String query = "INSERT INTO ACADEMIC_DETAILS VALUES(?,?,?,?,?,?,?)";
			int ret[] =  jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					academicDetailsBOs.get(i).setAcademicId(academicDetailsBOs.get(i).getAcademicYear());
					ps.setString(1, academicDetailsBOs.get(i).getAcademicYear());
					ps.setString(2, academicDetailsBOs.get(i).getAcademicYear());
					ps.setString(3, academicDetailsBOs.get(i).getDisplayName());
					ps.setDate(4, DateUtils.getSqlDate(academicDetailsBOs.get(i).getAcademicStartDate()));
					ps.setDate(5, DateUtils.getSqlDate(academicDetailsBOs.get(i).getAcademicEndDate()));
					ps.setDate(6, DateUtils.getSqlDate(new Date()));
					ps.setString(7, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return academicDetailsBOs.size();
				}
			});
			
			if(ret.length<=0) {
				System.out.println("Problem in adding new academic details");
				return null;
			}
		} catch (Exception e) {
			System.out.println("Error while adding new academic details");
		}
		return academicDetailsBOs;
	}

}
