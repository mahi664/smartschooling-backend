package com.example.demo.bo;

public class UserLoinCredsAndRoles {

	private String userId;
	private String username;
	private String password;
	private RoleDetailsBO role;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public RoleDetailsBO getRole() {
		return role;
	}
	public void setRole(RoleDetailsBO role) {
		this.role = role;
	}
	
	
}
