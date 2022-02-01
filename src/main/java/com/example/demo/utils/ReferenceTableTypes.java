package com.example.demo.utils;

public enum ReferenceTableTypes {
	STUD_FEES_COLLECTON_DET("students_fees_collections_details");
	
	private String value;

	private ReferenceTableTypes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
