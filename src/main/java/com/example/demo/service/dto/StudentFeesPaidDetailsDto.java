package com.example.demo.service.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeesPaidDetailsDto {

	private String transactionId;
	private Date receivedDate;
	private double amount;
	private String accountId;
	private String accountName;
	private String receivedBy;
	private String academicYear;
	private String feeId;
	private String feeName;
}
