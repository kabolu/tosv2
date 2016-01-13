package edu.must.tos.bean;

import java.util.Date;

public class BookStockOutOrder {

	private Integer stockOutNo;
	
	private String intake;
	
	private String prnum;
	
	private Integer supplierNo;
	
	private String outDate;
	
	private String actind;
	
	private Date creDate;
	
	private String creUid;
	
	private Date updDate;
	
	private String updUid;
	
	private String paidCurrency;
	
	private String isp;

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

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public String getPrnum() {
		return prnum;
	}

	public void setPrnum(String prnum) {
		this.prnum = prnum;
	}

	public Integer getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(Integer supplierNo) {
		this.supplierNo = supplierNo;
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

	public Integer getStockOutNo() {
		return stockOutNo;
	}

	public void setStockOutNo(Integer stockOutNo) {
		this.stockOutNo = stockOutNo;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getIsp() {
		return isp;
	}
}
