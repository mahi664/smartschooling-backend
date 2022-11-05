package com.example.demo.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.constant.ErrorDetails;
import com.example.demo.service.dto.ResponseDto;
import com.example.demo.utils.ResponseUtil;

@ControllerAdvice
public class SmartSchoolingExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(StudentException.class)
	protected ResponseEntity<ResponseDto> handleStudentException(StudentException ex) {
		logger.error("Handling student exception {}", ex);
		return ResponseUtil.populateErrorResponse(ex.getErrorDetails(), ex.getErrorMessages());
	}
	
	@ExceptionHandler(FileStorageException.class)
	protected ResponseEntity<ResponseDto> handleFileStorageException(FileStorageException ex) {
		logger.error("Handling student exception {}", ex);
		return ResponseUtil.populateErrorResponse(ex.getErrorDetails(), ex.getErrorMessages());
	}
	
	@ExceptionHandler(StaffException.class)
	protected ResponseEntity<ResponseDto> handleStaffExceptionException(StaffException ex) {
		logger.error("Handling student exception {}", ex);
		return ResponseUtil.populateErrorResponse(ex.getErrorDetails(), ex.getErrorMessages());
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("Populating error response for invalid method arguments");
		List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		return ResponseUtil.populateErrorResponseAsObject(ErrorDetails.BAD_REQUEST, errorMessages);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("Populating error response for invalid method arguments");
		return ResponseUtil.populateErrorResponseAsObject(ErrorDetails.METHOD_NOT_ALLOWED);
	}
}
