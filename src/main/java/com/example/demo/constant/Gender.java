package com.example.demo.constant;

public enum Gender {

	MALE("Male"), FEMALE("Female"), OTHER("Other");

	private String gender;

	Gender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return this.gender;
	}
}
