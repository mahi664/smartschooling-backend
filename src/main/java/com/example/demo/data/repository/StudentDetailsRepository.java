package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.ClassDetails;
import com.example.demo.data.entity.FeeReceivableDetails;
import com.example.demo.data.entity.FeesPaidAmnt;
import com.example.demo.data.entity.FeesTotalReceivableAmnt;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.data.entity.StudentBasicDetails;
import com.example.demo.data.entity.StudentClassDetails;
import com.example.demo.data.entity.StudentFeePaidDetails;
import com.example.demo.data.entity.StudentFeesAssignedDetails;
import com.example.demo.data.entity.StudentFeesPaidDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.service.dto.StudentListRequestDto;
import com.example.demo.utils.Constants;
import com.example.demo.utils.DateUtils;

/**
 * @author MAHI GHUGE
 *
 */
/**
 * @author MAHI GHUGE
 *
 */
/**
 * @author MAHI GHUGE
 *
 */
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
		String query = "INSERT INTO student_details VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
		String query = "SELECT COUNT(stud_id) as MAX_STUDENT_ID FROM student_details";
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
	 * @param studentListRequestDto 
	 * @return
	 * @throws StudentException
	 */
	public PageImpl<StudentBasicDetails> getStudentDetails(String academicYear, PageRequest pageable,
			StudentListRequestDto studentListRequestDto) throws StudentException {
		log.info("Fetching student details list for academic year {}, page no {}, page size {}", academicYear,
				pageable.getPageNumber(), pageable.getPageSize());
		String query = "select A.* , B.class_id, D.class_name, C.reg_no "
				+ "from student_details A, student_class_details B, general_register C, classes D "; 
		if(studentListRequestDto.getFilterDto()!=null && !CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getRouteIds())) {
			query += ",student_transport_details E ";
		}
		query += "where A.stud_id = B.stud_id and A.stud_id = C.stud_id and B.class_id = D.class_id and B.academic_id = ? "
				+ getFilterQuery(studentListRequestDto) + " " + "limit ? offset ?";
		log.info("query {}", query);
		List<StudentBasicDetails> studentBasicDetailsList = null;
		try {
			studentBasicDetailsList = jdbcTemplate.query(query,
					ps -> getStudentDetailsPS(ps, academicYear, studentListRequestDto, pageable),
					(rs, rowNum) -> mapStudentDetailsResult(rs));
		} catch (Exception ex) {
			log.error("Error while fetching students list for academic year {}, page no {}, page size {}", academicYear,
					pageable.getPageNumber(), pageable.getPageSize());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return new PageImpl<>(studentBasicDetailsList, pageable, studentBasicDetailsCount(academicYear, studentListRequestDto));
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
	 * @throws StudentException 
	 */
	public int studentBasicDetailsCount(String academicYear, StudentListRequestDto studentListRequestDto)
			throws StudentException {
		log.info("Fetching student details count for academic year {}", academicYear);
		String query = "select count(*) as studentsCount "
				+ "from student_details A, student_class_details B, general_register C, classes D ";
		if (studentListRequestDto.getFilterDto() != null
				&& !CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getRouteIds())) {
			query += ",student_transport_details E ";
		}
		query += "where A.stud_id = B.stud_id and A.stud_id = C.stud_id and B.class_id = D.class_id and B.academic_id = ? "
				+ getFilterQuery(studentListRequestDto) + " ";
		log.info("query {}", query);
		int studentBasicDetailsCount = 0;
		try {
			studentBasicDetailsCount = jdbcTemplate.query(query,
					ps -> getStudentDetailsPS(ps, academicYear, studentListRequestDto, null), new ResultSetExtractor<Integer>() {

						@Override
						public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
							int count = 0;
							while(rs.next()) {
								count = rs.getInt("studentsCount");
							}
							return count;
						}
					});
		} catch (Exception ex) {
			log.error("Error while fetching students count for academic year {}, page no {}, page size {}",
					academicYear);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return studentBasicDetailsCount;
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

	/**
	 * 
	 * @param studentBasicDetailsList
	 * @throws StudentException
	 */
	public void addStudentBasicDeails(List<StudentBasicDetails> studentBasicDetailsList) throws StudentException {
		log.info("adding student basic details for {} students", studentBasicDetailsList.size());
		String query = "INSERT INTO student_details VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, studentBasicDetailsList.get(i).getStudentId());
					ps.setString(2, studentBasicDetailsList.get(i).getFirstName());
					ps.setString(3, studentBasicDetailsList.get(i).getMiddleName());
					ps.setString(4, studentBasicDetailsList.get(i).getLastName());
					ps.setDate(5, DateUtils.getSqlDate(studentBasicDetailsList.get(i).getBirthDate()));
					ps.setString(6, studentBasicDetailsList.get(i).getGender());
					ps.setString(7, studentBasicDetailsList.get(i).getAdharNumber());
					ps.setString(8, studentBasicDetailsList.get(i).getEmail());
					ps.setString(9, studentBasicDetailsList.get(i).getMobileNumber());
					ps.setString(10, studentBasicDetailsList.get(i).getAlternateMobile());
					ps.setString(11, studentBasicDetailsList.get(i).getAddress());
					ps.setString(12, studentBasicDetailsList.get(i).getReligion());
					ps.setString(13, studentBasicDetailsList.get(i).getCaste());
					ps.setString(14, studentBasicDetailsList.get(i).isTransportOpted() ? Constants.YES : Constants.NO);
					ps.setString(15, studentBasicDetailsList.get(i).getNationality());
				}

				@Override
				public int getBatchSize() {
					return studentBasicDetailsList.size();
				}
			});
		} catch (Exception ex) {
			log.error("Error while inserting student details in batch", ex);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
	
	/**
	 * Get Filter Query
	 * 
	 * @param studentListRequestDto
	 * @return
	 */
	private String getFilterQuery(StudentListRequestDto studentListRequestDto) {
		log.info("Gettig filtered query for {}", studentListRequestDto.toString());
		String filteredQuery = "";
		if (studentListRequestDto != null && studentListRequestDto.getFilterDto() != null) {
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getClassIds())) {
				log.info("Filter for class id in {}", studentListRequestDto.getFilterDto().getClassIds().toString());
				filteredQuery += "and B.class_id in (";
				for (int i = 0; i < studentListRequestDto.getFilterDto().getClassIds().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getCastes())) {
				log.info("Filter for castes in {}", studentListRequestDto.getFilterDto().getCastes().toString());
				filteredQuery += "and A.caste in (";
				for (int i = 0; i < studentListRequestDto.getFilterDto().getCastes().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getReligions())) {
				log.info("Filter for religions in {}", studentListRequestDto.getFilterDto().getReligions().toString());
				filteredQuery += "and A.religion in (";
				for (int i = 0; i < studentListRequestDto.getFilterDto().getReligions().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if (!StringUtils.isEmpty(studentListRequestDto.getFilterDto().getGender())) {
				log.info("Filter for gender {}", studentListRequestDto.getFilterDto().getGender());
				filteredQuery += "and A.gender = ? ";
			}
			if (studentListRequestDto.getFilterDto().getTransportOpted() != null) {
				log.info("Filter for transport opted {}",
						studentListRequestDto.getFilterDto().getTransportOpted().booleanValue());
				filteredQuery += "and A.transport = ? ";
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getRouteIds())) {
				log.info("Filter for routes {}", studentListRequestDto.getFilterDto().getRouteIds().toString());
				filteredQuery += "and A.stud_id = E.stud_id and E.route_id in(";
				for (int i = 0; i < studentListRequestDto.getFilterDto().getRouteIds().size() - 1; i++) {
					filteredQuery += "?,";
				}
				filteredQuery += "?) ";
			}
			if(!StringUtils.isEmpty(studentListRequestDto.getFilterDto().getQuickSearchText())) {
				log.info("Filter for quick search for {}", studentListRequestDto.getFilterDto().getQuickSearchText());
				filteredQuery += "and (A.first_name like (?) or A.middle_name like(?) or A.last_name like(?) or"
						+ " A.address like(?)) ";
			}
		}
		if (CollectionUtils.isEmpty(studentListRequestDto.getSortOrders())) {
			log.info("Setting order by reg no");
			filteredQuery += "order by reg_no";
		} else {
			log.info("sorting on {}", studentListRequestDto.getSortOrders().toString());
			filteredQuery += "order by ";
			int i = 0;
			for (; i < studentListRequestDto.getSortOrders().size() - 1; i++) {
				filteredQuery += studentListRequestDto.getSortOrders().get(i).getField() + ", ";
			}
			filteredQuery += studentListRequestDto.getSortOrders().get(i).getField() + " ";
		}
		return filteredQuery;
	}
	
	/**
	 * Prepared Statement Setter for Get Student Details
	 * 
	 * @param ps
	 * @param academicYear
	 * @param studentListRequestDto
	 * @param pageable
	 * @throws SQLException
	 */
	private void getStudentDetailsPS(PreparedStatement ps, String academicYear,
			StudentListRequestDto studentListRequestDto, PageRequest pageable) throws SQLException {
		int indx = 1;
		ps.setString(indx++, academicYear);
		if (studentListRequestDto != null && studentListRequestDto.getFilterDto() != null) {
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getClassIds())) {
				for (String classId : studentListRequestDto.getFilterDto().getClassIds()) {
					ps.setString(indx++, classId);
				}
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getCastes())) {
				for (String caste : studentListRequestDto.getFilterDto().getCastes()) {
					ps.setString(indx++, caste);
				}
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getReligions())) {
				for (String religion : studentListRequestDto.getFilterDto().getReligions()) {
					ps.setString(indx++, religion);
				}
			}
			if (!StringUtils.isEmpty(studentListRequestDto.getFilterDto().getGender())) {
				ps.setString(indx++, studentListRequestDto.getFilterDto().getGender());
			}
			if (studentListRequestDto.getFilterDto().getTransportOpted()!=null) {
				ps.setString(indx++, studentListRequestDto.getFilterDto().getTransportOpted().booleanValue() ? "Y" : "N");
			}
			if (!CollectionUtils.isEmpty(studentListRequestDto.getFilterDto().getRouteIds())) {
				for (String route : studentListRequestDto.getFilterDto().getRouteIds()) {
					ps.setString(indx++, route);
				}
			}
			if(!StringUtils.isEmpty(studentListRequestDto.getFilterDto().getQuickSearchText())) {
				ps.setString(indx++, "%"+studentListRequestDto.getFilterDto().getQuickSearchText()+"%");
				ps.setString(indx++, "%"+studentListRequestDto.getFilterDto().getQuickSearchText()+"%");
				ps.setString(indx++, "%"+studentListRequestDto.getFilterDto().getQuickSearchText()+"%");
				ps.setString(indx++, "%"+studentListRequestDto.getFilterDto().getQuickSearchText()+"%");
			}
		}
		if (pageable != null) {
			ps.setInt(indx++, pageable.getPageSize());
			ps.setLong(indx, pageable.getOffset());
		}
	}
	
	/**
	 * Get Fee Receivables
	 * @param pageRequest 
	 * @param quickSearch 
	 * 
	 * @return
	 * @throws StudentException
	 */
	public PageImpl<FeeReceivableDetails> getFeeReceivables(String quickSearch, Pageable pageRequest)
			throws StudentException {
		log.info("Getting Fee receivable details");
		String query = "select D.stud_id, F.reg_no, D.first_name, D.middle_name, D.last_name, D.mobile, D.address, sum(A.amount) as total_fee "
				+ "from fee_details A, student_fees_details B, fee_types C, student_details D, academic_details E, general_register F "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and B.stud_id = D.stud_id and D.stud_id = F.stud_id "
				+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id=E.academic_id) or "
				+ "A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id=? and A.route_id=?)) "
				+ "and B.academic_id = E.academic_id ";
		if (!StringUtils.isEmpty(quickSearch)) {
			query += "and (D.first_name like(?) or D.last_name like(?)) ";
		}
		query += "group by D.stud_id order by total_fee desc limit ? offset ?";
		log.info("query {}", query);
		List<FeeReceivableDetails> feeReceivableDetailsList;
		try {
			feeReceivableDetailsList = jdbcTemplate.query(query,
					ps -> setFeeReceivablesPS(ps, quickSearch, pageRequest),
					(rs, rowNum) -> getFeeReceivableRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting fee receivable details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return new PageImpl<>(feeReceivableDetailsList, pageRequest, getFeeReceivableCount(quickSearch));
	}

	/**
	 * @param quickSearch
	 * @return
	 * @throws StudentException
	 */
	private long getFeeReceivableCount(String quickSearch) throws StudentException {
		log.info("Getting Fee receivable count");
		String query = "select D.stud_id, F.reg_no, D.first_name, D.middle_name, D.last_name, D.mobile, D.address, sum(A.amount) as total_fee "
				+ "from fee_details A, student_fees_details B, fee_types C, student_details D, academic_details E, general_register F "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and B.stud_id = D.stud_id and D.stud_id = F.stud_id "
				+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id=E.academic_id) or "
				+ "A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id=? and A.route_id=?)) "
				+ "and B.academic_id = E.academic_id ";
		if (!StringUtils.isEmpty(quickSearch)) {
			query += "and (D.first_name like(?) or D.last_name like(?)) ";
		}
		query += "group by D.stud_id";
		log.info("query {}", query);
		List<FeeReceivableDetails> feeReceivableDetailsList;
		try {
			feeReceivableDetailsList = jdbcTemplate.query(query, ps -> setFeeReceivablesCountPS(ps, quickSearch),
					(rs, rowNum) -> getFeeReceivableRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting fee receivable details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}

		return new Long(feeReceivableDetailsList.size()).longValue();
	}

	/**
	 * @param ps
	 * @param quickSearch
	 * @throws SQLException
	 */
	private void setFeeReceivablesCountPS(PreparedStatement ps, String quickSearch) throws SQLException {
		int indx = 1;
		ps.setString(indx++, Constants.BLANK_STRING);
		ps.setString(indx++, Constants.BLANK_STRING);
		if (!StringUtils.isEmpty(quickSearch)) {
			ps.setString(indx++, "%" + quickSearch + "%");
			ps.setString(indx++, "%" + quickSearch + "%");
		}
	}

	/**
	 * Map Fee Receivables Result Set
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private FeeReceivableDetails getFeeReceivableRowMapper(ResultSet rs) throws SQLException {
		return FeeReceivableDetails.builder().address(rs.getString("address")).firstName(rs.getString("first_name"))
				.genRegNo(rs.getInt("reg_no")).lastName(rs.getString("last_name"))
				.middleName(rs.getString("middle_name")).mobileNumber(rs.getString("mobile"))
				.studentId(rs.getString("stud_id")).totalFee(rs.getDouble("total_fee")).build();

	}

	/**
	 * Set Prepare statement for Fee Receivables
	 * 
	 * @param ps
	 * @param pageRequest 
	 * @param quickSearch 
	 * @throws SQLException
	 */
	private void setFeeReceivablesPS(PreparedStatement ps, String quickSearch, Pageable pageRequest) throws SQLException {
		int indx = 1;
		ps.setString(indx++, Constants.BLANK_STRING);
		ps.setString(indx++, Constants.BLANK_STRING);
		if(!StringUtils.isEmpty(quickSearch)) {
			ps.setString(indx++, "%"+quickSearch+"%");
			ps.setString(indx++, "%"+quickSearch+"%");
		}
		ps.setInt(indx++, pageRequest.getPageSize());
		ps.setLong(indx++, pageRequest.getOffset());
	}
	
	/**
	 * Get Student Fees Paid Amount
	 * 
	 * @return
	 * @throws StudentException
	 */
	public List<StudentFeePaidDetails> getStudentsFeesPaidAmount() throws StudentException {
		log.info("get students fees paid amount");
		String query = "select A.stud_id, sum(B.amount) as fees_paid "
				+ "from students_fees_collection_transaction A, students_fees_collection_transaction_details B "
				+ "where A.collection_id = B.collection_id group by A.stud_id";
		log.info("query {}", query);
		List<StudentFeePaidDetails> studentFeePaidDetailsList;
		try {
			studentFeePaidDetailsList = jdbcTemplate.query(query, (rs, rowNum) -> getStudentFeesPaidDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting student fees paid details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return studentFeePaidDetailsList;
	}

	/**
	 * Get Student Fees Paid details mapper
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentFeePaidDetails getStudentFeesPaidDetailsMapper(ResultSet rs) throws SQLException {
		return StudentFeePaidDetails.builder().studentId(rs.getString("stud_id")).feesPaid(rs.getDouble("fees_paid"))
				.build();
	}

	/**
	 * Get total paid amount
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public FeesPaidAmnt getTotalPaidAmount() throws StudentException {
		log.info("Getting total fees paid amount");
		String query = "select sum(B.amount) as fees_paid "
				+ "from students_fees_collection_transaction A, students_fees_collection_transaction_details B "
				+ "where A.collection_id = B.collection_id";
		log.info("query {}", query);
		List<FeesPaidAmnt> feesPaidAmntList;
		try {
			feesPaidAmntList = jdbcTemplate.query(query, (rs, rowNum) -> getTotalPaidAmntRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting total fees paid amount");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		Optional<FeesPaidAmnt> feePaidAmntOptional = feesPaidAmntList.stream().findFirst();
		if (!feePaidAmntOptional.isPresent()) {
			log.error("Failed to get total receivable amount");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR);
		}
		return feePaidAmntOptional.get();
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private FeesPaidAmnt getTotalPaidAmntRowMapper(ResultSet rs) throws SQLException {
		log.info("Mapping total paid amount result set to entity");
		return FeesPaidAmnt.builder().totalAmnt(rs.getDouble("fees_paid")).build();
	}

	/**
	 * Get Total receivable amount
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public FeesTotalReceivableAmnt getTotalReceivableAmount() throws StudentException {
		log.info("Getting total receivable amount");
		String query = "select sum(A.amount) as total_fee "
				+ "from fee_details A, student_fees_details B, fee_types C, student_details D, academic_details E, general_register F "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id and B.stud_id = D.stud_id and D.stud_id = F.stud_id "
				+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id=E.academic_id) or A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id=? and A.route_id=?)) "
				+ "and B.academic_id = E.academic_id";
		log.info("query {}", query);
		List<FeesTotalReceivableAmnt> feesTotalReceivableAmnt;
		try {
			feesTotalReceivableAmnt = jdbcTemplate.query(query, ps -> setTotalReceivableAmntPS(ps),
					(rs, rowNum) -> getTotalReceivableAmntRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting total receivable amount");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		Optional<FeesTotalReceivableAmnt> feeTotalReceivableAmntOptional = feesTotalReceivableAmnt.stream().findFirst();
		if (!feeTotalReceivableAmntOptional.isPresent()) {
			log.error("Failed to get total receivable amount");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR);
		}
		return feeTotalReceivableAmntOptional.get();
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private FeesTotalReceivableAmnt getTotalReceivableAmntRowMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to fee total receivables entity");
		return FeesTotalReceivableAmnt.builder().totalAmnt(rs.getDouble("total_fee")).build();
	}

	
	/**
	 * @param ps
	 * @throws SQLException
	 */
	private void setTotalReceivableAmntPS(PreparedStatement ps) throws SQLException {
		log.info("Setting total receivable amount prepared statement");
		ps.setString(1, Constants.BLANK_STRING);
		ps.setString(2, Constants.BLANK_STRING);
	}

	/**
	 * @param studentId
	 * @return
	 * @throws StudentException 
	 */
	public List<StudentFeesAssignedDetails> getStudentFeesAssignedDetails(String studentId) throws StudentException {
		log.info("Fetching student fees assigned details for student id {}", studentId);
		String query = "select B.academic_id, B.fee_id, C.fee_name, A.amount, B.last_update_time, B.last_user "
				+ "from fee_details A, student_fees_details B, fee_types C, academic_details D "
				+ "where A.fee_id = B.fee_id and A.fee_id=C.fee_id and B.fee_id=C.fee_id "
				+ "and (A.class_id in (select distinct(class_id) from student_class_details where stud_id=B.stud_id and academic_id = D.academic_id) or "
				+ "A.route_id in (select distinct(route_id) from student_transport_details where stud_id=B.stud_id) or (A.class_id= ? and A.route_id= ?)) "
				+ "and B.academic_id=D.academic_id and B.stud_id = ? order by B.academic_id desc";
		log.info("query {}", query);
		List<StudentFeesAssignedDetails> studentFeesAssignedDetailsList;
		try {
			studentFeesAssignedDetailsList = jdbcTemplate.query(query, ps -> setFeesAssignedDetailsPS(ps, studentId),
					(rs, rowNum) -> getFeesAssignedDetailsRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting student fees assigned details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return studentFeesAssignedDetailsList;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentFeesAssignedDetails getFeesAssignedDetailsRowMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to student fees assigned details entity");
		return StudentFeesAssignedDetails.builder().academicYear(rs.getString("academic_id"))
				.amount(rs.getDouble("amount")).feeId(rs.getString("fee_id")).feeName(rs.getString("fee_name"))
				.lastUpdateTime(DateUtils.getDate(rs.getDate("last_update_time"))).lastUser(rs.getString("last_user"))
				.build();
	}

	/**
	 * @param ps
	 * @param studentId
	 * @throws SQLException
	 */
	private void setFeesAssignedDetailsPS(PreparedStatement ps, String studentId) throws SQLException {
		log.info("Setting fees assigned details prepared statement");
		ps.setString(1, Constants.BLANK_STRING);
		ps.setString(2, Constants.BLANK_STRING);
		ps.setString(3, studentId);
	}

	/**
	 * @param studentId
	 * @return
	 * @throws StudentException
	 */
	public List<StudentFeesPaidDetails> getStudentFeesPaidDetails(String studentId) throws StudentException {
		log.info("Fetching students fees paid amount details for student id {}", studentId);
		String query = "select A.collection_id,A.account_id,C.account_name,A.last_update_time,A.last_user, B.academic_id, B.amount, B.fee_id , D.fee_name "
				+ "from students_fees_collection_transaction A, students_fees_collection_transaction_details B, accounts C, fee_types D "
				+ "where A.collection_id = B.collection_id and A.account_id=C.account_id and B.fee_id = D.fee_id and A.stud_id=?";
		log.info("query {}", query);
		List<StudentFeesPaidDetails> feesPaidDetailsList;
		try {
			feesPaidDetailsList = jdbcTemplate.query(query, ps -> setFeesPaidDetailsPS(ps, studentId),
					(rs, rowNum) -> getFeesPaidDetailsRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting student fees paid details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return feesPaidDetailsList;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private StudentFeesPaidDetails getFeesPaidDetailsRowMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to Fees Paid details entity");
		return StudentFeesPaidDetails.builder().academicId(rs.getString("academic_id"))
				.accountId(rs.getString("account_id")).accountName(rs.getString("account_name"))
				.amount(rs.getDouble("amount")).collectionId(rs.getString("collection_id"))
				.feeId(rs.getString("fee_id")).feeName(rs.getString("fee_name"))
				.lastUpdateTime(DateUtils.getDate(rs.getDate("last_update_time"))).lastUser(rs.getString("last_user"))
				.build();
	}

	/**
	 * @param ps
	 * @param studentId
	 * @throws SQLException
	 */
	private void setFeesPaidDetailsPS(PreparedStatement ps, String studentId) throws SQLException {
		log.info("Setting fees paid details prepared statement");
		ps.setString(1, studentId);
	}
}
