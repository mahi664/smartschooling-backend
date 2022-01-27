package com.example.demo.bo;

import java.util.Date;

public class AcademicDetailsBO {
	private String academicId;
	private String academicYear;
	private String displayName;
	private Date academicStartDate;
	private Date academicEndDate;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Date getAcademicStartDate() {
		return academicStartDate;
	}
	public void setAcademicStartDate(Date academicStartDate) {
		this.academicStartDate = academicStartDate;
	}
	public Date getAcademicEndDate() {
		return academicEndDate;
	}
	public void setAcademicEndDate(Date academicEndDate) {
		this.academicEndDate = academicEndDate;
	}
	@Override
	public String toString() {
		return "AcademicDetailsBO [academicId=" + academicId + ", academicYear=" + academicYear + ", displayName="
				+ displayName + ", academicStartDate=" + academicStartDate + ", academicEndDate=" + academicEndDate
				+ "]";
	}
	
	
}
