package com.example.demo.constant;

public enum StudentImportFields {

	GEN_REG_NO("Gen Reg No"), BOOK_NO("Book No"), FIRST_NAME("First Name"), MIDDLE_NAME("Middle Name"),
	LAST_NAME("Last Name"), ADHAR_NO("Adhar Number"), BIRTH_DATE("Date of Birth"), GENDER("Gender"),
	RELIGION("Religion"), CASTE("Caste"), NATIONALITY("Nationality"), MOBILE_NO("Mobile No"),
	ALT_MOB_NO("Alternate Mob No"), EMAIL("Email"), ADDRESS("Address"), ADMISSION_STD("Admission Std"),
	ADMISSION_DATE("Admission Date"), ACADEMIC_YEAR("Academic Year"), PREV_SCHOOL("Previous School"),
	TRANSPORT_OPTED("Transport Opted"), ROUTE("Route");

	private String field;

	StudentImportFields(String field) {
		this.field = field;
	}

	public String getField() {
		return this.field;
	}
}
