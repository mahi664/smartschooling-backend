package com.example.demo.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesPaidTrxnDetailsDto {

	@NotBlank(message = "Academic Id must not be empty")
	private String academicId;
	@NotBlank(message = "Fee Id must not be empty")
	private String feeId;
	@Positive(message = "Amount must not be 0 or negative value")
	private double amount;
}
