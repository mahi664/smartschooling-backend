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
import com.example.demo.data.entity.StudentFeesDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class StudentFeesDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(StudentFeesDetailsRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param studentFeesDetails
	 * @return
	 * @throws StudentException 
	 */
	public boolean addNewStudentFeesDetails(StudentFeesDetails studentFeesDetails) throws StudentException {
		log.info("Adding student fee record for stud id {} and fee id{}", studentFeesDetails.getStudentId(),
				studentFeesDetails.getFeeId());
		String query = "INSERT INTO STUDENT_FEES_DETAILS VALUES(?,?,?,?,?)";
		log.info("query {}", query);
		int res = 0;
		try {
			jdbcTemplate.update(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, studentFeesDetails.getStudentId());
					ps.setString(2, studentFeesDetails.getAcademicId());
					ps.setString(3, studentFeesDetails.getFeeId());
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, ServiceConstants.ADMIN);
				}
			});
		} catch (Exception ex) {
			log.error("Error while adding student fees details {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return res > 0 ? true : false;
	}
	
	/**
	 * 
	 * @param studentFeesDetailsList
	 * @return
	 * @throws StudentException
	 */
	public boolean addNewStudentFeesDetails(List<StudentFeesDetails> studentFeesDetailsList) throws StudentException {
		log.info("Adding student fee record for {}", studentFeesDetailsList);
		String query = "INSERT INTO STUDENT_FEES_DETAILS VALUES(?,?,?,?,?)";
		log.info("query {}", query);
		int res[] = null;
		try {
			res = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, studentFeesDetailsList.get(i).getStudentId());
					ps.setString(2, studentFeesDetailsList.get(i).getAcademicId());
					ps.setString(3, studentFeesDetailsList.get(i).getFeeId());
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, ServiceConstants.ADMIN);
				}

				@Override
				public int getBatchSize() {
					return studentFeesDetailsList.size();
				}
			});
		} catch (Exception ex) {
			log.error("Error while adding student fees details {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return res.length > 0 ? true : false;
	}
}
