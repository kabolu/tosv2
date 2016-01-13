package edu.must.tos.bean;

import java.util.Date;

public class VBookFeeForAppBean {

	private String chinesename;

	private String englishname;

	private String facultycode;

	private String programcode;
	
	private String majorcode;

	private String acceptance;

	private String region;

	private String studentNo;

	private String applicantNo;

	private String paidIntake;

	private String paidCurrency;

	private double paidAmount;

	private double prePaid;

	private Date dueDate;
	
	//Order and Order Detail Info
	private String orderSeqNo;
	
	private double paidamount;
	
	private double vPaidAmount;
	
	private String paidstatus;
	
	private double amerceMount;
	
	private double fineForLatePay;
	
	private double difference;
	
	private double shippingFee;
	
	private String isbn;
	
	private int confirmQty;
	
	private int withdrawQty;
	
	private int withdrawqty2;
	
	private int notenoughqty;

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

	/**
	 * @return the acceptance
	 */
	public String getAcceptance() {
		return acceptance;
	}

	/**
	 * @param acceptance
	 *            the acceptance to set
	 */
	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	/**
	 * @return the chinesename
	 */
	public String getChinesename() {
		return chinesename;
	}

	/**
	 * @param chinesename
	 *            the chinesename to set
	 */
	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	/**
	 * @return the englishname
	 */
	public String getEnglishname() {
		return englishname;
	}

	/**
	 * @param englishname
	 *            the englishname to set
	 */
	public void setEnglishname(String englishname) {
		this.englishname = englishname;
	}

	/**
	 * @return the facultycode
	 */
	public String getFacultycode() {
		return facultycode;
	}

	/**
	 * @param facultycode
	 *            the facultycode to set
	 */
	public void setFacultycode(String facultycode) {
		this.facultycode = facultycode;
	}

	/**
	 * @return the programcode
	 */
	public String getProgramcode() {
		return programcode;
	}

	/**
	 * @param programcode
	 *            the programcode to set
	 */
	public void setProgramcode(String programcode) {
		this.programcode = programcode;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	public String getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(String orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public double getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(double paidamount) {
		this.paidamount = paidamount;
	}

	public String getPaidstatus() {
		return paidstatus;
	}

	public void setPaidstatus(String paidstatus) {
		this.paidstatus = paidstatus;
	}

	public double getAmerceMount() {
		return amerceMount;
	}

	public void setAmerceMount(double amerceMount) {
		this.amerceMount = amerceMount;
	}

	public double getFineForLatePay() {
		return fineForLatePay;
	}

	public void setFineForLatePay(double fineForLatePay) {
		this.fineForLatePay = fineForLatePay;
	}

	public double getDifference() {
		return difference;
	}

	public void setDifference(double difference) {
		this.difference = difference;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getConfirmQty() {
		return confirmQty;
	}

	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}

	public int getWithdrawQty() {
		return withdrawQty;
	}

	public void setWithdrawQty(int withdrawQty) {
		this.withdrawQty = withdrawQty;
	}

	public int getWithdrawqty2() {
		return withdrawqty2;
	}

	public void setWithdrawqty2(int withdrawqty2) {
		this.withdrawqty2 = withdrawqty2;
	}

	public int getNotenoughqty() {
		return notenoughqty;
	}

	public void setNotenoughqty(int notenoughqty) {
		this.notenoughqty = notenoughqty;
	}

	public double getvPaidAmount() {
		return vPaidAmount;
	}

	public void setvPaidAmount(double vPaidAmount) {
		this.vPaidAmount = vPaidAmount;
	}

	public String getMajorcode() {
		return majorcode;
	}

	public void setMajorcode(String majorcode) {
		this.majorcode = majorcode;
	}

}
