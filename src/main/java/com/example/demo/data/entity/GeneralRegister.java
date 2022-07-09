package com.example.demo.data.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralRegister {

	private int regNo;
	private int bookNo;
	private String studentId;
	private String admissionStd;
	private Date admissionDate;
	private String previousSchool;
	private Date schoolLeavingDate;
	private String schoolLeavingReason;
	private String newSchool;
	private String academicYear;
}
