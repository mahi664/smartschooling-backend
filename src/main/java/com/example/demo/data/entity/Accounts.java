package com.example.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Accounts {

	private String accountId;
	private String accountName;
	private String bankName;
	private String bankAccountNumber;
}
