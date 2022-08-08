package com.example.demo.data.entity;

import lombok.Builder;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeesAssignedDetails {

	private String academicYear;
	private String feeId;
	private String feeName;
	private double amount;
	private Date lastUpdateTime;
	private String lastUser;
}
