package com.example.demo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.MonthMasterDetailsBO;
import com.example.demo.bo.PayrollDetailsBO;
import com.example.demo.bo.RoleDetailsBO;
import com.example.demo.bo.SalaryDetailsBO;
import com.example.demo.bo.SubjectDetailsBO;
import com.example.demo.bo.UserAcademicDetailsBO;
import com.example.demo.bo.UserAcademicDetailsDto;
import com.example.demo.bo.UserAdvanceDetailsBO;
import com.example.demo.bo.UserBasicDetailsBO;
import com.example.demo.bo.UserManagerDetailsBO;
import com.example.demo.services.helper.UserServiceHelper;
import com.example.demo.utils.CommonUtils;
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
	
	@Autowired
	private UserServiceHelper userServiceHelper;

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
		if (res > 0) {
			String userName = userBasicDetailsBO.getLastName().charAt(0) + "" + userBasicDetailsBO.getFirstName() + ""
					+ userId;
			String password = bCryptPasswordEncoder.encode(Constants.DEFAULT_PASSWORD);
			query = "INSERT INTO smartschoolingdev.user_login_details "
					+ "(user_id, username, password, last_update_time, last_user) " + "VALUES(?,?,?,?,?)";
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
		if (res <= 0) {
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
				while (rs.next()) {
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
				while (rs.next()) {
					UserBasicDetailsBO userBasicDetailsBO = new UserBasicDetailsBO();
					userBasicDetailsBO.setUserId(rs.getString("USER_ID"));
					userBasicDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					userBasicDetailsBO
							.setMiddleName(rs.getString("MIDDLE_NAME") == null ? "" : rs.getString("MIDDLE_NAME"));
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

	public UserAdvanceDetailsBO getUserBasicDetails(String userId) {
		String query = "SELECT * FROM USER_BASIC_DETAILS where user_id=?";

		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}

		}, new ResultSetExtractor<UserAdvanceDetailsBO>() {

			@Override
			public UserAdvanceDetailsBO extractData(ResultSet rs) throws SQLException, DataAccessException {

				while (rs.next()) {
					userAdvanceDetailsBO.setUserId(rs.getString("USER_ID"));
					userAdvanceDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					userAdvanceDetailsBO
							.setMiddleName(rs.getString("MIDDLE_NAME") == null ? "" : rs.getString("MIDDLE_NAME"));
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

	private Map<String, UserAcademicDetailsBO> getUserAcademicDetails(String userId) {
		String query = "select A.*, B.class_name, C.sub_name from USER_ACADEMIC_DETAILS A, classes B, subjects C \r\n"
				+ "where A.class_id = B.class_id and A.sub_id = c.sub_id and user_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}

		}, new ResultSetExtractor<Map<String, UserAcademicDetailsBO>>() {

			@Override
			public Map<String, UserAcademicDetailsBO> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, UserAcademicDetailsBO> userAcademicDetailsMap = new HashMap<>();
				Map<String, Map<String, ClassDetaislBO>> academicID2ClassId2DetailsMap = new HashMap<>();
				while (rs.next()) {
					String academicID = rs.getString("ACADEMIC_ID");
					String classID = rs.getString("CLASS_ID");

					if (!userAcademicDetailsMap.containsKey(academicID)) {
						userAcademicDetailsMap.put(academicID, new UserAcademicDetailsBO());
					}
					UserAcademicDetailsBO userAcademicDetailBo = userAcademicDetailsMap.get(academicID);
					if (userAcademicDetailBo.getUserClassSubjectsL() == null) {
						userAcademicDetailBo.setUserClassSubjectsL(new ArrayList<>());
					}
					if (!academicID2ClassId2DetailsMap.containsKey(academicID)) {
						academicID2ClassId2DetailsMap.put(academicID, new HashMap<>());
					}
					Map<String, ClassDetaislBO> classId2DetailsMap = academicID2ClassId2DetailsMap.get(academicID);
					if (!classId2DetailsMap.containsKey(classID)) {
						ClassDetaislBO classDetaislBO = new ClassDetaislBO();
						classDetaislBO.setClassId(classID);
						classDetaislBO.setClassName(rs.getString("CLASS_NAME"));
						classId2DetailsMap.put(classID, classDetaislBO);
					}
					ClassDetaislBO classDetaislBO = classId2DetailsMap.get(classID);
					if (classDetaislBO.getSubjects() == null) {
						classDetaislBO.setSubjects(new ArrayList<>());
					}
					List<SubjectDetailsBO> subjectDetailsBOs = classDetaislBO.getSubjects();
					SubjectDetailsBO subjectDetailsBO = new SubjectDetailsBO();
					subjectDetailsBO.setSubjectId(rs.getString("SUB_ID"));
					subjectDetailsBO.setSubjectName(rs.getString("SUB_NAME"));
					subjectDetailsBOs.add(subjectDetailsBO);
					classDetaislBO.setSubjects(subjectDetailsBOs);
					userAcademicDetailsMap.get(academicID).setUserClassSubjectsL(
							new ArrayList<>(academicID2ClassId2DetailsMap.get(academicID).values()));
				}
				return userAcademicDetailsMap;
			}
		});
	}

	private List<UserManagerDetailsBO> getUserManagerDetails(String userId) {
		String query = "SELECT A.*, B.first_name as reportee_name FROM USER_MANAGER_MAPPING A, user_basic_details B \r\n"
				+ "where A.reports_to = B.user_id AND A.user_id=?";

		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}

		}, new ResultSetExtractor<List<UserManagerDetailsBO>>() {

			@Override
			public List<UserManagerDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<UserManagerDetailsBO> userManagerDetailsBOs = new ArrayList<>();
				while (rs.next()) {
					UserManagerDetailsBO userManagerDetailsBO = new UserManagerDetailsBO();
					userManagerDetailsBO.setUserId(rs.getString("reports_to"));
					userManagerDetailsBO.setName(rs.getString("reportee_name"));
					userManagerDetailsBO.setEffDate(DateUtils.getDate(rs.getDate("eff_date")));
					userManagerDetailsBO.setEndDate(DateUtils.getDate(rs.getDate("end_date")));

					userManagerDetailsBOs.add(userManagerDetailsBO);
				}
				return userManagerDetailsBOs;
			}

		});
	}

	private List<SalaryDetailsBO> getUserSalaryDetails(String userId) {
		String query = "SELECT * FROM USER_SALARY_DETAILS where user_id=?";

		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}

		}, new ResultSetExtractor<List<SalaryDetailsBO>>() {

			@Override
			public List<SalaryDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<SalaryDetailsBO> salaryDetailsBO = new ArrayList<>();
				while (rs.next()) {
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

	private Map<String, List<PayrollDetailsBO>> getUserPayrollDetails(String userId) {
		String query = "SELECT A.*, B.`month` FROM USER_PAYROLL_DETAILS A, month_master B \r\n"
				+ "where A.month_id = B.month_id AND A.user_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}

		}, new ResultSetExtractor<Map<String, List<PayrollDetailsBO>>>() {

			@Override
			public Map<String, List<PayrollDetailsBO>> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, List<PayrollDetailsBO>> userPayrollDetailsMap = new HashMap<>();
				while (rs.next()) {
					String academicID = rs.getString("ACADEMIC_ID");
					if (!userPayrollDetailsMap.containsKey(academicID)) {
						userPayrollDetailsMap.put(academicID, new ArrayList<>());
					}
					List<PayrollDetailsBO> payrollDetailsBOs = userPayrollDetailsMap.get(academicID);
					PayrollDetailsBO payrollDetails = new PayrollDetailsBO();
					payrollDetails.setPayrollDate(rs.getDate("payroll_date"));
					payrollDetails.setPaidDays(rs.getDouble("paid_days"));
					payrollDetails.setUnpaidDays(rs.getDouble("unpaid_days"));
					payrollDetails.setAmount(rs.getDouble("amount"));
					payrollDetails.setPayrollLocked(rs.getString("payroll_locked").charAt(0));
					MonthMasterDetailsBO monthMasterDetailsBO = new MonthMasterDetailsBO();
					monthMasterDetailsBO.setMonthId(rs.getInt("month_id"));
					monthMasterDetailsBO.setMonthName(rs.getString("month"));
					payrollDetails.setPayrollMonth(monthMasterDetailsBO);
					payrollDetailsBOs.add(payrollDetails);
				}
				return userPayrollDetailsMap;
			}
		});
	}

	public UserAdvanceDetailsBO getUsersAdvanceDetails(String userId) {

		getUserBasicDetails(userId);
		userAdvanceDetailsBO.setUserAcademicDetails(getUserAcademicDetails(userId));
		userAdvanceDetailsBO.setUserManagerDetails(getUserManagerDetails(userId));
		userAdvanceDetailsBO.setUserSalaryDetails(getUserSalaryDetails(userId));
		userAdvanceDetailsBO.setUserPayrollDetails(getUserPayrollDetails(userId));
		userAdvanceDetailsBO.setUserRoles(getUserRoles(userId));
		return userAdvanceDetailsBO;
	}

	private List<RoleDetailsBO> getUserRoles(String userId) {
		String query = "select A.*, B.role_name from user_role_mapping A, roles B \r\n"
				+ "where A.role_id = B.role_id and user_id=?";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		}, new ResultSetExtractor<List<RoleDetailsBO>>() {

			@Override
			public List<RoleDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<RoleDetailsBO> roles = new ArrayList<>();
				while (rs.next()) {
					RoleDetailsBO roleDetailsBO = new RoleDetailsBO();
					roleDetailsBO.setRoleId(rs.getString("role_id"));
					roleDetailsBO.setRoleName(rs.getString("role_name"));
					roleDetailsBO.setEffDate(DateUtils.getDate(rs.getDate("eff_date")));
					roleDetailsBO.setEndDate(DateUtils.getDate(rs.getDate("end_date")));
					roles.add(roleDetailsBO);
				}
				return roles;
			}
		});
	}

	@Transactional
	public Map<String, UserAcademicDetailsBO> addUserAcademicDetails(String userId, Map<String, UserAcademicDetailsBO> usreAcademicDetails) {
		try {
			deleteUserAcademicDetails(userId, usreAcademicDetails);
			insertUserAcademicDetails(userId, userServiceHelper.populateUserAcademicDetailsDto(usreAcademicDetails));
		} catch (Exception e) {
			System.out.println("Error while saving user academic details");
			return null;
		}
		return usreAcademicDetails;
	}

	private int[] insertUserAcademicDetails(String userId, List<UserAcademicDetailsDto> userAcademicDetailsDTOs) {
		String query = "INSERT INTO user_academic_details "
				+ "(user_id, academic_id, class_id, sub_id, last_update_time, last_user) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, userId);
				ps.setString(2, userAcademicDetailsDTOs.get(i).getAcademicId());
				ps.setString(3, userAcademicDetailsDTOs.get(i).getClassId());
				ps.setString(4, userAcademicDetailsDTOs.get(i).getSubjectId());
				ps.setDate(5, DateUtils.getSqlDate(new Date()));
				ps.setString(6, "Mahesh");
			}
			
			@Override
			public int getBatchSize() {
				return userAcademicDetailsDTOs.size();
			}
		});
	}

	private void deleteUserAcademicDetails(String userId, Map<String, UserAcademicDetailsBO> usreAcademicDetails) {
		String query = "delete from user_academic_details where user_id=? and academic_id in ";
		query = CommonUtils.populateInClause(query, new ArrayList<>(usreAcademicDetails.keySet()));
		jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i=1;
				ps.setString(i++, userId);
				for(String academicId : usreAcademicDetails.keySet()) {
					ps.setString(i++, academicId);
				}
			}
		});
	}

	@Transactional
	public List<RoleDetailsBO> addUserRoleDetails(String userId, List<RoleDetailsBO> userRoles) {
		deleteUserRoleDetails(userId);
		updateUserRoleBOs(userRoles);
		int res[] = insertUserRoleDetails(userId,userRoles);
		if(res.length<0) {
			System.out.println("Error while adding user role details");
			return null;
		}
		return userRoles;
	}

	private void updateUserRoleBOs(List<RoleDetailsBO> userRoles) {
		userRoles.stream().forEach(t -> {
			if (t.getEffDate() == null)
				t.setEffDate(new Date());
			if (t.getEndDate() == null)
				t.setEndDate(Constants.MAX_DATE);
		});

		userRoles.sort(new Comparator<RoleDetailsBO>() {

			@Override
			public int compare(RoleDetailsBO o1, RoleDetailsBO o2) {
				if (o1.getEffDate().before(o2.getEffDate()))
					return -1;
				else
					return 1;
			}
		});
		
		userRoles.stream().forEach(t -> {
			int indx = userRoles.indexOf(t);
			if(indx<userRoles.size()-1)
				t.setEndDate(userRoles.get(indx+1).getEffDate());
		});

	}

	private int[] insertUserRoleDetails(String userId, List<RoleDetailsBO> userRoles) {
		String query = "insert into user_role_mapping values(?,?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, userId);
				ps.setString(2, userRoles.get(i).getRoleId());
				ps.setDate(3, DateUtils.getSqlDate(userRoles.get(i).getEffDate()));
				ps.setDate(4, DateUtils.getSqlDate(userRoles.get(i).getEndDate()));
				ps.setDate(5, DateUtils.getSqlDate(new Date()));
				ps.setString(6, "Admin");
			}
			
			@Override
			public int getBatchSize() {
				return userRoles.size();
			}
		});
	}

	private void deleteUserRoleDetails(String userId) {
		String query = "delete from user_role_mapping where user_id = ?";
		jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		});
	}
}
