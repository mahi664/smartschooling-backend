package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeesDueDetailsDto {

	private String academicYear;
	private String feeId;
	private String feeName;
	private double amount;
}
