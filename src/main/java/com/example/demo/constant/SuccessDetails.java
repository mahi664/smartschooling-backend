package com.example.demo.constant;

public enum SuccessDetails {

	STUDENT_REGISTRATION_SUCCESSFUL(1, ServiceConstants.STUDENT_REGISTRATION_SUCCESSFUL),
	STUDENT_DETAILS_FETCHED_SUCCESSFULLY(2, ServiceConstants.STUDENT_DETAILS_FETCHED_SUCCESSFULLY),
	RECEIVABLES_FETCHED_SUCCESSFULLY(3, ServiceConstants.RECEIVABLES_FETCHED_SUCCESSFULLY),
	RECEIVABLES_STATS_FETCHED_SUCCESSFULLY(4, ServiceConstants.RECEIVABLES_STATS_FETCHED_SUCCESSFULLY);

	private int successCode;
	private String sucessMessage;

	private SuccessDetails(int successCode, String sucessMessage) {
		this.successCode = successCode;
		this.sucessMessage = sucessMessage;
	}

	public int getSuccessCode() {
		return this.successCode;
	}

	public String getSucessMessage() {
		return this.sucessMessage;
	}
}
