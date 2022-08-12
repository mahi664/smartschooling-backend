package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.RefTableDetails;
import com.example.demo.data.entity.StudentFeeCollectionTransaction;
import com.example.demo.data.entity.StudentFeeCollectionTransactionDetails;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class StudentFeeCollectionRepository {

	private final Logger log = LoggerFactory.getLogger(StudentFeeCollectionRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @return
	 * @throws StudentException
	 */
	public List<RefTableDetails> getRefTableDetails() throws StudentException {
		log.info("Getting Reference Table details");
		String query = "select * from ref_table_types";
		log.info("query {}", query);
		List<RefTableDetails> refTableDetails;
		try {
			refTableDetails = jdbcTemplate.query(query, (rs, rowNum) -> getRefTableDetailsRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while getting reference table details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		return refTableDetails;
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private RefTableDetails getRefTableDetailsRowMapper(ResultSet rs) throws SQLException {
		return RefTableDetails.builder().refTableName(rs.getString("ref_table_name"))
				.refTableType(rs.getString("ref_table_type")).build();
	}

	/**
	 * @param studentFeeCollectionTransaction
	 * @throws StudentException
	 */
	public void addStudentFeeCollection(StudentFeeCollectionTransaction studentFeeCollectionTransaction)
			throws StudentException {
		log.info("Adding Student fee collections for {}", studentFeeCollectionTransaction.getStudentId());
		String query = "insert into students_fees_collection_transaction values(?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, ps -> setAddStudentFeeCollectionPS(ps, studentFeeCollectionTransaction));
		} catch (Exception ex) {
			log.error("Error while adding student fee collection for {}",
					studentFeeCollectionTransaction.getStudentId());
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * @param ps
	 * @param studentFeeCollectionTransaction
	 * @throws SQLException
	 */
	private void setAddStudentFeeCollectionPS(PreparedStatement ps,
			StudentFeeCollectionTransaction studentFeeCollectionTransaction) throws SQLException {
		log.info("setting add student fee collection prepared statement");
		ps.setString(1, studentFeeCollectionTransaction.getCollectionId());
		ps.setString(2, studentFeeCollectionTransaction.getStudentId());
		ps.setDate(3, DateUtils.getSqlDate(studentFeeCollectionTransaction.getTransactionDate()));
		ps.setString(4, studentFeeCollectionTransaction.getAccountId());
		ps.setDate(5, DateUtils.getSqlDate(new Date()));
		ps.setString(6, ServiceConstants.ADMIN);
	}
	
	/**
	 * @return
	 * @throws StudentException
	 */
	public int getMaxTrxnDetId() throws StudentException {
		log.info("getting max trxn detail id");
		String query = "select count(trans_det_id) as max_trans_det_id from students_fees_collection_transaction_details";
		log.info("query {}", query);
		int maxTrnxDetId;
		try {
			maxTrnxDetId = jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception ex) {
			log.error("Error while getting max trxn det id");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR);
		}
		return maxTrnxDetId;
	}

	/**
	 * @param studentFeeCollectionTransactionDetails
	 * @throws StudentException
	 */
	public void addStudentFeeCollectionDetails(
			List<StudentFeeCollectionTransactionDetails> studentFeeCollectionTransactionDetails)
			throws StudentException {
		log.info("Adding student fee collection transaction details");
		String query = "insert into students_fees_collection_transaction_details values(?,?,?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, studentFeeCollectionTransactionDetails.get(i).getTrxnDetId());
					ps.setString(2, studentFeeCollectionTransactionDetails.get(i).getCollectionId());
					ps.setString(3, studentFeeCollectionTransactionDetails.get(i).getFeeId());
					ps.setString(4, studentFeeCollectionTransactionDetails.get(i).getAcademicId());
					ps.setDouble(5, studentFeeCollectionTransactionDetails.get(i).getAmount());
					ps.setDate(6, DateUtils.getSqlDate(new Date()));
					ps.setString(7, ServiceConstants.ADMIN);
				}

				@Override
				public int getBatchSize() {
					return studentFeeCollectionTransactionDetails.size();
				}
			});
		} catch (Exception ex) {
			log.error("Error while adding student fee collection transaction details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
