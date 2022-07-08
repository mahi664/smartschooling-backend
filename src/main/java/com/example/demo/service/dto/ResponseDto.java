package com.example.demo.service.dto;

public class ResponseDto {

	private SuccessResponseDto<?> success;
	private ErrorResponseDto error;
	
	public SuccessResponseDto<?> getSuccess() {
		return success;
	}
	public void setSuccess(SuccessResponseDto<?> successResponseDto) {
		this.success = successResponseDto;
	}
	public ErrorResponseDto getError() {
		return error;
	}
	public void setError(ErrorResponseDto errorResponseDto) {
		this.error = errorResponseDto;
	}
	
	
}
