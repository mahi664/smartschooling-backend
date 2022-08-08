package com.example.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeeCollectionTransactionDetails {

	private String trxnDetId;
	private String collectionId;
	private String feeId;
	private String academicId;
	private double amount;
}
