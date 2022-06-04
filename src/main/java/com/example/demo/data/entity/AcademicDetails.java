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
public class AcademicDetails {

	private String academicId;
	private String academicYear;
	private String displayNae;
	private Date academicStartDate;
	private Date academicEndDate;
	private Date lastUpdateTime;
	private String lastUpdateUser;
}
