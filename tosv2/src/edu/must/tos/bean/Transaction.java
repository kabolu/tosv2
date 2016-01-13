package edu.must.tos.bean;

import java.util.Date;

public class Transaction {

	private int transactionNo;

	private String orderSeqNo;

	private String cashier;

	private Date paidDate;

	private String paidMentType;

	private double pay;

	private String paidCurrency;

	private String remarks;

	private String studentNo;

	private String chineseName;

	private String englishName;
	
	private int confirmQty;
	
	private int withdrawQty;
	
	private String status;

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public String getPaidMentType() {
		return paidMentType;
	}

	public void setPaidMentType(String paidMentType) {
		this.paidMentType = paidMentType;
	}

	public double getPay() {
		return pay;
	}

	public void setPay(double pay) {
		this.pay = pay;
	}

	public int getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(int transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(String orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public int getConfirmQty() {
		return confirmQty;
	}

	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getWithdrawQty() {
		return withdrawQty;
	}

	public void setWithdrawQty(int withdrawQty) {
		this.withdrawQty = withdrawQty;
	}
}
