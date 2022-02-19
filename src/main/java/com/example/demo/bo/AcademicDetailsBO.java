package com.example.demo.bo;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

public class AcademicDetailsBO {
	@Schema(description = "Academic Id to uniquely identify academic yeay",
			example = "AY-2019-20")
	private String academicId;
	@Schema(description = "Name of the academic year",
			example = "Academic Year 2019-20")
	private String academicYear;
	@Schema(description = "Display Name of the academic year",
			example = "Academic Year 2019-20")
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
