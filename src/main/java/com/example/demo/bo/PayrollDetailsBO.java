package com.example.demo.bo;

import java.util.Date;

public class PayrollDetailsBO {

	private Date payrollDate;
	private double paidDays;
	private double unpaidDays;
	private double amount;
	private char payrollLocked;
	private MonthMasterDetailsBO payrollMonth;
	public Date getPayrollDate() {
		return payrollDate;
	}
	public void setPayrollDate(Date payrollDate) {
		this.payrollDate = payrollDate;
	}
	public double getPaidDays() {
		return paidDays;
	}
	public void setPaidDays(double paidDays) {
		this.paidDays = paidDays;
	}
	public double getUnpaidDays() {
		return unpaidDays;
	}
	public void setUnpaidDays(double unpaidDays) {
		this.unpaidDays = unpaidDays;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public char getPayrollLocked() {
		return payrollLocked;
	}
	public void setPayrollLocked(char payrollLocked) {
		this.payrollLocked = payrollLocked;
	}
	public MonthMasterDetailsBO getPayrollMonth() {
		return payrollMonth;
	}
	public void setPayrollMonth(MonthMasterDetailsBO payrollMonth) {
		this.payrollMonth = payrollMonth;
	}
	@Override
	public String toString() {
		return "PayrollDetailsBO [payrollDate=" + payrollDate + ", paidDays=" + paidDays + ", unpaidDays=" + unpaidDays
				+ ", amount=" + amount + ", payrollLocked=" + payrollLocked + ", payrollMonth=" + payrollMonth + "]";
	}
	
	
}
