package edu.must.tos.bean;

import java.util.Date;

public class BookPurchasing {

	private int id;
	
	private String intake;
	
	private String orderNo;
	
	private String isbn;
	
	private int quantity;
	
	private String orderPp;
	
	private String orderBy;
	
	private String remarks;
	
	private String issued;
	
	private String orderDate;
	
	private String actind;
	
	private String creUid;
	
	private Date creDate;
	
	private String updUid;
	
	private Date updDate;
	
	private int leave;
	
	private Double costPrice = new Double(0);

	public int getLeave() {
		return leave;
	}

	public void setLeave(int leave) {
		this.leave = leave;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getOrderPp() {
		return orderPp;
	}

	public void setOrderPp(String orderPp) {
		this.orderPp = orderPp;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getActind() {
		return actind;
	}

	public void setActind(String actind) {
		this.actind = actind;
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

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
}
