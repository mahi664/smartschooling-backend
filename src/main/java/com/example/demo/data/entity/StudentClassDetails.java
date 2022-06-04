package com.example.demo.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentClassDetails {

	private String studentId;
	private AcademicDetails academicDetails;
	private ClassDetails classDetails;
}
