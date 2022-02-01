package com.example.demo.bo;

import java.util.Date;

public class StudentsFeesTransactionDetailsBO {
	private String collectionId;
	private String feeId;
	private double amount;
	private Date collectionDate;
	private AccountsDetailsBO accountsDetailsBO;
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getFeeId() {
		return feeId;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}
	public AccountsDetailsBO getAccountsDetailsBO() {
		return accountsDetailsBO;
	}
	public void setAccountsDetailsBO(AccountsDetailsBO accountsDetailsBO) {
		this.accountsDetailsBO = accountsDetailsBO;
	}
	@Override
	public String toString() {
		return "StudentsFeesTransactionDetailsBO [collectionId=" + collectionId + ", feeId=" + feeId + ", amount="
				+ amount + ", collectionDate=" + collectionDate + ", accountsDetailsBO=" + accountsDetailsBO + "]";
	}
}
