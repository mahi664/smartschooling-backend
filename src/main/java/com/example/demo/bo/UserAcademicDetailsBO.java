package com.example.demo.bo;

import java.util.List;

public class UserAcademicDetailsBO {
	
	private List<ClassDetaislBO> userClassSubjectsL;

	public List<ClassDetaislBO> getUserClassSubjectsL() {
		return userClassSubjectsL;
	}

	public void setUserClassSubjectsL(List<ClassDetaislBO> userClassSubjectsL) {
		this.userClassSubjectsL = userClassSubjectsL;
	}

	@Override
	public String toString() {
		return "UserAcademicDetailsBO [userClassSubjectsL=" + userClassSubjectsL + "]";
	}
	
	
}
