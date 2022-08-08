package com.example.demo.service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesReceivableDetailsDto {

	private List<StudentFeesAssignedDetailsDto> feesAssignedDetails;
	private List<StudentFeesPaidDetailsWrapperDto> feesPaidDetails;
	private List<StudentFeesDueDetailsDto> feesDueDetails;
	private double totalFeeAmnt;
	private double totalPaidAmnt;
	private double totalDueAmnt;
}
