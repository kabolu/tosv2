package edu.must.tos.bean;

import java.util.List;

public class Difference {

	private String studentNo;

	private String applicantNo;

	private String studentCName;

	private String studentEName;

	private int orderSeqNo;
	
	private String paidStatus;

	private double paidAmount;

	private double shoulePayAmount;
	
	private double shouldPayMopAmount;

	private double difference;
	
	private double differenceMop;

	private String contact;

	private String email;

	private double amercemount;

	private double fineforlatepay;

	private String paidcurrency;

	private List isbnList;

	private List bookPriceList;

	public String getStudentNo() {
		return studentNo;
	}

	public String getStudentCName() {
		return studentCName;
	}

	public String getStudentEName() {
		return studentEName;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public double getShoulePayAmount() {
		return shoulePayAmount;
	}

	public double getDifference() {
		return difference;
	}

	public String getContact() {
		return contact;
	}

	public String getEmail() {
		return email;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setStudentCName(String studentCName) {
		this.studentCName = studentCName;
	}

	public void setStudentEName(String studentEName) {
		this.studentEName = studentEName;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public void setShoulePayAmount(double shoulePayAmount) {
		this.shoulePayAmount = shoulePayAmount;
	}

	public void setDifference(double difference) {
		this.difference = difference;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApplicantNo() {
		return applicantNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	public String getPaidcurrency() {
		return paidcurrency;
	}

	public void setPaidcurrency(String paidcurrency) {
		this.paidcurrency = paidcurrency;
	}

	public double getAmercemount() {
		return amercemount;
	}

	public double getFineforlatepay() {
		return fineforlatepay;
	}

	public void setAmercemount(double amercemount) {
		this.amercemount = amercemount;
	}

	public void setFineforlatepay(double fineforlatepay) {
		this.fineforlatepay = fineforlatepay;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public List getBookPriceList() {
		return bookPriceList;
	}

	public void setBookPriceList(List bookPriceList) {
		this.bookPriceList = bookPriceList;
	}

	public List getIsbnList() {
		return isbnList;
	}

	public void setIsbnList(List isbnList) {
		this.isbnList = isbnList;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public double getShouldPayMopAmount() {
		return shouldPayMopAmount;
	}

	public void setShouldPayMopAmount(double shouldPayMopAmount) {
		this.shouldPayMopAmount = shouldPayMopAmount;
	}

	public double getDifferenceMop() {
		return differenceMop;
	}

	public void setDifferenceMop(double differenceMop) {
		this.differenceMop = differenceMop;
	}
}
