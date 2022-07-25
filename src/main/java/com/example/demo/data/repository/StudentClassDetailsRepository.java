package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class StudentClassDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(StudentClassDetailsRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param studentClassDetails
	 * @throws StudentException
	 */
	public void addStudentClassDetails(StudentClassDetails studentClassDetails) throws StudentException {
		log.info("Inserting record into student class details for {}", studentClassDetails.toString());
		String query = "INSERT INTO student_class_details VALUES(?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, studentClassDetails.getStudentId());
					ps.setString(2, studentClassDetails.getAcademicDetails().getAcademicId());
					ps.setString(3, studentClassDetails.getClassDetails().getClassId());
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, ServiceConstants.ADMIN);
				}
			});
		} catch (Exception ex) {
			log.error("Error while inserting new record into student class details {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * 
	 * @param studentClassDetailsList
	 * @throws StudentException
	 */
	public void addStudentClassDetails(List<StudentClassDetails> studentClassDetailsList) throws StudentException {
		log.info("Inserting record into student class details for {} students", studentClassDetailsList.size());
		String query = "INSERT INTO student_class_details VALUES(?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, studentClassDetailsList.get(i).getStudentId());
					ps.setString(2, studentClassDetailsList.get(i).getAcademicDetails().getAcademicId());
					ps.setString(3, studentClassDetailsList.get(i).getClassDetails().getClassId());
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, ServiceConstants.ADMIN);
				}

				@Override
				public int getBatchSize() {
					return studentClassDetailsList.size();
				}
			});
		} catch (Exception ex) {
			log.error("Error while inserting new record into student class details in batch mode", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
