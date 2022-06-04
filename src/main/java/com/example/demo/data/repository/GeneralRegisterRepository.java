package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.GeneralRegister;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class GeneralRegisterRepository {

	private final Logger log = LoggerFactory.getLogger(GeneralRegisterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param generalRegister
	 * @throws StudentException
	 */
	public void addNewStudentInGeneralRegister(GeneralRegister generalRegister) throws StudentException {
		log.info("Inserting new record in general register for {}", generalRegister.toString());
		String query = "insert into general_register(reg_no, book_no, stud_id, admission_std, admission_date, prev_school, academic_id) values(?,?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, generalRegister.getRegNo());
					ps.setInt(2, generalRegister.getBookNo());
					ps.setString(3, generalRegister.getStudentId());
					ps.setString(4, generalRegister.getAdmissionStd());
					ps.setDate(5, DateUtils.getSqlDate(generalRegister.getAdmissionDate()));
					ps.setString(6, generalRegister.getPreviousSchool());
					ps.setString(7, generalRegister.getAcademicYear());
				}
			});
		} catch (Exception ex) {
			log.error("Error while inserting data in general register {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * 
	 * @param genRegNo
	 * @return
	 * @throws StudentException
	 */
	public GeneralRegister getGeneraRegisterDetails(int genRegNo) throws StudentException {
		log.info("Fetching general register details for reg no {}", genRegNo);
		String query = "select * from general_register where reg_no = ?";
		log.info("query {}", query);
		GeneralRegister generalRegister = null;
		try {
			generalRegister = jdbcTemplate.query(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, genRegNo);
				}
			}, new ResultSetExtractor<GeneralRegister>() {

				@Override
				public GeneralRegister extractData(ResultSet rs) throws SQLException, DataAccessException {
					GeneralRegister generalRegister = null;
					while (rs.next()) {
						generalRegister = GeneralRegister.builder()
								.admissionDate(DateUtils.getDate(rs.getDate("ADMISSION_DATE")))
								.admissionStd(rs.getString("ADMISSION_STD")).bookNo(rs.getInt("BOOK_NO"))
								.newSchool(rs.getString("NEW_SCHOOL")).previousSchool(rs.getString("PREV_SCHOOL"))
								.regNo(rs.getInt("REG_NO"))
								.schoolLeavingDate(DateUtils.getDate(rs.getDate("SCHOOL_LEAVING_DATE")))
								.schoolLeavingReason(rs.getString("SCHOOL_LEAVING_REASON"))
								.studentId(rs.getString("STUD_ID")).academicYear(rs.getString("ACADEMIC_ID")).build();
					}
					return generalRegister;
				}
			});
		} catch (Exception ex) {
			log.error("Error in retrieving general register details for reg no {} ", genRegNo);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return generalRegister;
	}
}