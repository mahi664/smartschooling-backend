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

import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.utils.DateUtils;

@Service
public class SubjectService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<SubjectDetailsBO> addNewSubjects(List<SubjectDetailsBO> subjectDetailsBOs) {
		try {
			String query = "INSERT INTO SUBJECTS VALUES(?,?,?,?)";
			int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, subjectDetailsBOs.get(i).getSubjectId());
					ps.setString(2, subjectDetailsBOs.get(i).getSubjectName());
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setString(4, "BASE");
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return subjectDetailsBOs.size();
				}
			});
			if(res.length<=0) {
				System.out.println("Problem while adding new subjects details");
				return null;
			}
			return subjectDetailsBOs;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while adding new subjects details");
			e.printStackTrace();
		}
		return null;
	}

	public List<SubjectDetailsBO> getSubjects() {
		try {
			String query = "SELECT * FROM SUBJECTS";
			List<SubjectDetailsBO> subjectDetailsBOs = jdbcTemplate.query(query, new ResultSetExtractor<List<SubjectDetailsBO>>() {

				@Override
				public List<SubjectDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					// TODO Auto-generated method stub
					List<SubjectDetailsBO> subjects = new ArrayList<SubjectDetailsBO>();
					while(rs.next()) {
						SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
						subjectDetailsBO.setSubjectId(rs.getString("SUB_ID"));
						subjectDetailsBO.setSubjectName(rs.getString("SUB_NAME"));
						subjects.add(subjectDetailsBO);
					}
					return subjects;
				}
			});
			if(subjectDetailsBOs==null) {
				System.out.println("Problem in fetching subjects details");
				return null;
			}
			return subjectDetailsBOs;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error while fetching subject details");
			e.printStackTrace();
		}
		return null;
	}

}
