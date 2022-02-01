package com.example.demo.bo;

public class TransactionDetailsBO {
	private AccountsDetailsBO accountsDetailsBO;
	private char transactionType;
	private String refId;
	private String refTableType;
	public AccountsDetailsBO getAccountsDetailsBO() {
		return accountsDetailsBO;
	}
	public void setAccountsDetailsBO(AccountsDetailsBO accountsDetailsBO) {
		this.accountsDetailsBO = accountsDetailsBO;
	}
	public char getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(char transactionType) {
		this.transactionType = transactionType;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getRefTableType() {
		return refTableType;
	}
	public void setRefTableType(String refTableType) {
		this.refTableType = refTableType;
	}
	@Override
	public String toString() {
		return "TransactionDetailsBO [accountsDetailsBO=" + accountsDetailsBO + ", transactionType=" + transactionType
				+ ", refId=" + refId + ", refTableType=" + refTableType + "]";
	}
}
