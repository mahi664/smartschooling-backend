package com.example.demo.bo;

import java.util.Date;

public class RoleDetailsBO {

	private String roleId;
	private String roleName;
	private String roleDescription;
	private Date effDate;
	private Date endDate;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
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
	@Override
	public String toString() {
		return "RoleDetailsBO [roleId=" + roleId + ", roleName=" + roleName + ", roleDescription=" + roleDescription
				+ ", effDate=" + effDate + ", endDate=" + endDate + "]";
	}
	
	
}
