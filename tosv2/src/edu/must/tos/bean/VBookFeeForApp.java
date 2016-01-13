package edu.must.tos.bean;

import java.util.Date;

public class VBookFeeForApp {

	private String studentNo;

	private String applicantNo;

	private String paidIntake;

	private String paidCurrency;

	private double paidAmount;

	private double prePaid;

	private Date dueDate;

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public String getApplicantNo() {
		return applicantNo;
	}

	public String getPaidIntake() {
		return paidIntake;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public double getPrePaid() {
		return prePaid;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	public void setPaidIntake(String paidIntake) {
		this.paidIntake = paidIntake;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public void setPrePaid(double prePaid) {
		this.prePaid = prePaid;
	}

}
