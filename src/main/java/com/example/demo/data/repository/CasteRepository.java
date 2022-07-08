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
import com.example.demo.data.entity.Caste;
import com.example.demo.exception.StudentException;

@Repository
public class CasteRepository {

	private final Logger log = LoggerFactory.getLogger(CasteRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Fetch Caste Details
	 * 
	 * @return
	 * @throws StudentException
	 */
	public List<Caste> getCasteDetails() throws StudentException {
		log.info("fetching caste details");
		String query = "select * from caste";
		log.info("query {}", query);
		List<Caste> casteDetails;
		try {
			casteDetails = jdbcTemplate.query(query, (rs, rowNum) -> getCasteDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching caste details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return casteDetails;
	}

	/**
	 * Fetch Caste Details for caste
	 * 
	 * @param caste
	 * @return
	 * @throws StudentException
	 */
	public Caste getCasteDetailsByCaste(String caste) throws StudentException {
		log.info("fetching caste details for {}", caste);
		String query = "select * from caste where caste = ?";
		log.info("query {}", query);
		List<Caste> casteDetails;
		try {
			casteDetails = jdbcTemplate.query(query, ps -> getCastDetailsByCastPS(ps, caste),
					(rs, rowNum) -> getCasteDetailsMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching caste details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		if (casteDetails == null || !casteDetails.stream().findFirst().isPresent()) {
			log.info("caste details not found");
			throw new StudentException(ErrorDetails.CASTE_NOT_FOUND);
		}

		return casteDetails.stream().findFirst().get();
	}

	/**
	 * Set prepare statement for fetch caste details by caste
	 * 
	 * @param ps
	 * @param caste
	 * @throws SQLException
	 */
	private void getCastDetailsByCastPS(PreparedStatement ps, String caste) throws SQLException {
		ps.setString(1, caste);
	}

	/**
	 * Map Result set to entity
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Caste getCasteDetailsMapper(ResultSet rs) throws SQLException {
		log.info("Mapping result set to Caste entity");
		return Caste.builder().id(rs.getInt("id")).caste(rs.getString("caste")).build();
	}
}
