package edu.must.tos.bean;

import java.util.Date;

public class OrderInfo {
	
	private String isbn;

	private String title;

	private String studentNo;

	private String courseCode;
	
	private String majorCode ;

	private String orderIntake;

	private String actInd;

	private int orderSeqNo;

	private int year;

	private int confirmQty;

	private int unconfirmQty;

	private int withdrawQty;

	private double mopFuturePrice;

	private double mopNetPrice;
	
	private double rmbFuturePrice;
	

	private String creUid;

	private String updUid;

	private Date creDate;

	private Date updDate;
	
	private String supplement;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
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

	public int getUnconfirmQty() {
		return unconfirmQty;
	}

	public void setUnconfirmQty(int unconfirmQty) {
		this.unconfirmQty = unconfirmQty;
	}

	public double getMopFuturePrice() {
		return mopFuturePrice;
	}

	public void setMopFuturePrice(double mopFuturePrice) {
		this.mopFuturePrice = mopFuturePrice;
	}

	public double getRmbFuturePrice() {
		return rmbFuturePrice;
	}

	public void setRmbFuturePrice(double rmbFuturePrice) {
		this.rmbFuturePrice = rmbFuturePrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getWithdrawQty() {
		return withdrawQty;
	}

	public void setWithdrawQty(int withdrawQty) {
		this.withdrawQty = withdrawQty;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getOrderIntake() {
		return orderIntake;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
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

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}
	
	public String getSupplement() {
		return supplement;
	}

	public void setSupplement(String supplement) {
		this.supplement = supplement;
	}

	public double getMopNetPrice() {
		return mopNetPrice;
	}

	public void setMopNetPrice(double mopNetPrice) {
		this.mopNetPrice = mopNetPrice;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
	
	
}
