package com.example.demo.bo;

import java.util.Date;

public class UserManagerDetailsBO {

	private String userId; //User id of the reporting manager
	private String name;
	private Date effDate;
	private Date endDate;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserManagerDetailsBO [userId=" + userId + ", name=" + name + ", effDate=" + effDate + ", endDate="
				+ endDate + "]";
	}
	
	
}
