package com.example.demo.bo;

import java.util.Date;

public class TransactionBO {
	private String transactionId;
	private double amount;
	private Date transactonDate;
	private TransactionDetailsBO transactionDetailsBO;
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getTransactonDate() {
		return transactonDate;
	}
	public void setTransactonDate(Date transactonDate) {
		this.transactonDate = transactonDate;
	}
	public TransactionDetailsBO getTransactionDetailsBO() {
		return transactionDetailsBO;
	}
	public void setTransactionDetailsBO(TransactionDetailsBO transactionDetailsBO) {
		this.transactionDetailsBO = transactionDetailsBO;
	}
	@Override
	public String toString() {
		return "TransactionBO [transactionId=" + transactionId + ", amount=" + amount + ", transactonDate="
				+ transactonDate + ", transactionDetailsBO=" + transactionDetailsBO + "]";
	}
}
