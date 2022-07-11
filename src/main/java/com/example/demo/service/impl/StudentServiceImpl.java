package com.example.demo.service.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bo.AccountsDetailsBO;
import com.example.demo.bo.ClassDetaislBO;
import com.example.demo.bo.FeeReceivables;
import com.example.demo.bo.FeesDetailsBO;
import com.example.demo.bo.RouteDetailsBO;
import com.example.demo.bo.StudentDetailsBO;
import com.example.demo.bo.StudentsFeesTransactionDetailsBO;
import com.example.demo.bo.TransactionBO;
import com.example.demo.bo.TransactionDetailsBO;
import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.FeeTypes;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.data.entity.StudentFeesDetails;
import com.example.demo.data.entity.StudentTransportDetails;
import com.example.demo.data.repository.FeeTypesRepository;
import com.example.demo.data.repository.GeneralRegisterRepository;
import com.example.demo.data.repository.StudentClassDetailsRepository;
import com.example.demo.data.repository.StudentDetailsRepository;
import com.example.demo.data.repository.StudentFeesDetailsRepository;
import com.example.demo.data.repository.StudentTransportDetailsRepository;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.StudentException;
import com.example.demo.service.StudentService;
import com.example.demo.service.UtilService;
import com.example.demo.service.adapter.StudentServiceAdapter;
import com.example.demo.service.dto.FetchStudentsResponseDto;
import com.example.demo.service.dto.StudentDetailsForRegNoResponseDto;
import com.example.demo.service.dto.StudentImportData;
import com.example.demo.service.dto.StudentImportResponseDto;
import com.example.demo.service.dto.StudentListRequestDto;
import com.example.demo.service.dto.StudentRegistrationDto;
import com.example.demo.service.dto.StudentRegistrationResponseDto;
import com.example.demo.service.helper.FileStorageService;
import com.example.demo.service.validator.StudentValidator;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.DefaultAccountsTypes;
import com.example.demo.utils.FileUtils;
import com.example.demo.utils.ReferenceTableTypes;
import com.example.demo.utils.TransactionTypes;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UtilService utilService;
	
	private Map<String, String> refTableTypesM;
	
	@Autowired
	private StudentServiceAdapter studentServiceAdapter;
	
	@Autowired
	private StudentValidator studentValidator;
	
	@Autowired
	private StudentDetailsRepository studentDetailsRepository;
	
	@Autowired
	private GeneralRegisterRepository generalRegisterRepository;
	
	@Autowired
	private StudentClassDetailsRepository studentClassDetailsRepository;
	
	@Autowired
	private StudentTransportDetailsRepository studentTransportDetailsRepository;
	
	@Autowired
	private FeeTypesRepository feeTypesRepository;
	
	@Autowired
	private StudentFeesDetailsRepository studentFeesDetailsRepository;
	
	@Autowired
	private FileUtils fileUtils;

	@Transactional
	public StudentDetailsBO addNewStudent(StudentDetailsBO studentDetailsBO) {
		try {
			int nextStudentId = 0;
			studentDetailsBO.setStudentId(Integer.toString(nextStudentId));

			boolean res = true;
			res = updateStudentBasicDetails(studentDetailsBO);
			if (res && studentDetailsBO.getStudentClassDetails() != null)
				res = addStudentClassesDetails(studentDetailsBO.getStudentId(),
						studentDetailsBO.getStudentClassDetails());
			if (res && studentDetailsBO.getStudentFeeDetails() != null)
				res = addStudentFeesDetails(studentDetailsBO.getStudentId(), studentDetailsBO.getStudentFeeDetails());
			if (!res) {
				System.out.println("Problem in adding student Details");
				return null;
			}
			studentDetailsBO.setStudentId(getFormattedStudentId(nextStudentId));
			return studentDetailsBO;
		} catch (Exception e) {
			System.out.println("Error while adding new Student");
			throw e;
		}
	}

	private boolean updateStudentBasicDetails(StudentDetailsBO studentDetailsBO) {
		String query = "UPDATE STUDENT_DETAILS SET FIRST_NAME = ?, MIDDLE_NAME = ?, LAST_NAME = ?, "
				+ "BIRTH_DATE = ?, GENDER = ?, ADHAR = ?, EMAIL = ?, MOBILE = ?, ALTERNATE_MOBILE = ?,"
				+ "ADDRESS = ?, RELIGION = ?, CASTE = ?, TRANSPORT = ?, NATIONALITY = ? "
				+ "WHERE STUD_ID = ?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
			
				ps.setString(1, studentDetailsBO.getFirstName());
				ps.setString(2, studentDetailsBO.getMiddleName());
				ps.setString(3, studentDetailsBO.getLastName());
				ps.setDate(4, DateUtils.getSqlDate(studentDetailsBO.getBirthDate()));
				ps.setString(5, studentDetailsBO.getGender());
				ps.setString(6, studentDetailsBO.getAdharNumber());
				ps.setString(7, studentDetailsBO.getEmail());
				ps.setString(8, studentDetailsBO.getMobileNumber());
				ps.setString(9, studentDetailsBO.getAlternateMobile());
				ps.setString(10, studentDetailsBO.getAddress());
				ps.setString(11, studentDetailsBO.getReligion());
				ps.setString(12, studentDetailsBO.getCaste());
				ps.setString(13, studentDetailsBO.isTransportOpted() ? Constants.YES : Constants.NO);
				ps.setString(14, studentDetailsBO.getNationality());
				ps.setString(15, studentDetailsBO.getStudentId());
			}
		});
		return res>0 ? true : false;
	}

	private boolean addStudentFeesDetails(String studentId, Map<String, List<FeesDetailsBO>> studentFeeDetails) {
		boolean res = true;
		for (String academicId : studentFeeDetails.keySet()) {
			res = res && addStudentFeesDetails(studentId, academicId, studentFeeDetails.get(academicId));
		}
		return res;
	}

	private boolean addStudentFeesDetails(String studentId, String academicId, List<FeesDetailsBO> feesDetailsBOs) {
		String query = "INSERT INTO STUDENT_FEES_DETAILS VALUES(?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, studentId);
				ps.setString(2, academicId);
				ps.setString(3, feesDetailsBOs.get(i).getFeeId());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}

			@Override
			public int getBatchSize() {
				return feesDetailsBOs.size();
			}
		});
		return res.length <= 0 ? false : true;
	}

	private boolean addStudentClassesDetails(String studentId, Map<String, List<ClassDetaislBO>> studentClassDetails) {
		boolean res = true;
		for (String academicId : studentClassDetails.keySet()) {
			res = res && addStudentClassesDetails(studentId, academicId, studentClassDetails.get(academicId));
		}
		return res;
	}

	private boolean addStudentClassesDetails(String studentId, String academicId,
			List<ClassDetaislBO> classDetaislBOs) {
		String query = "INSERT INTO STUDENT_CLASS_DETAILS VALUES(?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, studentId);
				ps.setString(2, academicId);
				ps.setString(3, classDetaislBOs.get(i).getClassId());
				ps.setDate(4, DateUtils.getSqlDate(new Date()));
				ps.setString(5, "BASE");
			}

			@Override
			public int getBatchSize() {
				return classDetaislBOs.size();
			}
		});
		return res.length <= 0 ? false : true;
	}

	private String getFormattedStudentId(int studentId) {
		String nextStudentIdString = Constants.STUDENT_ID_PREFIX;
		if (studentId < 10)
			nextStudentIdString += "000" + studentId;
		else if (studentId >= 10 && studentId < 100)
			nextStudentIdString += "00" + studentId;
		else if (studentId >= 100 && studentId < 1000)
			nextStudentIdString += "0" + studentId;
		else
			nextStudentIdString += studentId;
		return nextStudentIdString;
	}

	public List<StudentDetailsBO> getStudentDetails() {
		Map<String, StudentDetailsBO> studentDetails = getStudentBasicDetails();
		if (studentDetails != null && studentDetails.size() > 0) {
			populateStudentAcademicDetails(studentDetails);
			populateStudentFeesDetails(studentDetails);
			populateStudentTransportDetails(studentDetails);
		}
		return new ArrayList<StudentDetailsBO>(studentDetails.values());
	}

	private void populateStudentTransportDetails(Map<String, StudentDetailsBO> studentDetails) {
		String query = "select A.*, B.source,B.destination,B.distance " + "from student_transport_details A, routes B "
				+ "where A.route_id = B.route_id";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if (studentDetailsBO.getRouteDetailsBO() == null)
						studentDetailsBO.setRouteDetailsBO(new RouteDetailsBO());
					RouteDetailsBO routeDetailsBO = studentDetailsBO.getRouteDetailsBO();
					routeDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
					routeDetailsBO.setSource(rs.getString("SOURCE"));
					routeDetailsBO.setDestination(rs.getString("DESTINATION"));
					routeDetailsBO.setDistance(rs.getDouble("DISTANCE"));
				}
				return null;
			}
		});
	}

	private void populateStudentFeesDetails(Map<String, StudentDetailsBO> studentDetails) {

		String query = "select B.stud_id, B.academic_id, B.fee_id, C.fee_name, A.amount, A.class_id, A.route_id from fee_details A, student_fees_details B, fee_types C "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id = B.academic_id) "
				+ "or A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id= ? and A.route_id=?))";
		jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, Constants.BLANK_STRING);
				ps.setString(2, Constants.BLANK_STRING);
			}
		}, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					String academicId = rs.getString("ACADEMIC_ID");
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if (studentDetails != null) {
						Map<String, List<FeesDetailsBO>> feesDetails = studentDetailsBO.getStudentFeeDetails();
						if (feesDetails == null)
							feesDetails = new HashMap<String, List<FeesDetailsBO>>();
						if (!feesDetails.containsKey(academicId))
							feesDetails.put(academicId, new ArrayList<FeesDetailsBO>());
						List<FeesDetailsBO> feesDetailsBOs = feesDetails.get(academicId);
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("FEE_ID"));
						feesDetailsBO.setFeeName(rs.getString("FEE_NAME"));
						feesDetailsBO.setAmount(rs.getDouble("AMOUNT"));
						feesDetailsBO.setClassId(rs.getString("CLASS_ID"));
						feesDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
						feesDetailsBOs.add(feesDetailsBO);
						studentDetailsBO.setStudentFeeDetails(feesDetails);
					}
				}

				return null;
			}
		});
	}

	private void populateStudentAcademicDetails(Map<String, StudentDetailsBO> studentDetails) {
		String query = "select A.*, B.class_name from student_class_details A, classes B "
				+ "where A.class_id = B.class_id";
		jdbcTemplate.query(query, new ResultSetExtractor<Void>() {

			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					if (studentDetailsBO != null) {
						Map<String, List<ClassDetaislBO>> classDetails = studentDetailsBO.getStudentClassDetails();
						if (classDetails == null)
							classDetails = new HashMap<String, List<ClassDetaislBO>>();
						String academicId = rs.getString("ACADEMIC_ID");
						if (!classDetails.containsKey(academicId))
							classDetails.put(academicId, new ArrayList<ClassDetaislBO>());
						List<ClassDetaislBO> classes = classDetails.get(academicId);
						ClassDetaislBO classDetaislBO = new ClassDetaislBO();
						classDetaislBO.setClassId(rs.getString("CLASS_ID"));
						classDetaislBO.setClassName(rs.getString("CLASS_NAME"));
						classes.add(classDetaislBO);
						studentDetailsBO.setStudentClassDetails(classDetails);
					}
				}

				return null;
			}
		});
	}

	private Map<String, StudentDetailsBO> getStudentBasicDetails() {
		String query = "SELECT * FROM STUDENT_DETAILS";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, StudentDetailsBO>>() {

			@Override
			public Map<String, StudentDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, StudentDetailsBO> studentDetails = new HashMap<String, StudentDetailsBO>();
				while (rs.next()) {
					String studentId = getFormattedStudentId(rs.getInt("STUD_ID"));
					if (!studentDetails.containsKey(studentId))
						studentDetails.put(studentId, new StudentDetailsBO());
					StudentDetailsBO studentDetailsBO = studentDetails.get(studentId);
					studentDetailsBO.setStudentId(studentId);
					studentDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					if (rs.getString("MIDDLE_NAME") != null)
						studentDetailsBO.setMiddleName(rs.getString("MIDDLE_NAME"));
					else
						studentDetailsBO.setMiddleName(Constants.BLANK_STRING);
					studentDetailsBO.setLastName(rs.getString("LAST_NAME"));
					studentDetailsBO.setBirthDate(DateUtils.getDate(rs.getDate("BIRTH_DATE")));
					studentDetailsBO.setGender(rs.getString("GENDER"));
					studentDetailsBO.setAdharNumber(rs.getString("ADHAR"));
					studentDetailsBO.setEmail(rs.getString("EMAIL"));
					studentDetailsBO.setMobileNumber(rs.getString("MOBILE"));
					studentDetailsBO.setAlternateMobile(rs.getString("ALTERNATE_MOBILE"));
					studentDetailsBO.setAddress(rs.getString("ADDRESS"));
					studentDetailsBO.setReligion(rs.getString("RELIGION"));
					studentDetailsBO.setCaste(rs.getString("CASTE"));
					studentDetailsBO.setTransportOpted(Constants.YES.equals(rs.getString("TRANSPORT")) ? true : false);
					studentDetailsBO.setNationality(rs.getString("NATIONALITY"));

//					if(studentDetailsBO.getRouteDetailsBO()==null)
//						studentDetailsBO.setRouteDetailsBO(new RouteDetailsBO());
//					RouteDetailsBO routeDetailsBO = studentDetailsBO.getRouteDetailsBO();
//					routeDetailsBO.setRouteId(rs.getString("ROUTE_ID"));
//					routeDetailsBO.setSource(rs.getString("SOURCE"));
//					routeDetailsBO.setDestination(rs.getString("DESTINATION"));
//					routeDetailsBO.setDistance(rs.getDouble("DISTANCE"));
				}
				return studentDetails;
			}
		});
	}

	@Transactional
	public boolean deleteStudentCompleteDetails(String studentID) {
		/*
		 * Following if condition applied as this deleteStudentCompleteDetails can be
		 * called through controller as api or internally by the updateStudentDetails
		 * action. So filter condition needs to be checked.
		 * 
		 */

		int studID;
		if (studentID.startsWith(Constants.STUDENT_ID_PREFIX)) {
			studID = reformatStudentID(studentID);
		} else {
			studID = Integer.parseInt(studentID);
		}

		System.out.println(
				"deleteStudentCompleteDetails has been invoked for ID : " + studentID + " and filtered : " + studID);

		String sID = "" + studID;
		boolean result = false;

		try {

			result = deleteEntryStudentClassDetails(sID);
			if (result)
				System.out.println(
						"Entry for student id " + sID + " has been deleted from the Student_Class_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID " + sID);

			result = deleteEntryStudentFeesDetails(sID);
			if (result)
				System.out.println(
						"Entry for student id " + sID + " has been deleted from the Student_Fees_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID " + sID);

			result = deleteEntryStudentTransportDetails(sID);
			if (result)
				System.out.println(
						"Entry for student id " + sID + " has been deleted from the Student_Transport_Details table.");
			else
				System.out.println("Entry does not exitst in the table for sID " + sID);
		} catch (Exception e) {
			System.out.println("Error while Deleting Student in deleteStudentCompleteDetails.");
			e.getStackTrace();
			throw e;

		}

		return result;
	}

	@Transactional
	public StudentDetailsBO updateStudentDetails(StudentDetailsBO studentDetailsBO) {
		String studentid = studentDetailsBO.getStudentId();
		int studentID = reformatStudentID(studentid);

		System.out.println(
				"updateStudentDetails has been invoked for ID : " + studentid + " and filtered : " + studentID);

		///
		try {
			String sID = "" + studentID;
			boolean result = false;

			// To delete entries from 4 tables
			System.out.println("Calling deleteStudentCompleteDetails for student id :" + sID);
			result = deleteStudentCompleteDetails(sID);
			if (result) {
				System.out.println("Deleted all the entries for student ID :" + sID);
			}

			// The student has been deleted from all the tables
			// Now add it again in all tables using the same ID

			// if entry deleted from tables, then only add
			// else it will be normal add only.

			if (result) {
				System.out.println("Calling addNewStudent for student id :" + sID);
				studentDetailsBO = addNewStudent(studentDetailsBO);
				System.out.println("addNewStudent action added student with student id :" + sID);
				System.out.println("Updatation successful.");
			} else {
				System.out.println("Updation not successful.");
			}

			return studentDetailsBO;
		} catch (Exception e) {
			System.out.println("Error while Updating Student");
			e.getStackTrace();
			throw e;
		}
	}

	private int reformatStudentID(String studentid) {
		int sid = -1;

		try {
			if (!StringUtils.isEmpty(studentid)) {
				System.out.println("Reformating student ID : " + studentid);
				// String actualID = studentid.substring(Constants.APPEND_CHARACTERS);
				String actualID = studentid.replaceFirst(Constants.STUDENT_ID_PREFIX, "0");
				System.out.println("After Removing STUDENT_ID_PREFIX : " + actualID);
				sid = Integer.parseInt(actualID);
				System.out.println("Integer student id : " + sid);
				return sid;
			}

		} catch (Exception e) {
			System.out.println("Error while reformating student id.");
			throw e;
		}

		return sid;
	}

	private boolean deleteEntryStudentClassDetails(String stud_id) {
		String query = "DELETE FROM STUDENT_CLASS_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, stud_id);
			}
		});

		return res <= 0 ? false : true;
	}

	private boolean deleteEntryStudentFeesDetails(String stud_id) {
		String query = "DELETE FROM STUDENT_FEES_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, stud_id);
			}
		});

		return res <= 0 ? false : true;
	}

	private boolean deleteEntryStudentTransportDetails(String stud_id) {
		String query = "DELETE FROM STUDENT_TRANSPORT_DETAILS WHERE STUD_ID=?";
		int res = jdbcTemplate.update(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, stud_id);
			}
		});

		return res <= 0 ? false : true;
	}

	public List<StudentDetailsBO> getStudentsReceivables() {
		try {
			Map<String, StudentDetailsBO> studentsReceivableMap = null;
			studentsReceivableMap = getStudentsTotalFeeAmount();
			studentsReceivableMap = getStudentsFeesPaidAmount(studentsReceivableMap);
			studentsReceivableMap = getStudentFeesDueAmount(studentsReceivableMap);
			populateStudentAcademicDetails(studentsReceivableMap);
			return new ArrayList<>(studentsReceivableMap.values());
		}catch (Exception e) {
			System.out.println("Error in fetching students Receivables");
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, StudentDetailsBO> getStudentFeesDueAmount(Map<String, StudentDetailsBO> studentsReceivableMap) {
		for(String studentId : studentsReceivableMap.keySet()) {
			StudentDetailsBO studentDetailsBO = studentsReceivableMap.get(studentId);
			FeeReceivables feeReceivables = studentDetailsBO.getFeeReceivables();
			feeReceivables.setDueAmount(feeReceivables.getTotalFee() - feeReceivables.getPaidAmount());
			studentDetailsBO.setFeeReceivables(feeReceivables);
		}
		return studentsReceivableMap;
	}

	private Map<String, StudentDetailsBO> getStudentsFeesPaidAmount(Map<String, StudentDetailsBO> studentsReceivableMap) {
		String query = "select A.stud_id, sum(B.amount) as fees_paid "
				+ "from students_fees_collection_transaction A, students_fees_collection_transaction_details B "
				+ "where A.collection_id = B.collection_id group by A.stud_id";
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, StudentDetailsBO>>() {

			@Override
			public Map<String, StudentDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					if(!studentsReceivableMap.containsKey(studentId)) {
						studentsReceivableMap.put(studentId, new StudentDetailsBO());
					}
					StudentDetailsBO studentDetailsBO = studentsReceivableMap.get(studentId);
					if(studentDetailsBO.getStudentId()==null || Constants.BLANK_STRING.equals(studentDetailsBO.getStudentId())) {
						studentDetailsBO.setStudentId(studentId);
					}
					if(studentDetailsBO.getFeeReceivables()==null) {
						studentDetailsBO.setFeeReceivables(new FeeReceivables());
					}
					FeeReceivables feeReceivables = studentDetailsBO.getFeeReceivables();
					feeReceivables.setPaidAmount(rs.getDouble("FEES_PAID"));
					studentDetailsBO.setFeeReceivables(feeReceivables);
				}
				return studentsReceivableMap;
			}
		});
	}

	public Map<String, StudentDetailsBO> getStudentsTotalFeeAmount() {
		Map<String, StudentDetailsBO> studentsTotalFeeAmountMap = new HashMap<>();
		String query = "select D.stud_id, D.first_name, D.middle_name, D.last_name, D.mobile, D.address, sum(A.amount) as total_fee "
				+ "from fee_details A, student_fees_details B, fee_types C, student_details D, academic_details E "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and B.stud_id = D.stud_id "
				+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id=E.academic_id) or "
				+ "A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id=? and A.route_id=?)) "
				+ "and B.academic_id = E.academic_id group by D.stud_id";
		return jdbcTemplate.query(query, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, Constants.BLANK_STRING);
				ps.setString(2, Constants.BLANK_STRING);
			}
		}, new ResultSetExtractor<Map<String, StudentDetailsBO>>() {

			@Override
			public Map<String, StudentDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					String studentId = getFormattedStudentId(Integer.parseInt(rs.getString("STUD_ID")));
					if (!studentsTotalFeeAmountMap.containsKey(studentId)) {
						studentsTotalFeeAmountMap.put(studentId, new StudentDetailsBO());
					}
					StudentDetailsBO studentDetailsBO = studentsTotalFeeAmountMap.get(studentId);
					studentDetailsBO.setStudentId(studentId);
					studentDetailsBO.setFirstName(rs.getString("FIRST_NAME"));
					studentDetailsBO.setMiddleName(rs.getString("MIDDLE_NAME"));
					studentDetailsBO.setLastName(rs.getString("LAST_NAME"));
					studentDetailsBO.setMobileNumber(rs.getString("MOBILE"));
					studentDetailsBO.setAddress(rs.getString("ADDRESS"));
					if (studentDetailsBO.getFeeReceivables() == null) {
						studentDetailsBO.setFeeReceivables(new FeeReceivables());
					}
					FeeReceivables feeReceivables = studentDetailsBO.getFeeReceivables();
					feeReceivables.setTotalFee(rs.getDouble("TOTAL_FEE"));
					studentDetailsBO.setFeeReceivables(feeReceivables);
				}
				return studentsTotalFeeAmountMap;
			}
		});
	}

	public Map<String, List<FeesDetailsBO>> getStudentFeesAssignedDetails(String studentId) {
		try {
			String reformattedStudId = Integer.toString(reformatStudentID(studentId));
			String query = "select B.academic_id, B.fee_id, C.fee_name, A.amount, B.last_update_time, B.last_user "
					+ "from fee_details A, student_fees_details B, fee_types C, academic_details D "
					+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id "
					+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id = D.academic_id) or "
					+ "A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id= ? and A.route_id= ?)) "
					+ "and B.academic_id=d.academic_id and B.stud_id = ?";
			return jdbcTemplate.query(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, Constants.BLANK_STRING);
					ps.setString(2, Constants.BLANK_STRING);
					ps.setString(3, reformattedStudId);
				}
			}, new ResultSetExtractor<Map<String, List<FeesDetailsBO>>>() {

				@Override
				public Map<String, List<FeesDetailsBO>> extractData(ResultSet rs)throws SQLException, DataAccessException {
					Map<String, List<FeesDetailsBO>> academicId2FeesDetailsMap = new HashMap<>();
					while(rs.next()) {
						String academicId = rs.getString("ACADEMIC_ID");
						if(!academicId2FeesDetailsMap.containsKey(academicId)) {
							academicId2FeesDetailsMap.put(academicId, new ArrayList<FeesDetailsBO>());
						}
						List<FeesDetailsBO> feesDetailsBOs = academicId2FeesDetailsMap.get(academicId);
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("FEE_ID"));
						feesDetailsBO.setFeeName(rs.getString("FEE_NAME"));
						feesDetailsBO.setAmount(rs.getDouble("AMOUNT"));
						feesDetailsBO.setEffDate(DateUtils.getDate(rs.getDate("LAST_UPDATE_TIME")));
						feesDetailsBO.setLastUser(rs.getString("LAST_USER"));
						
						feesDetailsBOs.add(feesDetailsBO);
					}
					return academicId2FeesDetailsMap;
				}
			});
		} catch (Exception e) {
			System.out.println("Error While getting students fees assigned details");
			e.printStackTrace();
		}
		return null;
	}

	public List<StudentsFeesTransactionDetailsBO> getStudentsFeesCollectionsTransactions(String studentId) {
		try {
			String reformattedStudId = Integer.toString(reformatStudentID(studentId));
			String query = "select A.collection_id,A.account_id,C.account_name,A.last_update_time,A.last_user, B.academic_id, B.amount, B.fee_id, D.fee_name "
					+ "from students_fees_collection_transaction A, students_fees_collection_transaction_details B, accounts C, fee_types D "
					+ "where A.collection_id = B.collection_id and A.account_id=C.account_id and B.fee_id = D.fee_id and A.stud_id=?";
			return jdbcTemplate.query(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, reformattedStudId);
				}
			}, new ResultSetExtractor<List<StudentsFeesTransactionDetailsBO>>() {

				@Override
				public List<StudentsFeesTransactionDetailsBO> extractData(ResultSet rs)throws SQLException, DataAccessException {
					Map<String, StudentsFeesTransactionDetailsBO> txnId2DetailsMap = new HashMap<>();
					while(rs.next()) {
						String txnId = rs.getString("COLLECTION_ID");
						if(!txnId2DetailsMap.containsKey(txnId)) {
							StudentsFeesTransactionDetailsBO txnDetailsBO = new StudentsFeesTransactionDetailsBO();
							txnDetailsBO.setCollectionId(txnId);
							txnDetailsBO.setCollectionDate(DateUtils.getDate(rs.getDate("LAST_UPDATE_TIME")));
							txnDetailsBO.setLastUser(rs.getString("LAST_USER"));
							txnDetailsBO.setAcademicId2FeesDetailsMap(new HashMap<>());
							txnDetailsBO.setAmount(0);
							
							AccountsDetailsBO accountDetailsBO = new AccountsDetailsBO();
							accountDetailsBO.setAccountId(rs.getString("ACCOUNT_ID"));
							accountDetailsBO.setAccountName(rs.getString("ACCOUNT_NAME"));
							txnDetailsBO.setAccountsDetailsBO(accountDetailsBO);
							
							txnId2DetailsMap.put(txnId, txnDetailsBO);
						}
						
						StudentsFeesTransactionDetailsBO txnDetailsBO = txnId2DetailsMap.get(txnId);
						double amount = rs.getDouble("AMOUNT");
						String academicId = rs.getString("ACADEMIC_ID");
						Map<String, List<FeesDetailsBO>> academicId2FeesDetails = txnDetailsBO.getAcademicId2FeesDetailsMap();
						if(!academicId2FeesDetails.containsKey(academicId)) {
							academicId2FeesDetails.put(academicId, new ArrayList<>());
						}
						List<FeesDetailsBO> feesDetailsBOs = academicId2FeesDetails.get(academicId);
						FeesDetailsBO feesDetailsBO = new FeesDetailsBO();
						feesDetailsBO.setFeeId(rs.getString("FEE_ID"));
						feesDetailsBO.setFeeName(rs.getString("FEE_NAME"));
						feesDetailsBO.setAmount(amount);
						feesDetailsBO.setEffDate(DateUtils.getDate(rs.getDate("LAST_UPDATE_TIME")));
						feesDetailsBO.setLastUser(rs.getString("LAST_USER"));
						feesDetailsBOs.add(feesDetailsBO);
						
						txnDetailsBO.setAmount(txnDetailsBO.getAmount() + amount);
					}
					return new ArrayList<>(txnId2DetailsMap.values());
				}
			});
		} catch (Exception e) {
			System.out.println("Error while fetching students fees collection transactions");
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, List<FeesDetailsBO>> getStudentFeesDueDetails(String studentId) {
		try {
			Map<String, List<FeesDetailsBO>> academicID2FeesMap = getStudentFeesAssignedDetails(studentId);
			List<StudentsFeesTransactionDetailsBO> studentsFeesTransactionDetailsBOs = getStudentsFeesCollectionsTransactions(studentId);
			for(StudentsFeesTransactionDetailsBO studTxnDetBO : studentsFeesTransactionDetailsBOs) {
				Map<String, List<FeesDetailsBO>> academicId2FeesPaidMap = studTxnDetBO.getAcademicId2FeesDetailsMap();
				for(String academicId : academicId2FeesPaidMap.keySet()) {
					reduceFeesAssigned(academicID2FeesMap, academicId, academicId2FeesPaidMap.get(academicId));
				}
			}
			return academicID2FeesMap;
		} catch (Exception e) {
			System.out.println("Error while fetching students fees due details");
			e.printStackTrace();
		}
		return null;
	}

	private void reduceFeesAssigned(Map<String, List<FeesDetailsBO>> academicID2FeesMap, String academicId, List<FeesDetailsBO> feesPaidDetailsBOs) {
		for(FeesDetailsBO feesPaidDetailsBO : feesPaidDetailsBOs) {
			for(FeesDetailsBO feesDetailsBO : academicID2FeesMap.get(academicId)) {
				if(feesDetailsBO.getFeeId().equals(feesPaidDetailsBO.getFeeId())) {
					feesDetailsBO.setAmount(feesDetailsBO.getAmount() - feesPaidDetailsBO.getAmount());
				}
			}
		}
	}
	
	@Transactional
	public StudentsFeesTransactionDetailsBO addNewStudentFeeCollectionDetails(String studentId, StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO) {
		try {
			
			refTableTypesM = utilService.getRefTableTypes();
			
			int res = addNewStrudentFeesTxn(studentId, studentsFeesTransactionDetailsBO);
			if(res<=0) {
				System.out.println("Problem in adding student fees collection Txn");
				return null;
			}
			boolean res1 = addNewStduentFeesTxnDet(studentsFeesTransactionDetailsBO);
			if(!res1) {
				System.out.println("Problem in adding student fees collection Txn");
				return null;
			}
			
			TransactionBO transactionBO = populateTransactionBO(studentsFeesTransactionDetailsBO);
			res = addNewTransaction(transactionBO);
			if(res<=0) {
				System.out.println("Problem in adding student fees collection Txn");
				return null;
			}
			res1 = addNewTransactionDetails(transactionBO.getTransactionId(), transactionBO.getTransactionDetailsBO());
			if(!res1) {
				System.out.println("Problem in adding student fees collection Txn");
				return null;
			}
			return studentsFeesTransactionDetailsBO;
		} catch (Exception e) {
			System.out.println("Error while adding new student Fees Collection");
			e.printStackTrace();
			throw e;
		}
	}

	private boolean addNewTransactionDetails(String transactionId, List<TransactionDetailsBO> transactionDetailsBO) {
		String nextTxnDetId = getNextTransactionDetId();
		String query = "INSERT INTO TRANSACTION_DETAILS VALUES(?,?,?,?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			int nextDetId = Integer.parseInt(nextTxnDetId);
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, Integer.toString(nextDetId++));
				ps.setString(2, transactionId);
				ps.setString(3, transactionDetailsBO.get(i).getAccountsDetailsBO().getAccountId());
				ps.setString(4, Character.toString(transactionDetailsBO.get(i).getTransactionType()));
				ps.setString(5, transactionDetailsBO.get(i).getRefId());
				ps.setString(6, transactionDetailsBO.get(i).getRefTableType());
				ps.setDate(7, DateUtils.getSqlDate(new Date()));
				ps.setString(8, "BASE");
			}
			
			@Override
			public int getBatchSize() {
				return transactionDetailsBO.size();
			}
		});
		return res.length<=0 ? false : true;
	}

	protected String getNextTransactionDetId() {
		return Integer.toString(getTransactionDetId() + 1);
	}

	private int getTransactionDetId() {
		String query = "SELECT COUNT(TRANSACTION_DET_ID) AS MAX_ID FROM TRANSACTION_DETAILS";
		return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxTxnId = 0;
				while(rs.next())
					maxTxnId = rs.getInt("MAX_ID");
				return maxTxnId;
			}
			
		});
	}

	private TransactionBO populateTransactionBO(StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO) {
		TransactionBO txnBO = new  TransactionBO();
		txnBO.setTransactionId(getTxnId());
		txnBO.setTransactonDate(studentsFeesTransactionDetailsBO.getCollectionDate());
		txnBO.setAmount(studentsFeesTransactionDetailsBO.getAmount());
		
		List<TransactionDetailsBO> txnDetailsBOs = new ArrayList<>();
		
		TransactionDetailsBO txnDetBO = new TransactionDetailsBO();
		txnDetBO.setAccountsDetailsBO(new AccountsDetailsBO(DefaultAccountsTypes.STUDENT_FEES_ACCOUNT.getValue()));
		txnDetBO.setRefId(studentsFeesTransactionDetailsBO.getCollectionId());
		txnDetBO.setRefTableType(refTableTypesM.get(ReferenceTableTypes.STUDENTS_FEES_COLLECTION_TRANSACTION.getValue()));
		txnDetBO.setTransactionType(TransactionTypes.DEBIT.toString().charAt(0));
		txnDetailsBOs.add(txnDetBO);
		
		txnDetBO = new TransactionDetailsBO();
		txnDetBO.setAccountsDetailsBO(studentsFeesTransactionDetailsBO.getAccountsDetailsBO());
		txnDetBO.setTransactionType(TransactionTypes.CREDIT.toString().charAt(0));
		txnDetailsBOs.add(txnDetBO);
		
		txnBO.setTransactionDetailsBO(txnDetailsBOs);
		return txnBO;
	}

	private int addNewTransaction(TransactionBO transactionBO) {
		String query = "INSERT INTO TRANSACTIONS VALUES(?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, transactionBO.getTransactionId());
				ps.setDouble(2, transactionBO.getAmount());
				ps.setDate(3, DateUtils.getSqlDate(transactionBO.getTransactonDate()));
				ps.setDate(4, DateUtils.getSqlDate(transactionBO.getTransactonDate()));
				ps.setString(5, "BASE");
			}
		});
	}

	private String getTxnId() {
		return CommonUtils.getUniqueId();
	}

	private boolean addNewStduentFeesTxnDet(StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO) {
		boolean result = true;
		for(String academicId : studentsFeesTransactionDetailsBO.getAcademicId2FeesDetailsMap().keySet()) {
			result = result & resaddNewStudentFeesTxnDet(academicId, studentsFeesTransactionDetailsBO.getCollectionId(),
					studentsFeesTransactionDetailsBO.getAcademicId2FeesDetailsMap().get(academicId), studentsFeesTransactionDetailsBO);
		}
		return result;
	}

	private boolean resaddNewStudentFeesTxnDet(String academicId, String collectionId, List<FeesDetailsBO> feesDetailsBOs, StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO) {
		String txnDetId = getNextTxnDetId();
		String query = "INSERT INTO STUDENTS_FEES_COLLECTION_TRANSACTION_DETAILS VALUES(?,?,?,?,?,?,?)";
		int res[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			int nextTxnId = Integer.parseInt(txnDetId);
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, Integer.toString(nextTxnId++));
				ps.setString(2, collectionId);
				ps.setString(3, feesDetailsBOs.get(i).getFeeId());
				ps.setString(4, academicId);
				ps.setDouble(5, feesDetailsBOs.get(i).getAmount());
				ps.setDate(6, DateUtils.getSqlDate(new Date()));
				ps.setString(7, "BASE");
			}
			
			@Override
			public int getBatchSize() {
				return feesDetailsBOs.size();
			}
		});
		return res.length<=0 ? false : true;
	}

	private String getNextTxnDetId() {
		return Integer.toString(getMaxTxnDetId() + 1);
	}

	private int getMaxTxnDetId() {
		String query = "SELECT COUNT(TRANS_DET_ID) AS MAX_ID FROM STUDENTS_FEES_COLLECTION_TRANSACTION_DETAILS";
		return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxTxnId = 0;
				while(rs.next())
					maxTxnId = rs.getInt("MAX_ID");
				return maxTxnId;
			}
			
		});
	}

	private int addNewStrudentFeesTxn(String studentId,StudentsFeesTransactionDetailsBO studentsFeesTransactionDetailsBO) {
		String collectionId = getCollectionId(); 
		studentsFeesTransactionDetailsBO.setCollectionId(collectionId);
		String query = "INSERT INTO STUDENTS_FEES_COLLECTION_TRANSACTION VALUES(?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, studentsFeesTransactionDetailsBO.getCollectionId());
				ps.setString(2, Integer.toString(reformatStudentID(studentId)));
				ps.setDate(3, DateUtils.getSqlDate(studentsFeesTransactionDetailsBO.getCollectionDate()));
				ps.setString(4, studentsFeesTransactionDetailsBO.getAccountsDetailsBO().getAccountId());
				ps.setDate(5, DateUtils.getSqlDate(new Date()));
				ps.setString(6, "BASE");
			}
		});
	}

	private String getCollectionId() {
		return CommonUtils.getUniqueId();
	}

	@Override
	@Transactional(rollbackFor = {StudentException.class, Exception.class})
	public StudentRegistrationResponseDto registerNewStudent(StudentRegistrationDto studentRegistrationDto)
			throws StudentException {
		log.info("new student registration for student name: {}", studentRegistrationDto.getFirstName());
		studentValidator.validateStudentRegistrationRequest(studentRegistrationDto);
		String nextStudentId = Integer.toString(studentDetailsRepository.getMaxStudentId() + 1);
		StudentBasicDetails studentBasicDetails = studentServiceAdapter.getStudentBasicDetails(nextStudentId,
				studentRegistrationDto);
		studentDetailsRepository.addStudentBasicDeails(studentBasicDetails);
		GeneralRegister generalRegister = studentServiceAdapter.getGeneralRegister(nextStudentId,
				studentRegistrationDto);
		generalRegisterRepository.addNewStudentInGeneralRegister(generalRegister);
		StudentClassDetails studentClassDetails = studentServiceAdapter.getStudentClassDetails(nextStudentId,
				studentRegistrationDto);
		studentClassDetailsRepository.addStudentClassDetails(studentClassDetails);
		if (studentRegistrationDto.isTransportOpted()) {
			StudentTransportDetails studentTransportDetails = studentServiceAdapter
					.getStudentTransportDetails(nextStudentId, studentRegistrationDto);
			studentTransportDetailsRepository.addSudentTransportDetails(studentTransportDetails);
		}
		Map<String, String> feeName2IdMap = feeTypesRepository.getFeeTypes().stream()
				.collect(Collectors.toMap(FeeTypes::getFeeName, FeeTypes::getFeeId));
		List<StudentFeesDetails> studentFeesDetails = studentServiceAdapter.getStudentFeesDetails(nextStudentId,
				studentRegistrationDto, feeName2IdMap);
		studentFeesDetailsRepository.addNewStudentFeesDetails(studentFeesDetails);
		return studentServiceAdapter.getStudentRegistrationResponse(nextStudentId, studentRegistrationDto);
	}

	@Override
	public FetchStudentsResponseDto getStudentList(String academicYear, int page, int size, StudentListRequestDto studentListRequestDto)
			throws StudentException {
		log.info("Fetching student list for academic year {}, page {} and size {}", academicYear, page, size);
		PageRequest pageable = PageRequest.of(page, size);
		Page<StudentBasicDetails> pagedStudentData = Optional
				.ofNullable(studentDetailsRepository.getStudentDetails(academicYear, pageable, studentListRequestDto))
				.orElseThrow(() -> new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND));
		return studentServiceAdapter.getFetchStudentResponseDto(pagedStudentData);
	}

	@Override
	public StudentDetailsForRegNoResponseDto getStudentDetailsForRegNo(int genRegNo) throws StudentException {
		log.info("Getting student details for gen reg no {}", genRegNo);
		GeneralRegister generalRegisterDetails = null;
		try {
			generalRegisterDetails = generalRegisterRepository.getGeneraRegisterDetails(genRegNo);
		} catch (StudentException ex) {
			log.error("Error occured while fetching general register details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
		}
		if (generalRegisterDetails == null) {
			log.info("No record found in general register details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND);
		}
		StudentBasicDetails studentBasicDetails;
		try {
			studentBasicDetails = studentDetailsRepository.getStudentDetails(generalRegisterDetails.getStudentId());
		} catch (StudentException ex) {
			log.error("Error occured while fetching student basic details for {}", genRegNo);
			throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
		}
		StudentTransportDetails studentTransportDetails = null;
		if(studentBasicDetails.isTransportOpted()) {
			try {
				studentTransportDetails = studentTransportDetailsRepository.getStudentTransportDetails(studentBasicDetails.getStudentId());
			} catch (Exception ex) {
				log.error("Error occured while fetching student transport details for {}", genRegNo);
				throw new StudentException(ErrorDetails.STUDENT_DETAILS_NOT_FOUND, ex);
			}
		}
		return studentServiceAdapter.getStudentDetailsForRegNoResponseDto(generalRegisterDetails, studentBasicDetails, studentTransportDetails);
	}

	@Override
	@Transactional(rollbackFor = {StudentException.class, Exception.class})
	public List<StudentImportResponseDto> importStudentDetailsFromFile(MultipartFile file) throws StudentException {
		@Valid List<StudentRegistrationDto> studentRegistrationDtoList = null;
		try {
			log.info("Reading content of file {}", file.getOriginalFilename());
			studentRegistrationDtoList = fileUtils.readStudentImportFile(file.getInputStream());
		} catch (FileStorageException ex) {
			log.error("Error while reading content of uploaded file", ex.getMessage());
			throw new StudentException(ErrorDetails.FILE_UPLOAD_ERROR, ex);
		} catch (IOException ex) {
			log.error("Error while geting input stream of uploaded file", ex.getMessage());
			throw new StudentException(ErrorDetails.FILE_READ_ERROR, ex);
		}
		if (studentRegistrationDtoList == null || studentRegistrationDtoList.isEmpty()) {
			log.info("No data present in excel");
			throw new StudentException(ErrorDetails.BLANK_FILE_ERROR);
		}
		List<String> errorMessages = studentValidator.validateStudentImportData(studentRegistrationDtoList);
		if(errorMessages!=null && !errorMessages.isEmpty()) {
			log.info("Student import validation failed {}", errorMessages.toString());
			throw new StudentException(ErrorDetails.BAD_REQUEST, errorMessages);
		}
		return registerStudentDetails(studentRegistrationDtoList);
	}

	/**
	 * 
	 * @param studentRegistrationDtoList
	 * @return
	 * @throws StudentException
	 */
	public List<StudentImportResponseDto> registerStudentDetails(@Valid List<StudentRegistrationDto> studentRegistrationDtoList) throws StudentException {
		log.info("new student registration for {} students", studentRegistrationDtoList.size());
		String nextStudentId = Integer.toString(studentDetailsRepository.getMaxStudentId() + 1);
		Map<String, String> feeName2IdMap = feeTypesRepository.getFeeTypes().stream()
				.collect(Collectors.toMap(FeeTypes::getFeeName, FeeTypes::getFeeId));
		StudentImportData studentImportData = studentServiceAdapter.getStudentRegistrationDetails(nextStudentId, studentRegistrationDtoList, feeName2IdMap);
		studentDetailsRepository.addStudentBasicDeails(studentImportData.getStudentBasicDetailsList());
		generalRegisterRepository.addNewStudentInGeneralRegister(studentImportData.getGeneralRegisterList());
		studentClassDetailsRepository.addStudentClassDetails(studentImportData.getStudentClassDetailsList());
		if(!studentImportData.getStudentTransportDetailsList().isEmpty()) {
			studentTransportDetailsRepository.addSudentTransportDetails(studentImportData.getStudentTransportDetailsList());
		}
		studentFeesDetailsRepository.addNewStudentFeesDetails(studentImportData.getStudentFeesDetailsList());
		return studentServiceAdapter.getStudentRegistrationResponse(studentImportData);
	}
}
