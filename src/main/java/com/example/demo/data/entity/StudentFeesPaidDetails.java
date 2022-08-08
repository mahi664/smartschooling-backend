package com.example.demo.data.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentFeesPaidDetails {

	private String collectionId;
	private String accountId;
	private String accountName;
	private Date lastUpdateTime;
	private String lastUser;
	private String academicId;
	private double amount;
	private String feeId;
	private String feeName;
}
