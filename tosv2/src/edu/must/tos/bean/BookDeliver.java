package edu.must.tos.bean;

import java.util.Date;

public class BookDeliver {

	private int deliverNo;

	private String intake;

	private int orderSeqNo;

	private String studentNo;

	private String chineseName;

	private String englishName;

	private String isbn;

	private int quantity;

	private String actind;

	private Date creDate;

	private String creUid;

	private Date updDate;

	private String updUid;

	private String deliverDate;

	public int getDeliverNo() {
		return deliverNo;
	}

	public void setDeliverNo(int deliverNo) {
		this.deliverNo = deliverNo;
	}

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getActind() {
		return actind;
	}

	public void setActind(String actind) {
		this.actind = actind;
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

	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
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
}
