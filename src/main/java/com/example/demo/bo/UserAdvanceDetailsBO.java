package com.example.demo.bo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class UserAdvanceDetailsBO extends UserBasicDetailsBO {

	private Map<String, UserAcademicDetailsBO> userAcademicDetails; // Map of Academic id to Academic Details
	private List<LeaveDetailsBO> userApplicableLeaves;
	private List<UserManagerDetailsBO> userManagerDetails;
	private Map<String, List<PayrollDetailsBO>> userPayrollDetails; // Map of Academic Id to Payroll details
	private List<SalaryDetailsBO> userSalaryDetails;
	private List<RoleDetailsBO> userRoles;
	
	
	public List<RoleDetailsBO> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<RoleDetailsBO> userRoles) {
		this.userRoles = userRoles;
	}
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
	public Map<String, List<PayrollDetailsBO>> getUserPayrollDetails() {
		return userPayrollDetails;
	}
	public void setUserPayrollDetails(Map<String, List<PayrollDetailsBO>> userPayrollDetails) {
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
			Map<String, List<PayrollDetailsBO>> userPayrollDetails, List<SalaryDetailsBO> userSalaryDetails) {
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
				+ userPayrollDetails + ", userSalaryDetails=" + userSalaryDetails + ", userRoles=" + userRoles + "]";
	}
	
	
}
