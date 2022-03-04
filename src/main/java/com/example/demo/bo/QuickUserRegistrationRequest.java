package com.example.demo.bo;

import java.util.Date;

public class QuickUserRegistrationRequest {

	private String userId;
	private String firstName;
	private String lastName;
	private String mobile;
	private Date birthDate;
	private String address;
	private String maritalStatus;
	
	
	public QuickUserRegistrationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QuickUserRegistrationRequest(String userId, String firstName, String lastName, String mobile, Date birthDate,
			String address, String maritalStatus) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.birthDate = birthDate;
		this.address = address;
		this.maritalStatus = maritalStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	
	
}
