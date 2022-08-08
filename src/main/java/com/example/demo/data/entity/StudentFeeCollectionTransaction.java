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
public class StudentFeeCollectionTransaction {

	private String collectionId;
	private String studentId;
	private Date transactionDate;
	private String accountId;
	private Date lastUpdateTime;
	private String lastUser;
}
