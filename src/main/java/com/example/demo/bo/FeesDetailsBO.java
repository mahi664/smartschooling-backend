package com.example.demo.bo;

import java.util.Date;

public class FeesDetailsBO {

	private String feeId;
	private String feeName;
	private String feeDiscription;
	private String classId;
	private String routeId;
	private double amount;
	private Date effDate;
	private Date endDate;
	private String lastUser;
	public String getFeeId() {
		return feeId;
	}
	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getFeeDiscription() {
		return feeDiscription;
	}
	public void setFeeDiscription(String feeDiscription) {
		this.feeDiscription = feeDiscription;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLastUser() {
		return lastUser;
	}
	public void setLastUser(String lastUser) {
		this.lastUser = lastUser;
	}
	@Override
	public String toString() {
		return "FeesDetailsBO [feeId=" + feeId + ", feeName=" + feeName + ", feeDiscription=" + feeDiscription
				+ ", classId=" + classId + ", routeId=" + routeId + ", amount=" + amount + ", effDate=" + effDate
				+ ", endDate=" + endDate + ", lastUser=" + lastUser + "]";
	}
	
}
