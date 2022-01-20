package com.example.demo.bo;

import java.util.Date;

public class InstituteDetailsBO {
	
	private String instituteId;
	private String instituteName;
	private Date instituteFoundationDate;
	private String instituteAddress;
	public String getInstituteId() {
		return instituteId;
	}
	public void setInstituteId(String instituteId) {
		this.instituteId = instituteId;
	}
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	public Date getInstituteFoundationDate() {
		return instituteFoundationDate;
	}
	public void setInstituteFoundationDate(Date instituteFoundationDate) {
		this.instituteFoundationDate = instituteFoundationDate;
	}
	public String getInstituteAddress() {
		return instituteAddress;
	}
	public void setInstituteAddress(String instituteAddress) {
		this.instituteAddress = instituteAddress;
	}
	@Override
	public String toString() {
		return "InstituteDetailsBO [instituteId=" + instituteId + ", instituteName=" + instituteName
				+ ", instituteFoundationDate=" + instituteFoundationDate + ", instituteAddress=" + instituteAddress
				+ "]";
	}
	
}
