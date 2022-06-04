package com.example.demo.constant;

public enum FeeTypes {

	TUTION_FEES("Tution Fees"), TRANSPORT_FEES("Transport Fees");

	private String value;
	
	FeeTypes(String value) {
		this.value = value;
	}
	
	public String getFeeType() {
		return this.value;
	}
	
}
