package com.example.demo.bo;

import java.util.Date;

public class BranchDetailsBO {
	private String branchId;
	private String branchName;
	private Date foundationDate;
	private String branchAddress;
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Date getFoundationDate() {
		return foundationDate;
	}
	public void setFoundationDate(Date foundationDate) {
		this.foundationDate = foundationDate;
	}
	public String getBranchAddress() {
		return branchAddress;
	}
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}
	@Override
	public String toString() {
		return "BranchDetailsBO [branchId=" + branchId + ", branchName=" + branchName + ", foundationDate="
				+ foundationDate + ", branchAddress=" + branchAddress + "]";
	}
	
}
