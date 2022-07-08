package com.example.demo.service.dto;

public class SuccessResponseObjectDto<I> extends SuccessResponseDto<I> {

	private I data;

	public I getData() {
		return data;
	}

	public void setData(I data) {
		this.data = data;
	}
	
	
}
