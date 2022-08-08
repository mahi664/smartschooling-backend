package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.constant.ServiceConstants;
import com.example.demo.data.entity.Transaction;
import com.example.demo.exception.StudentException;
import com.example.demo.utils.DateUtils;

@Repository
public class TransactionRepository {

	private final Logger log = LoggerFactory.getLogger(TransactionRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addTransaction(Transaction transaction) throws StudentException {
		log.info("Adding new transaction data");
		String query = "insert into transactions values(?,?,?,?,?)";
		log.info("query {}", query);
		try {
			jdbcTemplate.update(query, ps -> setAddTransactionPS(ps, transaction));
		} catch (Exception ex) {
			log.error("Error while adding transaction details");
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * @param ps
	 * @param transaction
	 * @throws SQLException
	 */
	private void setAddTransactionPS(PreparedStatement ps, Transaction transaction) throws SQLException {
		log.info("setting add transaction prepared statement");
		ps.setString(1, transaction.getTransactionId());
		ps.setDouble(2, transaction.getAmount());
		ps.setDate(3, DateUtils.getSqlDate(transaction.getTransactionDate()));
		ps.setDate(4, DateUtils.getSqlDate(new Date()));
		ps.setString(5, ServiceConstants.ADMIN);
	}
	
	
}
