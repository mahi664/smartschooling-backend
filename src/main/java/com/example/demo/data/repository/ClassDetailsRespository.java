package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.ClassDetails;
import com.example.demo.exception.StudentException;

@Repository
public class ClassDetailsRespository {

	private final Logger log = LoggerFactory.getLogger(GeneralRegisterRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param classId
	 * @return
	 * @throws StudentException
	 */
	public ClassDetails getClassDetails(String classId) throws StudentException {
		log.info("Fetching class details for {}", classId);
		String query = "select * from classes where class_id = ?";
		log.info("query {}", query);
		ClassDetails classDetails = null;
		try {
			classDetails = jdbcTemplate.query(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, classId);
				}
			}, new ResultSetExtractor<ClassDetails>() {

				@Override
				public ClassDetails extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClassDetails classDetails = null;
					while (rs.next()) {
						classDetails = ClassDetails.builder().classId(rs.getString("class_id"))
								.className(rs.getString("class_name")).build();
					}
					return classDetails;
				}
			});
		} catch (Exception ex) {
			log.error("Error while fetching class details for {}", classId);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return classDetails;
	}

	/**
	 * 
	 * @return
	 * @throws StudentException
	 */
	public List<ClassDetails> getClassDetails() throws StudentException {
		log.info("Fetching classes details");
		String query = "select * from classes";
		log.info("query {}", query);
		List<ClassDetails> classesDetailsList;
		try {
			classesDetailsList = jdbcTemplate.query(query, (rs, rowNum) -> getClassDetailsRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching classes details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return classesDetailsList;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	private ClassDetails getClassDetailsRowMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to classes details entity");
		return ClassDetails.builder().classId(rs.getString("class_id")).className(rs.getString("class_name")).build();
	}
}
