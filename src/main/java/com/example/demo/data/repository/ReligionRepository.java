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
import com.example.demo.data.entity.Religion;
import com.example.demo.exception.StudentException;

@Repository
public class ReligionRepository {

	private final Logger log = LoggerFactory.getLogger(ReligionRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @return
	 * @throws StudentException
	 */
	public List<Religion> getReligions() throws StudentException {
		log.info("Getting religion details");
		String query = "select * from religion";
		log.info("query {}", query);
		List<Religion> religions;
		try {
			religions = jdbcTemplate.query(query, (rs, rowNum) -> getReligionMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching religion details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return religions;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws StudentException
	 */
	public Religion getReligionById(int id) throws StudentException {
		log.info("Getting religion details for id {}", id);
		String query = "select * from religion where id = ?";
		log.info("query {}", query);
		List<Religion> religions = null;
		try {
			religions = jdbcTemplate.query(query, ps -> getReligionByIdPs(ps, id),
					(rs, rowNum) -> getReligionMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching religion details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		if (religions == null || religions.stream().findFirst().isPresent()) {
			throw new StudentException(ErrorDetails.RELIGION_NOT_FOUND);
		}
		return religions.stream().findFirst().get();
	}
	
	public Religion getReligionByCode(String code) throws StudentException {
		log.info("Getting religion details for id {}", code);
		String query = "select * from religion where code = ?";
		log.info("query {}", query);
		List<Religion> religions = null;
		try {
			religions = jdbcTemplate.query(query, ps -> getReligionByCodePs(ps, code),
					(rs, rowNum) -> getReligionMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching religion details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		if (religions == null || !religions.stream().findFirst().isPresent()) {
			throw new StudentException(ErrorDetails.RELIGION_NOT_FOUND);
		}
		return religions.stream().findFirst().get();
	}

	/**
	 * 
	 * @param ps
	 * @param code
	 * @throws SQLException
	 */
	private void getReligionByCodePs(PreparedStatement ps, String code) throws SQLException {
		ps.setString(1, code);
	}

	/**
	 * 
	 * @param ps
	 * @param id
	 * @throws SQLException
	 */
	private void getReligionByIdPs(PreparedStatement ps, int id) throws SQLException {
		ps.setInt(1, id);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Religion getReligionMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to religion entity");
		return Religion.builder().id(rs.getInt("id")).code(rs.getString("code"))
				.description(rs.getString("description")).build();
	}
}
