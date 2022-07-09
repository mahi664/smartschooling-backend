package com.example.demo.data.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.FeeTypes;
import com.example.demo.exception.StudentException;

@Repository
public class FeeTypesRepository {

	private final Logger log = LoggerFactory.getLogger(FeeTypesRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 * @throws StudentException 
	 */
	public List<FeeTypes> getFeeTypes() throws StudentException {
		log.info("Fetching fee types");
		String query = "SELECT * FROM FEE_TYPES";
		log.info("query {}", query);
		try {
			return jdbcTemplate.query(query, (rs, rowNum) -> feeTypesMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching fee types {}", ex.getMessage());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private FeeTypes feeTypesMapper(ResultSet rs) throws SQLException {
		return FeeTypes.builder().feeDescription(rs.getString("FEE_DISCRIPTION")).feeId(rs.getString("FEE_ID"))
				.feeName(rs.getString("FEE_NAME")).build();
	}
}
