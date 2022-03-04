package com.example.demo.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserBasicDetailsBO {

	private String userId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobile;
	private String email;
	private String address;
	private Date birthDate;
	private String maritalStatus;
	private String adhar;
	private String religion;
	private String caste;
	private String nationality;
	private LoginRequest loginDetailsBO;
	private List<RoleDetailsBO> userRolesDetailsBOs;
	private Map<String, UserAcademicDetailsBO> academicDetailsM; // Academic Id to academic details Map
	private List<UserApplicableLeavesBO> userApplicableLeavesBOs;
	private List<LeaveAccrualDetailsBO> userLeavesAccrualDetailsBOs;
	private List<LeaveDeductionDetailsBO> userLeaveDeductionsBos;
	private List<SalaryDetailsBO> salaryDetailsBOs;
	private Map<String, PayrollDetailsBO> payrollDetailsM; // Academic Id to paroll details Map
	private List<UserManagerDetailsBO> userManagerDetailsBOs;
	private Map<String, UserAttendanceDetailsBO> userAttendanceDetailsM; // Academic Id to attendance details Map
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
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getAdhar() {
		return adhar;
	}
	public void setAdhar(String adhar) {
		this.adhar = adhar;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public LoginRequest getLoginDetailsBO() {
		return loginDetailsBO;
	}
	public void setLoginDetailsBO(LoginRequest loginDetailsBO) {
		this.loginDetailsBO = loginDetailsBO;
	}
	public List<RoleDetailsBO> getUserRolesDetailsBOs() {
		return userRolesDetailsBOs;
	}
	public void setUserRolesDetailsBOs(List<RoleDetailsBO> userRolesDetailsBOs) {
		this.userRolesDetailsBOs = userRolesDetailsBOs;
	}
	public Map<String, UserAcademicDetailsBO> getAcademicDetailsM() {
		return academicDetailsM;
	}
	public void setAcademicDetailsM(Map<String, UserAcademicDetailsBO> academicDetailsM) {
		this.academicDetailsM = academicDetailsM;
	}
	public List<UserApplicableLeavesBO> getUserApplicableLeavesBOs() {
		return userApplicableLeavesBOs;
	}
	public void setUserApplicableLeavesBOs(List<UserApplicableLeavesBO> userApplicableLeavesBOs) {
		this.userApplicableLeavesBOs = userApplicableLeavesBOs;
	}
	public List<LeaveAccrualDetailsBO> getUserLeavesAccrualDetailsBOs() {
		return userLeavesAccrualDetailsBOs;
	}
	public void setUserLeavesAccrualDetailsBOs(List<LeaveAccrualDetailsBO> userLeavesAccrualDetailsBOs) {
		this.userLeavesAccrualDetailsBOs = userLeavesAccrualDetailsBOs;
	}
	public List<LeaveDeductionDetailsBO> getUserLeaveDeductionsBos() {
		return userLeaveDeductionsBos;
	}
	public void setUserLeaveDeductionsBos(List<LeaveDeductionDetailsBO> userLeaveDeductionsBos) {
		this.userLeaveDeductionsBos = userLeaveDeductionsBos;
	}
	public List<SalaryDetailsBO> getSalaryDetailsBOs() {
		return salaryDetailsBOs;
	}
	public void setSalaryDetailsBOs(List<SalaryDetailsBO> salaryDetailsBOs) {
		this.salaryDetailsBOs = salaryDetailsBOs;
	}
	public Map<String, PayrollDetailsBO> getPayrollDetailsM() {
		return payrollDetailsM;
	}
	public void setPayrollDetailsM(Map<String, PayrollDetailsBO> payrollDetailsM) {
		this.payrollDetailsM = payrollDetailsM;
	}
	public List<UserManagerDetailsBO> getUserManagerDetailsBOs() {
		return userManagerDetailsBOs;
	}
	public void setUserManagerDetailsBOs(List<UserManagerDetailsBO> userManagerDetailsBOs) {
		this.userManagerDetailsBOs = userManagerDetailsBOs;
	}
	public Map<String, UserAttendanceDetailsBO> getUserAttendanceDetailsM() {
		return userAttendanceDetailsM;
	}
	public void setUserAttendanceDetailsM(Map<String, UserAttendanceDetailsBO> userAttendanceDetailsM) {
		this.userAttendanceDetailsM = userAttendanceDetailsM;
	}
	@Override
	public String toString() {
		return "UserBasicDetailsBO [userId=" + userId + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", mobile=" + mobile + ", email=" + email + ", address=" + address
				+ ", birthDate=" + birthDate + ", maritalStatus=" + maritalStatus + ", adhar=" + adhar + ", religion="
				+ religion + ", caste=" + caste + ", nationality=" + nationality + ", loginDetailsBO=" + loginDetailsBO
				+ ", userRolesDetailsBOs=" + userRolesDetailsBOs + ", academicDetailsM=" + academicDetailsM
				+ ", userApplicableLeavesBOs=" + userApplicableLeavesBOs + ", userLeavesAccrualDetailsBOs="
				+ userLeavesAccrualDetailsBOs + ", userLeaveDeductionsBos=" + userLeaveDeductionsBos
				+ ", salaryDetailsBOs=" + salaryDetailsBOs + ", payrollDetailsM=" + payrollDetailsM
				+ ", userManagerDetailsBOs=" + userManagerDetailsBOs + ", userAttendanceDetailsM="
				+ userAttendanceDetailsM + "]";
	}
	
	
}
