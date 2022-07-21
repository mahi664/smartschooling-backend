package com.example.demo.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.LoginRequest;
import com.example.demo.bo.LoginResponse;
import com.example.demo.bo.QuickUserRegistrationRequest;
import com.example.demo.bo.RoleDetailsBO;
import com.example.demo.bo.UserLoinCredsAndRoles;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.JwtUtils;

@Service
public class AuthenticationService {

	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public LoginResponse loginWithPassword(LoginRequest loginDetailsBO) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetailsBO.getUsername(), loginDetailsBO.getPassword()));
		}catch (BadCredentialsException e) {
			// TODO: handle exception
			throw e;
		}
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginDetailsBO.getUsername());
		String token = jwtUtils.generateToken(userDetails);
		
		return new LoginResponse(loginDetailsBO.getUsername(), token, userDetails.getAuthorities().toString());
	}

	@Transactional
	public String addDefaultUserRegistration(QuickUserRegistrationRequest quickUserRegistrationRequest) {
		String query = "INSERT INTO user_basic_details (user_id,first_name,last_name,mobile,birth_date, marital_status, address, gender) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1,"1");
				ps.setString(2, quickUserRegistrationRequest.getFirstName());
				ps.setString(3, quickUserRegistrationRequest.getLastName());
				ps.setString(4, quickUserRegistrationRequest.getMobile());
				ps.setDate(5, DateUtils.getSqlDate(quickUserRegistrationRequest.getBirthDate()));
				ps.setString(6, quickUserRegistrationRequest.getMaritalStatus());
				ps.setString(7, quickUserRegistrationRequest.getAddress());
				ps.setString(8, "Male");
			}
		});
		
		if(res>0) {
			query = "INSERT INTO user_login_details VALUES (?,?,?,?,?,?)";
			res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, "1");
					ps.setString(2, "admin");
					ps.setString(3, bCryptPasswordEncoder.encode("admin"));
					ps.setDate(4, DateUtils.getSqlDate(new Date()));
					ps.setString(5, "BASE");
					ps.setDate(6, DateUtils.getSqlDate(new Date()));
				}
			});
		}
		
		if(res>0) {
			query = "INSERT INTO user_role_mapping VALUES(?,?,?,?,?,?)";
			res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					// TODO Auto-generated method stub
					ps.setString(1, "1");
					ps.setString(2, "1");
					ps.setDate(3, DateUtils.getSqlDate(new Date()));
					ps.setDate(4, DateUtils.getSqlDate(Constants.MAX_DATE));
					ps.setDate(5, DateUtils.getSqlDate(new Date()));
					ps.setString(6, "BASE");
				}
			});
		}
		
		if(res>0)
			return "User registerted successfully";
		else
			return "Error in quick registration";
	}

	public UserLoinCredsAndRoles getUserDetailsByUserName(String username) {
		String query = "select A.user_id, A.username, A.password, B.role_id, C.role_name "
				+ "from user_login_details A, user_role_mapping B, roles C "
				+ "where A.user_id = B.user_id and B.role_id = C.role_id and A.username = ?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, username);
			}
		}, new ResultSetExtractor<UserLoinCredsAndRoles>() {

			@Override
			public UserLoinCredsAndRoles extractData(ResultSet rs) throws SQLException, DataAccessException {
				UserLoinCredsAndRoles userLoinCredsAndRoles = new UserLoinCredsAndRoles();
				while(rs.next()) {
					userLoinCredsAndRoles.setUserId(rs.getString("USER_ID"));
					userLoinCredsAndRoles.setUsername(rs.getString("USERNAME"));
					userLoinCredsAndRoles.setPassword(rs.getString("PASSWORD"));
					
					RoleDetailsBO role = new RoleDetailsBO();
					role.setRoleId(rs.getString("ROLE_ID"));
					role.setRoleName(rs.getString("ROLE_NAME"));
					
					userLoinCredsAndRoles.setRole(role);
				}
				
				return userLoinCredsAndRoles;
			}
		});
	}

}
