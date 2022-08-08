package com.example.demo.data.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

	private String transactionId;
	private double amount;
	private Date transactionDate;
}
