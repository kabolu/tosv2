package edu.must.tos.bean;

import java.util.Date;

public class BookStockOut {

	private Integer outNo = null;

	private String intake = null;

	private String PRnum = null;

	private String isbn = null;

	private Date credate = null;

	private String creuid = null;

	private Integer adjnum = null;

	private String remarks = null;

	private Double purchasePrice = null;

	private Double discount = null;

	private Double costPrice = null;

	private String actind = null;
	
	private Date upddate = null;

	private String upduid = null;
	
	private String ispDate = null;
	
	private Integer purchaseId = null;
	
	private String courseCode = null;
	
	private String lectCode = null;

	public String getIspDate() {
		return ispDate;
	}

	public void setIspDate(String ispDate) {
		this.ispDate = ispDate;
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

	public String getActind() {
		return actind;
	}

	public void setActind(String actind) {
		this.actind = actind;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getUpduid() {
		return upduid;
	}

	public void setUpduid(String upduid) {
		this.upduid = upduid;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getLectCode() {
		return lectCode;
	}

	public void setLectCode(String lectCode) {
		this.lectCode = lectCode;
	}

	public Integer getOutNo() {
		return outNo;
	}

	public void setOutNo(Integer outNo) {
		this.outNo = outNo;
	}

	public Integer getAdjnum() {
		return adjnum;
	}

	public void setAdjnum(Integer adjnum) {
		this.adjnum = adjnum;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Integer purchaseId) {
		this.purchaseId = purchaseId;
	}
}
