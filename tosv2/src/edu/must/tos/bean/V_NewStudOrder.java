package edu.must.tos.bean;

import java.util.Date;

public class V_NewStudOrder {

	private String intake;

	private String studentNo;

	private String studentName;

	private String studEngName;

	private String facultyName;

	private String programName;
	
	private String majorCode;

	private int academicYear;

	private String applicantNo;

	private String acceptance;

	private String region;

	private String isbn;
	
	private double mopPrice;

	private String courseCode;

	private int confirmQty;

	private String paidCurrency;

	private double paidAmount;
	
	private Date creDate;
	
	private Date bookFeeCreDate;

	private Date bookFeeUpdDate;
	
	
	public Date getBookFeeCreDate() {
		return bookFeeCreDate;
	}

	public void setBookFeeCreDate(Date bookFeeCreDate) {
		this.bookFeeCreDate = bookFeeCreDate;
	}

	public Date getBookFeeUpdDate() {
		return bookFeeUpdDate;
	}

	public void setBookFeeUpdDate(Date bookFeeUpdDate) {
		this.bookFeeUpdDate = bookFeeUpdDate;
	}
	
	public String getIntake() {
		return intake;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public String getProgramName() {
		return programName;
	}

	public int getAcademicYear() {
		return academicYear;
	}

	public String getApplicantNo() {
		return applicantNo;
	}

	public String getAcceptance() {
		return acceptance;
	}

	public String getRegion() {
		return region;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public int getConfirmQty() {
		return confirmQty;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public void setAcademicYear(int academicYear) {
		this.academicYear = academicYear;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}

	public String getStudEngName() {
		return studEngName;
	}

	public void setStudEngName(String studEngName) {
		this.studEngName = studEngName;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

	public double getMopPrice() {
		return mopPrice;
	}

	public void setMopPrice(double mopPrice) {
		this.mopPrice = mopPrice;
	}
}
