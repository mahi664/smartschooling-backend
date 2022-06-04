package com.example.demo.data.entity;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentBasicDetails {

	private String studentId;
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
	private List<GeneralRegister> generalRegisterDetails;
	private List<StudentClassDetails> studentClassDetails;
	private List<StudentTransportDetails> studentTransportDetails;
}
