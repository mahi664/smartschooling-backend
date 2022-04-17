package com.example.demo.bo;

public class UserAcademicDetailsDto {

	private String academicId;
	private String classId;
	private String subjectId;
	public String getAcademicId() {
		return academicId;
	}
	public void setAcademicId(String academicId) {
		this.academicId = academicId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	@Override
	public String toString() {
		return "UserAcademicDetailsDto [academicId=" + academicId + ", classId=" + classId + ", subjectId=" + subjectId
				+ "]";
	}
	
	
}
