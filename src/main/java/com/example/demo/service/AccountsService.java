package com.example.demo.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.example.demo.bo.AccountsDetailsBO;

@Service
public class AccountsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<AccountsDetailsBO> getAccountDetails() {
		try {
			String query = "SELECT * FROM accounts";
			return jdbcTemplate.query(query, new ResultSetExtractor<List<AccountsDetailsBO>>() {

				@Override
				public List<AccountsDetailsBO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<AccountsDetailsBO> accountsDetailBOs = new ArrayList<>();
					while(rs.next()) {
						AccountsDetailsBO accountsDetailsBO = new AccountsDetailsBO();
						accountsDetailsBO.setAccountId(rs.getString("account_id"));
						accountsDetailsBO.setAccountName(rs.getString("account_name"));
						accountsDetailsBO.setBankName(rs.getString("bank_name"));
						accountsDetailsBO.setBankAccountNumber(rs.getString("bank_account_number"));
						accountsDetailBOs.add(accountsDetailsBO);
					}
					return accountsDetailBOs;
				}
			});
		} catch (Exception e) {
			System.out.println("Error while fetching accounts details");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public AccountsDetailsBO addNewAccountDetails(AccountsDetailsBO accountsDetailsBO) {
		try {
			String query = "INSERT INTO accounts VALUES(?,?,?,?)";
			int res = jdbcTemplate.update(query, new PreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					accountsDetailsBO.setAccountId(accountsDetailsBO.getAccountName());
					ps.setString(1, accountsDetailsBO.getAccountId());
					ps.setString(2, accountsDetailsBO.getAccountName());
					ps.setString(3, accountsDetailsBO.getBankName());
					ps.setString(4, accountsDetailsBO.getBankAccountNumber());
				}
			});
			if(res<=0) {
				System.out.println("Problem in inserting account details");
				return null;
			}
			return accountsDetailsBO;
		} catch (Exception e) {
			System.out.println("Error in adding new account details");
			e.printStackTrace();
		}
		return null;
	}
}
