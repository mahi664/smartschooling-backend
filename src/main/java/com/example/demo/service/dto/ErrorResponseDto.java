package com.example.demo.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

	private String errorCode;
	private String errorMessage;
	private List<String> errorMessages;
}
