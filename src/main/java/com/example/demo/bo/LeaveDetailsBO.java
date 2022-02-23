package com.example.demo.bo;

import java.util.Date;

public class LeaveDetailsBO {

	private String leaveId;
	private String leaveName;
	private String leaveDescription;
	private String accrualFreq;
	private int accrualDay;
	private double amount;
	private Date startDate;
	private Date endDate;
	public String getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(String leaveId) {
		this.leaveId = leaveId;
	}
	public String getLeaveName() {
		return leaveName;
	}
	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}
	public String getLeaveDescription() {
		return leaveDescription;
	}
	public void setLeaveDescription(String leaveDescription) {
		this.leaveDescription = leaveDescription;
	}
	public String getAccrualFreq() {
		return accrualFreq;
	}
	public void setAccrualFreq(String accrualFreq) {
		this.accrualFreq = accrualFreq;
	}
	public int getAccrualDay() {
		return accrualDay;
	}
	public void setAccrualDay(int accrualDay) {
		this.accrualDay = accrualDay;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "LeaveDetailsBO [leaveId=" + leaveId + ", leaveName=" + leaveName + ", leaveDescription="
				+ leaveDescription + ", accrualFreq=" + accrualFreq + ", accrualDay=" + accrualDay + ", amount="
				+ amount + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	
}
