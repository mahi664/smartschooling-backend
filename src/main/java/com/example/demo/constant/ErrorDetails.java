package com.example.demo.constant;

import org.springframework.http.HttpStatus;

public enum ErrorDetails {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1, ServiceConstants.INTERNAL_SERVER_ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, 2, ServiceConstants.BAD_REQUEST),
	DUPLICATE_GEN_REG_NO(HttpStatus.BAD_REQUEST, 3, ServiceConstants.DUPLICATE_GEN_REG_NO),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 4, ServiceConstants.METHOD_NOT_ALLOWED),
	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, 5, ServiceConstants.ROUTE_NOT_FOUND),
	ADMISSION_STD_NOT_FOUND(HttpStatus.NOT_FOUND, 6, ServiceConstants.ADMISSION_STD_NOT_FOUND),
	STUDENT_DETAILS_NOT_FOUND(HttpStatus.NOT_FOUND, 7, ServiceConstants.STUDENT_DETAILS_NOT_FOUND);
	
	private HttpStatus httpStatus;
	private int errorCode;
	private String errorDescription;
	
	ErrorDetails(HttpStatus httpStatus, int errorCode, String errorDescription) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorDescription() {
		return this.errorDescription;
	}
}
