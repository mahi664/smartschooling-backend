package com.example.demo.bo;

public class AccountsDetailsBO {
	private String accountId;
	private String accountName;
	private String bankName;
	private String bankAccountNumber;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	@Override
	public String toString() {
		return "AccountsDetailsBO [accountId=" + accountId + ", accountName=" + accountName + ", bankName=" + bankName
				+ ", bankAccountNumber=" + bankAccountNumber + "]";
	}
}
