package com.example.demo.bo;

public class LoginResponse {

	private String username;
	private String token;
	private String role;
	

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public LoginResponse() {
	}
	public LoginResponse(String username, String token, String role) {
		this.username = username;
		this.token = token;
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "LoginResponse [username=" + username + ", token=" + token + "]";
	}
}
