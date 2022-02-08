package com.example.demo.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StudentsFeesTransactionDetailsBO {
	private String collectionId;
	private double amount;
	private Date collectionDate;
	private AccountsDetailsBO accountsDetailsBO;
	private Map<String, List<FeesDetailsBO>> academicId2FeesDetailsMap;	
	private String lastUser;
	
	public String getLastUser() {
		return lastUser;
	}
	public void setLastUser(String lastUser) {
		this.lastUser = lastUser;
	}
	public Map<String, List<FeesDetailsBO>> getAcademicId2FeesDetailsMap() {
		return academicId2FeesDetailsMap;
	}
	public void setAcademicId2FeesDetailsMap(Map<String, List<FeesDetailsBO>> academicId2FeesDetailsMap) {
		this.academicId2FeesDetailsMap = academicId2FeesDetailsMap;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
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
		return "StudentsFeesTransactionDetailsBO [collectionId=" + collectionId + ", amount=" + amount
				+ ", collectionDate=" + collectionDate + ", accountsDetailsBO=" + accountsDetailsBO
				+ ", academicId2FeesDetailsMap=" + academicId2FeesDetailsMap + ", lastUser=" + lastUser + "]";
	}
}
