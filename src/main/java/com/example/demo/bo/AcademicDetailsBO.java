package com.example.demo.bo;

public class AcademicDetailsBO {
	private String academicId;
	private String academicYear;
	public String getAcademicId() {
		return academicId;
	}
	public void setAcademicId(String academicId) {
		this.academicId = academicId;
	}
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}
	@Override
	public String toString() {
		return "AcademicDetailsBO [academicId=" + academicId + ", academicYear=" + academicYear + "]";
	}
	
	
}
