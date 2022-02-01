package com.example.demo.utils;

public enum TransactionTypes {
	CREDIT("C"),
	DEBIT("D");
	
	private String value;
	private TransactionTypes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
