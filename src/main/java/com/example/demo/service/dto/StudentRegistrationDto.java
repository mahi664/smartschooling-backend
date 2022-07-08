package com.example.demo.service.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationDto {

	@Positive(message = "Gen Reg no must be greater than zero")
	private int genRegNo;
	@Positive(message = "Book no must be greater than zero")
	private int bookNo;
	@NotBlank(message = "Admission Std must not be null or blank")
	private String admissionStd;
	@NotNull(message = "Admission Date must not be null or blank")
	private Date admissionDate;
	@NotBlank(message = "Academic Year must not be null or blank")
	private String academicYear;
	private String prevSchool;
	@NotBlank(message = "First Name must not be null or blank")
	private String firstName;
	private String middleName;
	@NotBlank(message = "Last Name must not be null or blank")
	private String lastName;
	@NotNull(message = "Birth Date must not be null or blank")
	private Date birthDate;
	@NotBlank(message = "Gender must not be null or blank")
	private String gender;
	private String adharNumber;
	@NotBlank(message = "Mobile Number must not be null or blank")
	private String mobileNumber;
	private String email;
	private String alternateMobileNumber;
	@NotBlank(message = "Address must not be null or blank")
	private String address;
	@NotBlank(message = "Religion must not be null or blank")
	private String religion;
	@NotBlank(message = "Caste must not be null or blank")
	private String caste;
	@NotBlank(message = "Nationality must not be null or blank")
	private String nationality;
	private boolean transportOpted;
	private String route;
}
