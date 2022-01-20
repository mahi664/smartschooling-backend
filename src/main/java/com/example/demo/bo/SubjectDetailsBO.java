package com.example.demo.bo;

public class SubjectDetailsBO {
	private String subjectId;
	private String subjectName;
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	@Override
	public String toString() {
		return "SubjectDetails [subjectId=" + subjectId + ", subjectName=" + subjectName + "]";
	}
	
	
}
