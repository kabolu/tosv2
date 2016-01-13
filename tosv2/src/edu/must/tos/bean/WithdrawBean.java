package edu.must.tos.bean;

import java.util.Date;

public class WithdrawBean {

	private Integer whithdrawNo;

	private String orderIntake;

	private Integer orderSeqNo;

	private String studentNo;

	private String studentName;

	private String isbn;

	private String bookName;

	private Integer withdrawQty;

	private String cause;

	private Date creDate;

	private String creUid;

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getCreUid() {
		return creUid;
	}

	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(Integer orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public Integer getWhithdrawNo() {
		return whithdrawNo;
	}

	public void setWhithdrawNo(Integer whithdrawNo) {
		this.whithdrawNo = whithdrawNo;
	}

	public Integer getWithdrawQty() {
		return withdrawQty;
	}

	public void setWithdrawQty(Integer withdrawQty) {
		this.withdrawQty = withdrawQty;
	}

	public String getOrderIntake() {
		return orderIntake;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
}
