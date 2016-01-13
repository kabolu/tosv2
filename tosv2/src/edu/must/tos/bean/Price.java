package edu.must.tos.bean;

import java.util.Date;

public class Price {

	private String isbn;

	private String currency;

	private double futurePrice;

	private double netPrice;

	private String creUid;

	private Date creDate;

	private String updUid;

	private Date updDate;

	private String actInd;

	private double withdrawPrice;

	private double mopFuturePrice;

	private double rmbFuturePrice;

	private double mopNetPrice;

	private double rmbNetPrice;

	private String intake;

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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getFuturePrice() {
		return futurePrice;
	}

	public double getNetPrice() {
		return netPrice;
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

	public double getWithdrawPrice() {
		return withdrawPrice;
	}

	public void setWithdrawPrice(double withdrawPrice) {
		this.withdrawPrice = withdrawPrice;
	}

	public void setFuturePrice(double futurePrice) {
		this.futurePrice = futurePrice;
	}

	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
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

	public double getMopNetPrice() {
		return mopNetPrice;
	}

	public void setMopNetPrice(double mopNetPrice) {
		this.mopNetPrice = mopNetPrice;
	}

	public double getRmbNetPrice() {
		return rmbNetPrice;
	}

	public void setRmbNetPrice(double rmbNetPrice) {
		this.rmbNetPrice = rmbNetPrice;
	}
}
