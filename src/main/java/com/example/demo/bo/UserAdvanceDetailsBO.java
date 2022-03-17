package com.example.demo.bo;

import java.util.List;
import java.util.Map;

public class UserAdvanceDetailsBO extends UserBasicDetailsBO {

	private Map<String, UserAcademicDetailsBO> userAcademicDetails; // Map of Academic id to Academic Details
	private List<LeaveDetailsBO> userApplicableLeaves;
	private List<UserManagerDetailsBO> userManagerDetails;
	private Map<String, PayrollDetailsBO> userPayrollDetails; // Map of Academic Id to Payroll details
	private List<SalaryDetailsBO> userSalaryDetails;
	public Map<String, UserAcademicDetailsBO> getUserAcademicDetails() {
		return userAcademicDetails;
	}
	public void setUserAcademicDetails(Map<String, UserAcademicDetailsBO> userAcademicDetails) {
		this.userAcademicDetails = userAcademicDetails;
	}
	public List<LeaveDetailsBO> getUserApplicableLeaves() {
		return userApplicableLeaves;
	}
	public void setUserApplicableLeaves(List<LeaveDetailsBO> userApplicableLeaves) {
		this.userApplicableLeaves = userApplicableLeaves;
	}
	public List<UserManagerDetailsBO> getUserManagerDetails() {
		return userManagerDetails;
	}
	public void setUserManagerDetails(List<UserManagerDetailsBO> userManagerDetails) {
		this.userManagerDetails = userManagerDetails;
	}
	public Map<String, PayrollDetailsBO> getUserPayrollDetails() {
		return userPayrollDetails;
	}
	public void setUserPayrollDetails(Map<String, PayrollDetailsBO> userPayrollDetails) {
		this.userPayrollDetails = userPayrollDetails;
	}
	public List<SalaryDetailsBO> getUserSalaryDetails() {
		return userSalaryDetails;
	}
	public void setUserSalaryDetails(List<SalaryDetailsBO> userSalaryDetails) {
		this.userSalaryDetails = userSalaryDetails;
	}
	public UserAdvanceDetailsBO(Map<String, UserAcademicDetailsBO> userAcademicDetails,
			List<LeaveDetailsBO> userApplicableLeaves, List<UserManagerDetailsBO> userManagerDetails,
			Map<String, PayrollDetailsBO> userPayrollDetails, List<SalaryDetailsBO> userSalaryDetails) {
		super();
		this.userAcademicDetails = userAcademicDetails;
		this.userApplicableLeaves = userApplicableLeaves;
		this.userManagerDetails = userManagerDetails;
		this.userPayrollDetails = userPayrollDetails;
		this.userSalaryDetails = userSalaryDetails;
	}
	public UserAdvanceDetailsBO() {
		super();
	}
	@Override
	public String toString() {
		return "UserAdvanceDetailsBO [userAcademicDetails=" + userAcademicDetails + ", userApplicableLeaves="
				+ userApplicableLeaves + ", userManagerDetails=" + userManagerDetails + ", userPayrollDetails="
				+ userPayrollDetails + ", userSalaryDetails=" + userSalaryDetails + "]";
	}
	
	
}
