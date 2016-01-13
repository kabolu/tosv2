package edu.must.tos.bean;

public class NewsOrder {

	private String intake;

	private String studentNo;

	private String applicantNo;

	private double paidAmount;

	private String paidCurrency;

	private String paidStatus;

	private String actind;

	private String creUid;

	public String getStudentNo() {
		return studentNo;
	}

	public String getApplicantNo() {
		return applicantNo;
	}

	public String getActind() {
		return actind;
	}

	public String getCreUid() {
		return creUid;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	public void setActind(String actind) {
		this.actind = actind;
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

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}
}
