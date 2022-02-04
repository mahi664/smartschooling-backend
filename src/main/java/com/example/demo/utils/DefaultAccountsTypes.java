package com.example.demo.utils;

public enum DefaultAccountsTypes {
	STUDENT_FEES_ACCOUNT("STUDENT FEE ACCOUNT");
	
	private String value;
	private DefaultAccountsTypes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
