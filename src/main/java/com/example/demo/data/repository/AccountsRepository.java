package com.example.demo.data.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.data.entity.Accounts;
import com.example.demo.exception.StudentException;

@Repository
public class AccountsRepository {

	private final Logger log = LoggerFactory.getLogger(AccountsRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @param accountId
	 * @return
	 * @throws StudentException
	 */
	public Accounts getAccounts(String accountId) throws StudentException {
		log.info("Fetching accounts details for account id {}", accountId);
		String query = "select * from accounts where account_id = ?";
		log.info("query {}", query);
		List<Accounts> accounts;
		try {
			accounts = jdbcTemplate.query(query, ps -> setGetAccountsPS(ps, accountId),
					(rs, rowNum) -> getAccountsRowMapper(rs));
		} catch (Exception ex) {
			log.error("Error while fetching accounts details for account id {}", accountId);
			throw new StudentException(ErrorDetails.INTERNAL_SERVER_ERROR, ex);
		}
		Optional<Accounts> accountsOptional = accounts.stream().findFirst();
		if (!accountsOptional.isPresent()) {
			log.error("accounts details not found");
			throw new StudentException(ErrorDetails.INVALID_ACCOUNT_NUMBER);
		}
		return accountsOptional.get();
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Accounts getAccountsRowMapper(ResultSet rs) throws SQLException {
		log.info("mapping result set to Account entity");
		return Accounts.builder().accountId(rs.getString("account_id")).accountName(rs.getString("account_name"))
				.bankAccountNumber(rs.getString("bank_account_number")).bankName(rs.getString("bank_name")).build();
	}

	/**
	 * @param ps
	 * @param accountId
	 * @throws SQLException
	 */
	private void setGetAccountsPS(PreparedStatement ps, String accountId) throws SQLException {
		log.info("Setting get accounts prepared statement");
		ps.setString(1, accountId);
	}
}
