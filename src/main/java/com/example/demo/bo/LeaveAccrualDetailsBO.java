package com.example.demo.bo;

import java.util.Date;

public class LeaveAccrualDetailsBO {
	
	private String id;
	private LeaveDetailsBO leaveDetailsBO;
	private Date accrualDate;
	private double amount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LeaveDetailsBO getLeaveDetailsBO() {
		return leaveDetailsBO;
	}
	public void setLeaveDetailsBO(LeaveDetailsBO leaveDetailsBO) {
		this.leaveDetailsBO = leaveDetailsBO;
	}
	public Date getAccrualDate() {
		return accrualDate;
	}
	public void setAccrualDate(Date accrualDate) {
		this.accrualDate = accrualDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "LeaveAccrualDetailsBO [id=" + id + ", leaveDetailsBO=" + leaveDetailsBO + ", accrualDate=" + accrualDate
				+ ", amount=" + amount + "]";
	}
	
	
}
