package com.example.demo.bo;

import java.util.Date;

public class UserAttendanceDetailsBO {

	private Date attendanceDate;
	private char availability;
	private Date checkInTime;
	private Date checkOutTime;
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public char getAvailability() {
		return availability;
	}
	public void setAvailability(char availability) {
		this.availability = availability;
	}
	public Date getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(Date checkInTime) {
		this.checkInTime = checkInTime;
	}
	public Date getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(Date checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	@Override
	public String toString() {
		return "UserAttendanceDetailsBO [attendanceDate=" + attendanceDate + ", availability=" + availability
				+ ", checkInTime=" + checkInTime + ", checkOutTime=" + checkOutTime + "]";
	}
	
	
}
