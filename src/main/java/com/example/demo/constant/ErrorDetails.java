package com.example.demo.constant;

import org.springframework.http.HttpStatus;

public enum ErrorDetails {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1, ServiceConstants.INTERNAL_SERVER_ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, 2, ServiceConstants.BAD_REQUEST),
	DUPLICATE_GEN_REG_NO(HttpStatus.BAD_REQUEST, 3, ServiceConstants.DUPLICATE_GEN_REG_NO),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 4, ServiceConstants.METHOD_NOT_ALLOWED),
	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, 5, ServiceConstants.ROUTE_NOT_FOUND),
	ADMISSION_STD_NOT_FOUND(HttpStatus.NOT_FOUND, 6, ServiceConstants.ADMISSION_STD_NOT_FOUND),
	STUDENT_DETAILS_NOT_FOUND(HttpStatus.NOT_FOUND, 7, ServiceConstants.STUDENT_DETAILS_NOT_FOUND),
	FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 8, ServiceConstants.FILE_UPLOAD_ERROR),
	FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9, ServiceConstants.FILE_READ_ERROR),
	BLANK_FILE_ERROR(HttpStatus.BAD_REQUEST, 10, ServiceConstants.BLANK_FILE_ERROR),
	FIELD_MUST_NOT_BLANK(HttpStatus.BAD_REQUEST, 11, ServiceConstants.FIELD_MUST_NOT_BLANK),
	INVALID_VALUE(HttpStatus.BAD_REQUEST, 12, ServiceConstants.INVALID_VALUE),
	CLASS_DETAILS_NOT_FOUND(HttpStatus.NOT_FOUND, 13, ServiceConstants.CLASS_DETAILS_NOT_FOUND),
	RELIGION_NOT_FOUND(HttpStatus.NOT_FOUND, 14, ServiceConstants.RELIGION_NOT_FOUND),
	CASTE_NOT_FOUND(HttpStatus.NOT_FOUND, 15, ServiceConstants.CASTE_NOT_FOUND),
	GENDER_NOT_FOUND(HttpStatus.NOT_FOUND, 16, ServiceConstants.GENDER_NOT_FOUND),
	INVALID_NATIONALITY(HttpStatus.BAD_REQUEST, 17, ServiceConstants.INVALID_NATIONALITY),
	ACADEMIC_DETAILS_NOT_FOUND(HttpStatus.NOT_FOUND, 18, ServiceConstants.ACADEMIC_DETAILS_NOT_FOUND),
	INVALID_ADMISSION_DATE(HttpStatus.BAD_REQUEST, 19, ServiceConstants.INVALID_ADMISSION_DATE),
	INVALID_MOBILE_NUMBER(HttpStatus.BAD_REQUEST, 20, ServiceConstants.INVALID_MOBILE_NUMBER),
	INVALID_ALTERNATE_MOBILE_NUMBER(HttpStatus.BAD_REQUEST, 20, ServiceConstants.INVALID_ALTERNATE_MOBILE_NUMBER),
	INVALID_ADHAR_NUMBER(HttpStatus.BAD_REQUEST, 20, ServiceConstants.INVALID_ADHAR_NUMBER);
	
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
