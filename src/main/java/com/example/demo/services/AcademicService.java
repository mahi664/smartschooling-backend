package com.example.demo.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.AcademicDetailsBO;

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

}
