package edu.must.tos.bean;

import java.util.Date;

public class Order {
	
	private String studentNo;

	private String orderIntake;

	private int orderSeqNo;

	private String paidCurrency;

	private double paidAmount;

	private String paidStatus;

	private String actInd;

	private String creUid;

	private Date creDate;

	private String updUid;

	private Date updDate;

	private String chaUid;

	private double fineforlatepay;

	private Double difference;

	private double amerceMount;

	private double shippingFee;
	
	private String remarks;
	
	private String netpaidcurrency = null;
	
	private Double netpaidamount = null;
	
	private Double currate = null;

	/**
	 * @return the shippingFee
	 */
	public double getShippingFee() {
		return shippingFee;
	}

	/**
	 * @param shippingFee
	 *            the shippingFee to set
	 */
	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public double getAmerceMount() {
		return amerceMount;
	}

	public void setAmerceMount(double amerceMount) {
		this.amerceMount = amerceMount;
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

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
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

	public String getActInd() {
		return actInd;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public double getFineforlatepay() {
		return fineforlatepay;
	}

	public void setFineforlatepay(double fineforlatepay) {
		this.fineforlatepay = fineforlatepay;
	}

	public String getChaUid() {
		return chaUid;
	}

	public void setChaUid(String chaUid) {
		this.chaUid = chaUid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNetpaidcurrency() {
		return netpaidcurrency;
	}

	public void setNetpaidcurrency(String netpaidcurrency) {
		this.netpaidcurrency = netpaidcurrency;
	}

	public Double getNetpaidamount() {
		return netpaidamount;
	}

	public void setNetpaidamount(Double netpaidamount) {
		this.netpaidamount = netpaidamount;
	}

	public Double getCurrate() {
		return currate;
	}

	public void setCurrate(Double currate) {
		this.currate = currate;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}
}
