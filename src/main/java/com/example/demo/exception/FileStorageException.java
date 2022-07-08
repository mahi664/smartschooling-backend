package com.example.demo.exception;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.constant.ErrorDetails;

public class FileStorageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ErrorDetails errorDetails;
	private final List<String> errorMessages;
	private final int errorCode;
	
	public FileStorageException(ErrorDetails errorDetails) {
		super(errorDetails.getErrorDescription());
		this.errorDetails = errorDetails;
		this.errorMessages = new ArrayList<>();
		this.errorCode = errorDetails.getErrorCode();
	}

	public FileStorageException(ErrorDetails errorDetails, Exception ex) {
		super(errorDetails.getErrorDescription(), ex);
		this.errorDetails = errorDetails;
		this.errorMessages = new ArrayList<>();
		this.errorCode = errorDetails.getErrorCode();
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
