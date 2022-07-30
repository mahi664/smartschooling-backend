package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
								.admissionDate(DateUtils.getDate(rs.getDate("admission_date")))
								.admissionStd(rs.getString("admission_std")).bookNo(rs.getInt("book_no"))
								.newSchool(rs.getString("new_school")).previousSchool(rs.getString("prev_school"))
								.regNo(rs.getInt("reg_no"))
								.schoolLeavingDate(DateUtils.getDate(rs.getDate("school_leaving_date")))
								.schoolLeavingReason(rs.getString("school_leaving_reason"))
								.studentId(rs.getString("stud_id")).academicYear(rs.getString("academic_id")).build();
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

	/**
	 * 
	 * @param generalRegisterList
	 * @throws StudentException
	 */
	public void addNewStudentInGeneralRegister(List<GeneralRegister> generalRegisterList) throws StudentException {
		log.info("Inserting new records in general register for {} students", generalRegisterList.size());
		String query = "insert into general_register(reg_no, book_no, stud_id, admission_std, admission_date, prev_school, academic_id) values(?,?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, generalRegisterList.get(i).getRegNo());
					ps.setInt(2, generalRegisterList.get(i).getBookNo());
					ps.setString(3, generalRegisterList.get(i).getStudentId());
					ps.setString(4, generalRegisterList.get(i).getAdmissionStd());
					ps.setDate(5, DateUtils.getSqlDate(generalRegisterList.get(i).getAdmissionDate()));
					ps.setString(6, generalRegisterList.get(i).getPreviousSchool());
					ps.setString(7, generalRegisterList.get(i).getAcademicYear());
				}

				@Override
				public int getBatchSize() {
					return generalRegisterList.size();
				}
			});
		} catch (Exception ex) {
			log.error("Error while inserting records in general register", ex);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * 
	 * @param genRegNos
	 * @return
	 * @throws StudentException 
	 */
	public List<GeneralRegister> getGeneralRegisterDetails(List<Integer> genRegNos) throws StudentException{
		log.info("Fetching general register details for {}", genRegNos.toString());
		String query = "select * from general_register where reg_no in ("+buildGeneralRegisterInClause(genRegNos) +")";
		log.info("query {}", query);
		List<GeneralRegister> generalRegisterList = null;
		try {
			generalRegisterList = jdbcTemplate.query(query, ps -> setFetchGenRegDetailsForRegNos(ps, genRegNos) , (rs, rowNum) -> generalRegisterMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching general register details", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return generalRegisterList;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private GeneralRegister generalRegisterMapper(ResultSet rs) throws SQLException {
		return GeneralRegister.builder().admissionDate(DateUtils.getDate(rs.getDate("admission_date")))
				.admissionStd(rs.getString("admission_std")).bookNo(rs.getInt("book_no"))
				.newSchool(rs.getString("new_school")).previousSchool(rs.getString("prev_school"))
				.regNo(rs.getInt("reg_no")).schoolLeavingDate(DateUtils.getDate(rs.getDate("school_leaving_date")))
				.schoolLeavingReason(rs.getString("school_leaving_reason")).studentId(rs.getString("stud_id"))
				.academicYear(rs.getString("academic_id")).build();
	}

	/**
	 * 
	 * @param ps
	 * @param genRegNos
	 * @throws SQLException
	 */
	private void setFetchGenRegDetailsForRegNos(PreparedStatement ps, List<Integer> genRegNos) throws SQLException {
		for (int i = 0; i < genRegNos.size(); i++) {
			ps.setInt(i + 1, genRegNos.get(i));
		}
	}

	/**
	 * 
	 * @param genRegNos
	 * @return
	 */
	private String buildGeneralRegisterInClause(List<Integer> genRegNos) {
		String inClause = "";
		for(int i=0; i<genRegNos.size()-1; i++) {
			inClause+="?,";
		}
		inClause+="?";
		return inClause;
	}
}
