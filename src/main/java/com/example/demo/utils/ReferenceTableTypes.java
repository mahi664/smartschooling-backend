package com.example.demo.utils;

public enum ReferenceTableTypes {
	STUDENTS_FEES_COLLECTION_TRANSACTION("STUDENTS_FEES_COLLECTION_TRANSACTION");
	
	private String value;

	private ReferenceTableTypes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
