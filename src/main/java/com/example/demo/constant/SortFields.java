package com.example.demo.constant;

public enum SortFields {

	firstName_ASC("first_name asc"), firstName_DESC("first_name desc"), middleName_ASC("middle_name asc"),
	middleName_DESC("middle_name desc"), lastName_ASC("last_name asc"), lastName_DESC("last_name desc"),
	studentId_ASC("stud_id asc"), studentId_DESC("stud_id desc"), genRegNo_ASC("reg_no asc"),
	genRegNo_DESC("reg_no desc"), address_ASC("address asc"), address_DESC("address desc"), caste_ASC("caste asc"),
	caste_DESC("caste desc");

	private String field;

	SortFields(String field) {
		this.field = field;
	}

	public String getField() {
		return this.field;
	}
}
