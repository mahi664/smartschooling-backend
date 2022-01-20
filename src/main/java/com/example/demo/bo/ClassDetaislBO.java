package com.example.demo.bo;

import java.util.List;

public class ClassDetaislBO {
	private String classId;
	private String className;
	private List<SubjectDetailsBO> subjects;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<SubjectDetailsBO> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<SubjectDetailsBO> subjects) {
		this.subjects = subjects;
	}
	@Override
	public String toString() {
		return "ClassDetaislBO [classId=" + classId + ", className=" + className + ", subjects=" + subjects + "]";
	}
}
