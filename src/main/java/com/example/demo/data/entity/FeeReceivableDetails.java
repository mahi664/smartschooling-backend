package com.example.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeReceivableDetails {
	
	private String studentId;
	private int genRegNo;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String address;
	private double totalFee;
}
