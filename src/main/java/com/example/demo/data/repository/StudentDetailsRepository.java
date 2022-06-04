package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.ClassDetails;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

@Repository
public class StudentDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(StudentDetailsRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param studentBasicDetails
	 * @throws StudentException
	 */
	public void addStudentBasicDeails(StudentBasicDetails studentBasicDetails) throws StudentException {
		log.info("Inserting studnet basic details for {}", studentBasicDetails.toString());
		String query = "INSERT INTO STUDENT_DETAILS VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, studentBasicDetails.getStudentId());
					ps.setString(2, studentBasicDetails.getFirstName());
					ps.setString(3, studentBasicDetails.getMiddleName());
					ps.setString(4, studentBasicDetails.getLastName());
					ps.setDate(5, DateUtils.getSqlDate(studentBasicDetails.getBirthDate()));
					ps.setString(6, studentBasicDetails.getGender());
					ps.setString(7, studentBasicDetails.getAdharNumber());
					ps.setString(8, studentBasicDetails.getEmail());
					ps.setString(9, studentBasicDetails.getMobileNumber());
					ps.setString(10, studentBasicDetails.getAlternateMobile());
					ps.setString(11, studentBasicDetails.getAddress());
					ps.setString(12, studentBasicDetails.getReligion());
					ps.setString(13, studentBasicDetails.getCaste());
					ps.setString(14, studentBasicDetails.isTransportOpted() ? Constants.YES : Constants.NO);
					ps.setString(15, studentBasicDetails.getNationality());
				}
			});
		} catch (Exception ex) {
			log.info("Error while saving student basic details {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxStudentId() {
		log.info("getting max student id");
		String query = "SELECT COUNT(STUD_ID) as MAX_STUDENT_ID FROM STUDENT_DETAILS";
		int maxStudId = jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				int maxStudId = 0;
				while (rs.next())
					maxStudId = rs.getInt("MAX_STUDENT_ID");
				return maxStudId;
			}
		});
		return maxStudId;
	}

	/**
	 * This method return student details like basic details, general register
	 * details, student class details.
	 * 
	 * @param size
	 * @param page
	 * @param academicYear
	 * @return
	 * @throws StudentException
	 */
	public PageImpl<StudentBasicDetails> getStudentDetails(String academicYear, PageRequest pageable) throws StudentException {
		log.info("Fetching student details list for academic year {}, page no {}, page size {}", academicYear, pageable.getPageNumber(),
				pageable.getPageSize());
		String query = "select A.* , B.class_id, D.class_name, C.reg_no "
				+ "from student_details A, student_class_details B, general_register C, classes D "
				+ "where A.stud_id = B.stud_id and A.stud_id = C.stud_id and B.class_id = D.class_id and B.academic_id = ? limit ? offset ?";
		log.info("query {}", query);
		List<StudentBasicDetails> studentBasicDetailsList = null;
		try {
			studentBasicDetailsList = jdbcTemplate.query(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, academicYear);
					ps.setInt(2, pageable.getPageSize());
					ps.setLong(3, pageable.getOffset());
				}
			}, (rs, rowNum)->mapStudentDetailsResult(rs));
		} catch (Exception ex) {
			log.error("Error while fetching students list for academic year {}, page no {}, page size {}", academicYear,
					pageable.getPageNumber(), pageable.getPageSize());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return new PageImpl<>(studentBasicDetailsList, pageable, studentBasicDetailsCount());
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentBasicDetails mapStudentDetailsResult(final ResultSet rs) throws SQLException {
		StudentClassDetails studentClassDetails = StudentClassDetails.builder().classDetails(
				ClassDetails.builder().classId(rs.getString("class_id")).className(rs.getString("class_name")).build())
				.build();
		GeneralRegister generalRegisterDetails = GeneralRegister.builder().regNo(rs.getInt("reg_no")).build();
		return StudentBasicDetails.builder().address(rs.getString("address")).adharNumber(rs.getString("adhar"))
				.alternateMobile(rs.getString("alternate_mobile"))
				.birthDate(DateUtils.getDate(rs.getDate("birth_date"))).caste(rs.getString("caste"))
				.email(rs.getString("email")).firstName(rs.getString("first_name")).gender(rs.getString("gender"))
				.generalRegisterDetails(Stream.of(generalRegisterDetails).collect(Collectors.toList()))
				.lastName(rs.getString("last_name")).middleName(rs.getString("middle_name"))
				.mobileNumber(rs.getString("mobile")).nationality(rs.getString("nationality"))
				.religion(rs.getString("religion"))
				.studentClassDetails(Stream.of(studentClassDetails).collect(Collectors.toList()))
				.studentId(rs.getString("stud_id")).transportOpted(rs.getBoolean("transport")).build();
	}
	
	/**
	 * 
	 * @return
	 */
	public int studentBasicDetailsCount() {
		return jdbcTemplate.queryForObject("SELECT count(*) FROM student_details", Integer.class);
	}

	/**
	 * It returns student details for studentId
	 * 
	 * @param studentId
	 * @throws StudentException
	 */
	public StudentBasicDetails getStudentDetails(String studentId) throws StudentException {
		log.info("fetching student details for {}", studentId);
		String query = "select * from student_details where stud_id = ?";
		log.info("query {}", query);
		StudentBasicDetails studentBasicDetails = null;
		try {
			List<StudentBasicDetails> studentDetailsList = jdbcTemplate.query(query,
					ps -> studentIdSetter(ps, studentId), (rs, rowNum) -> mapStudentDetails(rs));
			if (studentDetailsList.stream().findFirst().isPresent()) {
				studentBasicDetails = studentDetailsList.stream().findFirst().get();
			}
		} catch (Exception ex) {
			log.error("Error while fetching student details for {}", studentId);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return studentBasicDetails;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentBasicDetails mapStudentDetails(ResultSet rs) throws SQLException {
		log.info("Mapping result set to student details entity");
		return StudentBasicDetails.builder().address(rs.getString("address")).adharNumber(rs.getString("adhar"))
				.alternateMobile(rs.getString("alternate_mobile"))
				.birthDate(DateUtils.getDate(rs.getDate("birth_date"))).caste(rs.getString("caste"))
				.email(rs.getString("email")).firstName(rs.getString("first_name")).gender(rs.getString("gender"))
				.lastName(rs.getString("last_name")).middleName(rs.getString("middle_name"))
				.mobileNumber(rs.getString("mobile")).nationality(rs.getString("nationality"))
				.religion(rs.getString("religion")).studentId(rs.getString("stud_id"))
				.transportOpted(rs.getBoolean("transport")).build();
	}

	/**
	 * 
	 * @param ps
	 * @param studentId
	 * @throws SQLException
	 */
	private void studentIdSetter(PreparedStatement ps, String studentId) throws SQLException {
		ps.setString(1, studentId);
	}
}
