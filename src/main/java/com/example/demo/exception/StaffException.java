package com.example.demo.exception;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.constant.ErrorDetails;

public class StaffException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5066026405958201231L;
	private final ErrorDetails errorDetails;
	private List<String> errorMessages;
	private final int errorCode;
	
	public StaffException(ErrorDetails errorDetails) {
		super(errorDetails.getErrorDescription());
		this.errorDetails = errorDetails;
		this.errorMessages = new ArrayList<>();
		this.errorCode = errorDetails.getErrorCode();
	}

	public StaffException(ErrorDetails errorDetails, Exception ex) {
		super(errorDetails.getErrorDescription(), ex);
		this.errorDetails = errorDetails;
		this.errorMessages = new ArrayList<>();
		this.errorCode = errorDetails.getErrorCode();
	}
	
	public StaffException(ErrorDetails errorDetails, List<String> errorMessages) {
		super(errorDetails.getErrorDescription());
		this.errorDetails = errorDetails;
		this.errorMessages = new ArrayList<>();
		this.errorCode = errorDetails.getErrorCode();
		this.errorMessages = errorMessages;
	}

	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
