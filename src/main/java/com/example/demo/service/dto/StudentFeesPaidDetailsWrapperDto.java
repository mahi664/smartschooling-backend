package com.example.demo.service.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesPaidDetailsWrapperDto {

	private String transactionId;
	private Date receivedDate;
	private double amount;
	private String accountId;
	private String accountName;
	private String receivedBy;
	List<StudentFeesPaidDetailsDto> studentFeesPaidDetails;
}
