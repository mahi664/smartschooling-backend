package com.example.demo.data.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBasicDetails {

	private String userId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobile;
	private String email;
	private String address;
	private Date birthDate;
	private String maritalStatus;
	private String adhar;
	private String religion;
	private String caste;
	private String nationality;
	private String gender;
	private String alternateMobile;
}
