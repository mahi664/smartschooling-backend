package com.example.demo.service.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesPaidTrxnResponseDto {

	private String transactionId;
	private double amount;
	private Date trxnDate;
}
