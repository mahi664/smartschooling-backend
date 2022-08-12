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
public class StudentFeesAssignedDetailsDto {

	private String academicYear;
	private String feeId;
	private String feeName;
	private double amount;
	private Date assignedDate;
	private String assignedBy;
}
