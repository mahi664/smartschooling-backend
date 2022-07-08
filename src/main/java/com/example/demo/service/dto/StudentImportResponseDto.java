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
public class StudentImportResponseDto {

	private String studentId;
	private int genRegNo;
	private int bookNo;
	private String admissionStd;
	private Date admissionDate;
	private String academicYear;
	private String prevSchool;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date birthDate;
	private String gender;
	private String adharNumber;
	private String mobileNumber;
	private String email;
	private String alternateMobile;
	private String address;
	private String religion;
	private String caste;
	private String nationality;
	private boolean transportOpted;
	private String route;
}
