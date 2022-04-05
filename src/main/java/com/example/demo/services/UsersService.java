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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.UserBasicDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class UsersService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional
	public UserBasicDetailsBO addNewUser(UserBasicDetailsBO userBasicDetailsBO) {
		String userId = getNextUserId();
		userBasicDetailsBO.setUserId(userId);
		String query = "INSERT INTO user_basic_details "
				+ "(user_id, first_name, middle_name, last_name, mobile, email, address, birth_date, marital_status, adhar, religion, caste, nationality, gender, alternate_mobile) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userBasicDetailsBO.getUserId());
				ps.setString(2, userBasicDetailsBO.getFirstName());
				ps.setString(3, userBasicDetailsBO.getMiddleName());
				ps.setString(4, userBasicDetailsBO.getLastName());
				ps.setString(5, userBasicDetailsBO.getMobile());
				ps.setString(6, userBasicDetailsBO.getEmail());
				ps.setString(7, userBasicDetailsBO.getAddress());
				ps.setDate(8, DateUtils.getSqlDate(userBasicDetailsBO.getBirthDate()));
				ps.setString(9, userBasicDetailsBO.getMaritalStatus());
				ps.setString(10, userBasicDetailsBO.getAdhar());
				ps.setString(11, userBasicDetailsBO.getReligion());
				ps.setString(12, userBasicDetailsBO.getCaste());
				ps.setString(13, userBasicDetailsBO.getNationality());
				ps.setString(14, userBasicDetailsBO.getGender());
				ps.setString(15, userBasicDetailsBO.getAlternateMobile());
			}
		});
		if(res>0) {
			String userName = userBasicDetailsBO.getLastName().charAt(0) + "" + userBasicDetailsBO.getFirstName() + "" + userId;
			String password = bCryptPasswordEncoder.encode(Constants.DEFAULT_PASSWORD);
			query = "INSERT INTO smartschoolingdev.user_login_details "
					+ "(user_id, username, password, last_update_time, last_user) "
					+ "VALUES(?,?,?,?,?)";
			res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, userBasicDetailsBO.getUserId());
					ps.setString(2, userName);
					ps.setString(3, password);
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, "BASE");
//					ps.setDate(res, null);
				}
			});
		}
		if(res<=0) {
			System.out.println("Error while adding new user");
			return null;
		}
		return userBasicDetailsBO;
	}

	private String getNextUserId() {
		int userId = getMaxUserId();
		return Integer.toString(++userId);
	}

	private int getMaxUserId() {
		String query = "select count(*) as max_user_id from user_basic_details";
		return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxUserId = 0;
				while(rs.next()) {
					maxUserId = rs.getInt("max_user_id");
				}
				return maxUserId;
			}
		});
	}

	public List<UserBasicDetailsBO> getUsers() {
		String query = "SELECT * FROM USER_BASIC_DETAILS";
		return jdbcTemplate.query(query, new ResultSetExtractor<List<UserBasicDetailsBO>>() {

			@Override
			public List<UserBasicDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<UserBasicDetailsBO> userBasicDetailsBOs = new ArrayList<>();
				while(rs.next()) {
					UserBasicDetailsBO userBasicDetailsBO = new UserBasicDetailsBO();
					userBasicDetailsBO.setUserId(rs.getString("USER_ID"));
					userBasicDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					userBasicDetailsBO.setMiddleName(rs.getString("MIDDLE_NAME")==null?"":rs.getString("MIDDLE_NAME"));
					userBasicDetailsBO.setLastName(rs.getString("LAST_NAME"));
					userBasicDetailsBO.setMobile(rs.getString("MOBILE"));
					userBasicDetailsBO.setEmail(rs.getString("EMAIL"));
					userBasicDetailsBO.setAddress(rs.getString("ADDRESS"));
					userBasicDetailsBO.setBirthDate(DateUtils.getDate(rs.getDate("BIRTH_DATE")));
					userBasicDetailsBO.setMaritalStatus(rs.getString("MARITAL_STATUS"));
					userBasicDetailsBO.setAdhar(rs.getString("ADHAR"));
					userBasicDetailsBO.setReligion(rs.getString("RELIGION"));
					userBasicDetailsBO.setCaste(rs.getString("CASTE"));
					userBasicDetailsBO.setNationality(rs.getString("NATIONALITY"));
					userBasicDetailsBO.setGender(rs.getString("GENDER"));
					userBasicDetailsBO.setAlternateMobile(rs.getString("ALTERNATE_MOBILE"));
					userBasicDetailsBOs.add(userBasicDetailsBO);
				}
				return userBasicDetailsBOs;
			}
		});
	}
}
