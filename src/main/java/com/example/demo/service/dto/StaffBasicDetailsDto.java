package com.example.demo.service.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffBasicDetailsDto {

	@NotBlank(message = "First name must not be blank")
	private String firstName;
	private String middleName;
	@NotBlank(message = "Last name must not be blank")
	private String lastName;
	private String adharNumber;
	@NotNull(message = "Birth Date must not be null or blank")
	private Date birthDate;
	@NotBlank(message = "Gender must not be blank")
	private String gender;
	private String maritalStatus;
	@NotBlank(message = "Religion must not be blank")
	private String religion;
	@NotBlank(message = "Caste must not be blank")
	private String caste;
	@NotBlank(message = "Nationality must not be blank")
	private String nationality;
	@NotBlank(message = "Mobile Number must not be blank")
	private String mobileNumber;
	private String alternateMobileNumber;
	private String emailId;
	@NotBlank(message = "Address must not be blank")
	private String address;
	private String userId;
}
