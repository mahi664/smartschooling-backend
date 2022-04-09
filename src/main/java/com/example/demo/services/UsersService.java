package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.FeeReceivables;
import com.example.demo.bo.MonthMasterDetailsBO;
import com.example.demo.bo.PayrollDetailsBO;
import com.example.demo.bo.SalaryDetailsBO;
import com.example.demo.bo.StudentDetailsBO;
import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.bo.UserAcademicDetailsBO;
import com.example.demo.bo.UserAdvanceDetailsBO;
import com.example.demo.bo.UserBasicDetailsBO;
import com.example.demo.bo.UserManagerDetailsBO;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Service
public class UsersService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	UserAdvanceDetailsBO userAdvanceDetailsBO;
	
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
	
	
	public UserAdvanceDetailsBO getUserBasicDetails(String userId)
	{
		String query = "SELECT * FROM USER_BASIC_DETAILS where user_id=?";
		
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
			
		}, new ResultSetExtractor<UserAdvanceDetailsBO>() {
			
			@Override
			public UserAdvanceDetailsBO extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				while (rs.next()) 
				{
					userAdvanceDetailsBO.setUserId(rs.getString("USER_ID"));
					userAdvanceDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					userAdvanceDetailsBO.setMiddleName(rs.getString("MIDDLE_NAME")==null?"":rs.getString("MIDDLE_NAME"));
					userAdvanceDetailsBO.setLastName(rs.getString("LAST_NAME"));
					userAdvanceDetailsBO.setMobile(rs.getString("MOBILE"));
					userAdvanceDetailsBO.setEmail(rs.getString("EMAIL"));
					userAdvanceDetailsBO.setAddress(rs.getString("ADDRESS"));
					userAdvanceDetailsBO.setBirthDate(DateUtils.getDate(rs.getDate("BIRTH_DATE")));
					userAdvanceDetailsBO.setMaritalStatus(rs.getString("MARITAL_STATUS"));
					userAdvanceDetailsBO.setAdhar(rs.getString("ADHAR"));
					userAdvanceDetailsBO.setReligion(rs.getString("RELIGION"));
					userAdvanceDetailsBO.setCaste(rs.getString("CASTE"));
					userAdvanceDetailsBO.setNationality(rs.getString("NATIONALITY"));
					userAdvanceDetailsBO.setGender(rs.getString("GENDER"));
					userAdvanceDetailsBO.setAlternateMobile(rs.getString("ALTERNATE_MOBILE"));
				}
				return userAdvanceDetailsBO;
			}
			
		});
	}
	
	
	
	
	public String getClassNameFromClassID(String classID)
	{
		String query = "SELECT class_name FROM classes where class_id=?";	
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps)throws SQLException
			{
				ps.setString(1, classID);
			}
		}, new ResultSetExtractor<String>() {
			
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				String classes=null;
				while (rs.next()) 
				{
					classes = rs.getString("class_name");
					
				}
				return classes;
			}
			
		});
	}
	public String getSubjectNameFromSubjectID(String subjectID)
	{
		String query = "SELECT sub_name FROM subjects where sub_id=?";	
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps)throws SQLException
			{
				ps.setString(1, subjectID);
			}
		}, new ResultSetExtractor<String>() {
			
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				String subjects=null;
				while (rs.next()) 
				{
					subjects = rs.getString("sub_name");
					
				}
				return subjects;
			}
			
		});
	}
	public String getMonthNameFromMonthMaster(String monthId)
	{
		String query = "SELECT month FROM month_master where month_id=?";	
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps)throws SQLException
			{
				ps.setString(1, monthId);
			}
		}, new ResultSetExtractor<String>() {
			
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				String month=null;
				while (rs.next()) 
				{
					month = rs.getString("month");
					
				}
				return month;
			}
			
		});
	}
	
	//Prototype to get the user academic details in UserAdvanceDeails
	//private Map<String, UserAcademicDetailsBO> userAcademicDetails; // Map of Academic id to Academic Details
	public Map<String, UserAcademicDetailsBO> getUserAcademicDetails(String userId)
	{
		String query = "SELECT * FROM USER_ACADEMIC_DETAILS where user_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
			
		}, new ResultSetExtractor<Map<String, UserAcademicDetailsBO>>(){
			
			@Override
			public Map<String, UserAcademicDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, UserAcademicDetailsBO> userAcademicDetailsMap = new HashMap<>();
				while(rs.next())
				{
					String academicID = rs.getString("ACADEMIC_ID");
					String classID = rs.getString("CLASS_ID");
					String className = getClassNameFromClassID(classID);
					String subjectID = rs.getString("SUB_ID");
					String subjectName = getSubjectNameFromSubjectID(subjectID);
					
					if(userAcademicDetailsMap.containsKey(academicID))
					{
						UserAcademicDetailsBO userAcademicDetails = userAcademicDetailsMap.get(academicID);
						List<ClassDetaislBO> classDetails = userAcademicDetails.getUserClassSubjectsL();
						
						int foundClasID = 0;
						for(ClassDetaislBO cbo : classDetails)
						{
							String classID_list = cbo.getClassId();
							if(classID_list.equals(classID))
							{
								foundClasID=1;
								SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
								subjectDetailsBO.setSubjectId(subjectID);
								subjectDetailsBO.setSubjectName(subjectName);
								cbo.getSubjects().add(subjectDetailsBO);
								break;
							}
						}
						if(foundClasID==0)
						{
							SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
							subjectDetailsBO.setSubjectId(subjectID);
							subjectDetailsBO.setSubjectName(subjectName);
							List<SubjectDetailsBO> subjects = new ArrayList();
							subjects.add(subjectDetailsBO);
							ClassDetaislBO classDet = new ClassDetaislBO();
							classDet.setClassId(classID);
							classDet.setClassName(className);
							classDet.setSubjects(subjects);
							classDetails.add(classDet);
						}
					}
					else
					{
						SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
						subjectDetailsBO.setSubjectId(subjectID);
						subjectDetailsBO.setSubjectName(subjectName);
						
						List<SubjectDetailsBO> subjects = new ArrayList();
						subjects.add(subjectDetailsBO);
						
						ClassDetaislBO classDetailBO = new ClassDetaislBO();
						classDetailBO.setClassId(classID);
						classDetailBO.setClassName(className);
						classDetailBO.setSubjects(subjects);
						
						List<ClassDetaislBO> classDetailsBOList = new ArrayList();
						classDetailsBOList.add(classDetailBO);
						
						UserAcademicDetailsBO userAcademicDetailsBO = new UserAcademicDetailsBO();
						userAcademicDetailsBO.setUserClassSubjectsL(classDetailsBOList);
						
						userAcademicDetailsMap.put(academicID, userAcademicDetailsBO);
					}
					
				}
				return userAcademicDetailsMap;
			}
		});
	}
	
	public List<UserManagerDetailsBO> getUserManagerDetails(String userId)
	{
		String query = "SELECT * FROM USER_MANAGER_MAPPING where user_id=?";
		
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
			
		}, new ResultSetExtractor<List<UserManagerDetailsBO>>() {
			
			@Override
			public List<UserManagerDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<UserManagerDetailsBO> userManagerDetailsBO = new ArrayList(); 
				while (rs.next()) 
				{
					UserManagerDetailsBO umdBO = new UserManagerDetailsBO();
					umdBO.setUserId(rs.getString("reports_to"));
					umdBO.setEffDate(rs.getDate("eff_date"));
					umdBO.setEndDate(rs.getDate("end_date"));
					
					userManagerDetailsBO.add(umdBO);
				}
				return userManagerDetailsBO;
			}
			
		});
	}
	
	public List<SalaryDetailsBO> getSalaryDetailsOfOneUser(String userId)
	{
		String query = "SELECT * FROM USER_SALARY_DETAILS where user_id=?";
		
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
			
		}, new ResultSetExtractor<List<SalaryDetailsBO>>() {
			
			@Override
			public List<SalaryDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<SalaryDetailsBO> salaryDetailsBO = new ArrayList(); 
				while (rs.next()) 
				{
					SalaryDetailsBO salaryDet = new SalaryDetailsBO();
					salaryDet.setAmount(rs.getDouble("amount"));
					salaryDet.setEffDate(rs.getDate("eff_date"));
					salaryDet.setEndDate(rs.getDate("end_date"));
					
					salaryDetailsBO.add(salaryDet);
				}
				return salaryDetailsBO;
			}
			
		});
	}
	
	//Prototype to get Payroll details in user_payroll_details
	//private Map<String, PayrollDetailsBO> userPayrollDetails; // Map of Academic Id to Payroll details
	public Map<String, List<PayrollDetailsBO>> getUserPayrollDetails(String userId)
	{
		String query = "SELECT * FROM USER_PAYROLL_DETAILS where user_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
			
		}, new ResultSetExtractor<Map<String, List<PayrollDetailsBO>>>(){
			
			@Override
			public Map<String, List<PayrollDetailsBO>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, List<PayrollDetailsBO>> userPayrollDetailsMap = new HashMap<>();
				while(rs.next())
				{
					String academicID = rs.getString("ACADEMIC_ID");
					
					PayrollDetailsBO payrollDetails = new PayrollDetailsBO();
					payrollDetails.setPayrollDate(rs.getDate("payroll_date"));
					payrollDetails.setPaidDays(rs.getDouble("paid_days"));
					payrollDetails.setUnpaidDays(rs.getDouble("unpaid_day"));
					payrollDetails.setAmount(rs.getDouble("amount"));
					payrollDetails.setPayrollLocked(rs.getString("payroll_locked").charAt(0));
					int month_id = rs.getInt("month_id");
					String monthID = ""+month_id;
					String month = getMonthNameFromMonthMaster(monthID); 
					MonthMasterDetailsBO monthMasterDetailsBO = new MonthMasterDetailsBO();
					monthMasterDetailsBO.setMonthId(month_id);
					monthMasterDetailsBO.setMonthName(month);
					payrollDetails.setPayrollMonth(monthMasterDetailsBO);
					
					if(userPayrollDetailsMap.containsKey(academicID))
					{
						List<PayrollDetailsBO> listPayrollDetailsBO = userPayrollDetailsMap.get(academicID);
						listPayrollDetailsBO.add(payrollDetails);
						userPayrollDetailsMap.put(academicID, listPayrollDetailsBO);
						
					}
					else
					{
						List<PayrollDetailsBO> listPayrollDetailsBO = new ArrayList();
						listPayrollDetailsBO.add(payrollDetails);
						userPayrollDetailsMap.put(academicID, listPayrollDetailsBO);
					}
					
				}
				return userPayrollDetailsMap;
			}
		});
	}
	
	public UserAdvanceDetailsBO getUsersAdvanceDetails(String userId)
	{
		
		getUserBasicDetails(userId);
		userAdvanceDetailsBO.setUserAcademicDetails(getUserAcademicDetails(userId));
		userAdvanceDetailsBO.setUserManagerDetails(getUserManagerDetails(userId));
		userAdvanceDetailsBO.setUserSalaryDetails(getSalaryDetailsOfOneUser(userId));
		userAdvanceDetailsBO.setUserPayrollDetails(getUserPayrollDetails(userId));
	    return userAdvanceDetailsBO;
	}
}
