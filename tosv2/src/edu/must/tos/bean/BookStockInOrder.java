package edu.must.tos.bean;

import java.util.Date;

public class BookStockInOrder {

	private Integer stockInNo;

	private String intake;

	private String prnum;

	private Integer supplierNo;

	private String paidstatus;

	private String paidcurrency;

	private String paidDate;

	private String inDate;

	private String invoiceDate;

	private String actind;

	private Date creDate;

	private String creUid;

	private Date updDate;

	private String updUid;

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

	public String getPaidcurrency() {
		return paidcurrency;
	}

	public void setPaidcurrency(String paidcurrency) {
		this.paidcurrency = paidcurrency;
	}

	public String getPaidstatus() {
		return paidstatus;
	}

	public void setPaidstatus(String paidstatus) {
		this.paidstatus = paidstatus;
	}

	public String getPrnum() {
		return prnum;
	}

	public void setPrnum(String prnum) {
		this.prnum = prnum;
	}

	public Integer getStockInNo() {
		return stockInNo;
	}

	public void setStockInNo(Integer stockInNo) {
		this.stockInNo = stockInNo;
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

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
}
