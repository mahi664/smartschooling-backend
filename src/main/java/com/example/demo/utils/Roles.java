package com.example.demo.utils;

public enum Roles {

	ADMIN("Admin");
	
	private String value;
	private Roles(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
