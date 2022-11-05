package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.UserLoginDetails;
import com.example.demo.exception.StaffException;
import com.example.demo.utils.DateUtils;

@Service
public class UserLoginDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(UserLoginDetailsRepository.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * @param userLoginDetails
	 * @throws StaffException
	 */
	public void addUserLoginDetails(UserLoginDetails userLoginDetails) throws StaffException {
		log.info("Adding login details for user id {}", userLoginDetails.getUserId());
		String query = "INSERT INTO smartschoolingdev.user_login_details(user_id, username, password, last_update_time, last_user) "
				+ "VALUES(?,?,?,?,?)";
		log.info("Query {}", query);
		try {
			jdbcTemplate.update(query, ps -> setUserLoginDetailsPreparedStatement(ps, userLoginDetails));
		} catch (Exception ex) {
			log.error("Error while inserting user login details");
			throw new StaffException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * @param ps
	 * @param userLoginDetails
	 * @throws SQLException
	 */
	private void setUserLoginDetailsPreparedStatement(PreparedStatement ps, UserLoginDetails userLoginDetails)
			throws SQLException {
		log.info("Setting user login details prepared statement");
		ps.setString(1, userLoginDetails.getUserId());
		ps.setString(2, userLoginDetails.getUserName());
		ps.setString(3, userLoginDetails.getPassword());
		ps.setDate(4, DateUtils.getSqlDate(new Date()));
		ps.setString(5, "BASE");
	}
}
