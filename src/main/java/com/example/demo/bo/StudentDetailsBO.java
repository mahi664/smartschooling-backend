package com.example.demo.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StudentDetailsBO {
	private String studentId;
	private String firstName;
	private String middleName;
	private String lastName;
	private Date birthDate;
	private String gender;
	private String adharNumber;
	private String mobileNumber;
	private String email;
	private String alternateMobile;
	private String address;
	private String religion;
	private String caste;
	private String nationality;
	private boolean transportOpted;
	private RouteDetailsBO routeDetailsBO;
	private Map<String, List<FeesDetailsBO>> studentFeeDetails; // key- Academic id
	private Map<String, List<ClassDetaislBO>> studentClassDetails; // key- AcademicId
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAdharNumber() {
		return adharNumber;
	}
	public void setAdharNumber(String adharNumber) {
		this.adharNumber = adharNumber;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAlternateMobile() {
		return alternateMobile;
	}
	public void setAlternateMobile(String alternateMobile) {
		this.alternateMobile = alternateMobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public String getCaste() {
		return caste;
	}
	public void setCaste(String caste) {
		this.caste = caste;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public boolean isTransportOpted() {
		return transportOpted;
	}
	public void setTransportOpted(boolean transportOpted) {
		this.transportOpted = transportOpted;
	}
	public RouteDetailsBO getRouteDetailsBO() {
		return routeDetailsBO;
	}
	public void setRouteDetailsBO(RouteDetailsBO routeDetailsBO) {
		this.routeDetailsBO = routeDetailsBO;
	}
	public Map<String, List<FeesDetailsBO>> getStudentFeeDetails() {
		return studentFeeDetails;
	}
	public void setStudentFeeDetails(Map<String, List<FeesDetailsBO>> studentFeeDetails) {
		this.studentFeeDetails = studentFeeDetails;
	}
	public Map<String, List<ClassDetaislBO>> getStudentClassDetails() {
		return studentClassDetails;
	}
	public void setStudentClassDetails(Map<String, List<ClassDetaislBO>> studentClassDetails) {
		this.studentClassDetails = studentClassDetails;
	}
	@Override
	public String toString() {
		return "StudentDetailsBO [studentId=" + studentId + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", birthDate=" + birthDate + ", gender=" + gender + ", adharNumber="
				+ adharNumber + ", mobileNumber=" + mobileNumber + ", email=" + email + ", alternateMobile="
				+ alternateMobile + ", address=" + address + ", religion=" + religion + ", caste=" + caste
				+ ", nationality=" + nationality + ", transportOpted=" + transportOpted + ", routeDetailsBO="
				+ routeDetailsBO + ", studentFeeDetails=" + studentFeeDetails + ", studentClassDetails="
				+ studentClassDetails + "]";
	}
	
}
