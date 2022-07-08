package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.AcademicDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class AcademicDetailsRepository {

	private final Logger log = LoggerFactory.getLogger(AcademicDetailsRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Get Academic Details
	 * 
	 * @return
	 * @throws StudentException
	 */
	public List<AcademicDetails> getAcademicDetails() throws StudentException {
		log.info("Fetching Academic Details");
		String query = "select * from academic_details";
		log.info("query {}", query);
		List<AcademicDetails> academicDetails;
		try {
			academicDetails = jdbcTemplate.query(query, (rs, rowNum) -> getAcademicDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error While fetching academic details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return academicDetails;
	}

	/**
	 * Get Academic details by Academic Id
	 * 
	 * @param academicId
	 * @return
	 * @throws StudentException
	 */
	public AcademicDetails getAcademicDetailById(String academicId) throws StudentException {
		log.info("Fetching Academic Details for {}", academicId);
		String query = "select * from academic_details where academic_id = ?";
		log.info("query {}", query);
		List<AcademicDetails> academicDetails;
		try {
			academicDetails = jdbcTemplate.query(query, ps -> getAcademicDetailsByIdPS(ps, academicId),
					(rs, rowNum) -> getAcademicDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error While fetching academic details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		if (academicDetails == null || !academicDetails.stream().findFirst().isPresent()) {
			log.info("Academic details not found for {}", academicId);
			throw new StudentException(ErrorDetails.ACADEMIC_DETAILS_NOT_FOUND);
		}
		return academicDetails.stream().findFirst().get();
	}

	/**
	 * Set prepared statement for get academic details by id
	 * 
	 * @param ps
	 * @param academicId
	 * @throws SQLException
	 */
	private void getAcademicDetailsByIdPS(PreparedStatement ps, String academicId) throws SQLException {
		log.info("Prepared statement setter for get academic details by id");
		ps.setString(1, academicId);
	}

	/**
	 * Map Result set to entity
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private AcademicDetails getAcademicDetailsMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to academic details entity");
		return AcademicDetails.builder().academicId(rs.getString("ACADEMIC_ID"))
				.academicYear(rs.getString("ACADEMIC_YEAR")).displayName(rs.getString("DISPLAY_NAME"))
				.academicStartDate(DateUtils.getDate(rs.getDate("ACADEMIC_START_DATE")))
				.academicEndDate(DateUtils.getDate(rs.getDate("ACADEMIC_END_DATE")))
				.lastUpdateTime(DateUtils.getDate(rs.getDate("LAST_UPDATE_TIME")))
				.lastUpdateUser(rs.getString("LAST_UPDATE_USER")).build();
	}
}
