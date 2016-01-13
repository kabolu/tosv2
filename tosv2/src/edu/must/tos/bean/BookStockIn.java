package edu.must.tos.bean;

import java.util.Date;

public class BookStockIn {

	private int no;

	private String intake;

	private String PRnum;

	private String isbn;

	private Date credate;

	private String creuid;

	private int adjnum;

	private String remarks;

	private double purchasePrice;

	private double discount;

	private double costPrice;

	private String actind;

	public int getAdjnum() {
		return adjnum;
	}

	public void setAdjnum(int adjnum) {
		this.adjnum = adjnum;
	}

	public Date getCredate() {
		return credate;
	}

	public void setCredate(Date credate) {
		this.credate = credate;
	}

	public String getCreuid() {
		return creuid;
	}

	public void setCreuid(String creuid) {
		this.creuid = creuid;
	}

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPRnum() {
		return PRnum;
	}

	public void setPRnum(String rnum) {
		PRnum = rnum;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getActind() {
		return actind;
	}

	public void setActind(String actind) {
		this.actind = actind;
	}
}
