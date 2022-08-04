package com.example.demo.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeReceivableDetailsDto {

	private String studentId;
	private int genRegNo;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private double totalAmnt;
	private double dueAmnt;
	private double paidAmnt;
	private String address;
}
