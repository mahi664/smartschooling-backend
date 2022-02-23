package com.example.demo.bo;

import java.util.Date;

public class LeaveDeductionDetailsBO {

	private String userLeaveRecId;
	private LeaveDetailsBO leaveDetailsBO;
	private Date effDate;
	private Date endDate;
	private char halfDayLeave;
	private double amount;
	public String getUserLeaveRecId() {
		return userLeaveRecId;
	}
	public void setUserLeaveRecId(String userLeaveRecId) {
		this.userLeaveRecId = userLeaveRecId;
	}
	public LeaveDetailsBO getLeaveDetailsBO() {
		return leaveDetailsBO;
	}
	public void setLeaveDetailsBO(LeaveDetailsBO leaveDetailsBO) {
		this.leaveDetailsBO = leaveDetailsBO;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public char getHalfDayLeave() {
		return halfDayLeave;
	}
	public void setHalfDayLeave(char halfDayLeave) {
		this.halfDayLeave = halfDayLeave;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "LeaveDeductionDetailsBO [userLeaveRecId=" + userLeaveRecId + ", leaveDetailsBO=" + leaveDetailsBO
				+ ", effDate=" + effDate + ", endDate=" + endDate + ", halfDayLeave=" + halfDayLeave + ", amount="
				+ amount + "]";
	}
	
	
}
