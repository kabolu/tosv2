package edu.must.tos.bean;

import java.util.Date;

public class OrDetail {
	
	private int orderSeqNo;

	private String studentNo;

	private String orderIntake;

	private String isbn;

	private String bookTitle;

	private String actInd;

	private int confirmQty;

	private int unconfirmQty;

	private int withdrawQty;

	private int withdrawQty2;

	private String creUid;

	private Date creDate;

	private String updUid;

	private Date updDate;

	private int notEnoughQty;

	private String courseCode;
	
	private String majorCode ;

	private double paidAmount;

	private String paidCurrency;
	
	private String paidStatus;
	
	private String remarks;
	
	public int getNotEnoughQty() {
		return notEnoughQty;
	}

	public void setNotEnoughQty(int notEnoughQty) {
		this.notEnoughQty = notEnoughQty;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getOrderIntake() {
		return orderIntake;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getConfirmQty() {
		return confirmQty;
	}

	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}

	public int getUnconfirmQty() {
		return unconfirmQty;
	}

	public void setUnconfirmQty(int unconfirmQty) {
		this.unconfirmQty = unconfirmQty;
	}

	public int getWithdrawQty() {
		return withdrawQty;
	}

	public void setWithdrawQty(int withdrawQty) {
		this.withdrawQty = withdrawQty;
	}

	public String getActInd() {
		return actInd;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public String getCreUid() {
		return creUid;
	}

	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public int getWithdrawQty2() {
		return withdrawQty2;
	}

	public void setWithdrawQty2(int withdrawQty2) {
		this.withdrawQty2 = withdrawQty2;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
}
