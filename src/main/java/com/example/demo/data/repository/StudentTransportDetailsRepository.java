package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.StudentTransportDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Repository
public class StudentTransportDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(StudentTransportDetailsRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param studentTransportDetails
	 * @throws StudentException
	 */
	public void addSudentTransportDetails(StudentTransportDetails studentTransportDetails) throws StudentException {
		log.info("Inserting record into student transport details for {}", studentTransportDetails.toString());
		String query = "INSERT INTO STUDENT_TRANSPORT_DETAILS VALUES(?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, studentTransportDetails.getStudentId());
					ps.setString(2, studentTransportDetails.getRouteId());
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setDate(4, DateUtils.getSqlDate(Constants.MAX_DATE));
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setString(6, ServiceConstants.ADMIN);
				}
			});
		} catch (Exception ex) {
			log.error("Error while adding student transport details {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * 
	 * @param studentId
	 * @return
	 * @throws StudentException
	 */
	public StudentTransportDetails getStudentTransportDetails(String studentId) throws StudentException {
		log.info("Fetching student transport details for {}", studentId);
		String query = "select * from student_transport_details where stud_id = ? and eff_date <= ? and end_date >= ?";
		log.info("query {}", query);
		StudentTransportDetails studentTransportDetails = null;
		try {
			List<StudentTransportDetails> studentTransportDetailsList = jdbcTemplate.query(query,
					ps -> setStudentTransportDetails(ps, studentId), (rs, rowNum) -> mapStudentTransportDetails(rs));
			if (studentTransportDetailsList.stream().findFirst().isPresent()) {
				studentTransportDetails = studentTransportDetailsList.stream().findFirst().get();
			}
		} catch (Exception ex) {
			log.error("Error while fetching student transport details for {}", studentId);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return studentTransportDetails;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentTransportDetails mapStudentTransportDetails(ResultSet rs) throws SQLException {
		log.info("mapping result set to student transport details entity");
		return StudentTransportDetails.builder().effDate(rs.getDate("EFF_DATE")).endDate(rs.getDate("END_DATE"))
				.routeId(rs.getString("ROUTE_ID")).studentId(rs.getString("STUD_ID")).build();
	}

	/**
	 * 
	 * @param ps
	 * @param studentId
	 * @throws SQLException
	 */
	private void setStudentTransportDetails(PreparedStatement ps, String studentId) throws SQLException {
		log.info("setting query params {}", studentId, new Date());
		ps.setString(1, studentId);
		ps.setDate(2, DateUtils.getSqlDate(new Date()));
		ps.setDate(3, DateUtils.getSqlDate(new Date()));
	}
}
