package com.example.demo.bo;

import java.util.Date;

public class SalaryDetailsBO {

	private double amount;
	private Date effDate;
	private Date endDate;
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
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
	@Override
	public String toString() {
		return "SalaryDetailsBO [amount=" + amount + ", effDate=" + effDate + ", endDate=" + endDate + "]";
	}
	
	
}
