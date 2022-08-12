package com.example.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetails {

	private String transactionDetId;
	private String transactionId;
	private String accountId;
	private String transactionType;
	private String refId;
	private String refTableType;
}
