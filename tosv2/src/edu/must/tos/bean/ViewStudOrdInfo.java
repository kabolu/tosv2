package edu.must.tos.bean;

import java.util.Date;

public class ViewStudOrdInfo {

	private String chineseName;

	private String studentNo;

	private String applicantNo;

	private String facultyName;

	private String program;

	private int academicYear;

	private String orderSeqNo;

	private String paidCurrency;

	private Double paidAmount;

	private Date paidDate;

	private String cashier;

	// summary
	private int totalQty;

	private Double totalFutPriceMOP;

	private Double totalFutPriceRMB;

	private Double totalNetPriceMOP;

	private Double totalNetPriceRMB;

	private int totalNotEngQty;

	private Double totalNotEngPriceMOP;

	private Double totalNotEngPriceRMB;

	private Double amercemount;

	private Double fineforlatepay;

	private Double difference;

	private Double totalWithdrawPriceMop;

	private Double totalWithdrawPriceRmb;

	private double shippingFee;

	// detail
	private String title;

	private String author;

	private String publisher;

	private String edition;

	private String publishYear;

	private String isbn;

	private int orderEdQty;

	private int notEnoughQty;

	private Double futurePriceMOP;

	private Double futurePriceRMB;

	private Double payfuturepricemop;

	private Double payfuturepricermb;

	private Double paynetpricemop;

	private Double paynetpricermb;

	private Double netPriceMOP;

	private Double netPriceRMB;

	private Double withdrawpriceMOP;

	private Double withdrawpriceRMB;

	private Date upddate;

	private String upduid;

	private int withdrawqty2;

	// deliver
	private int receivedQty ;
	
	private Date bookDeliverDate;

	private String bookDeliver;

	private String contactNo;

	private String email;

	private String status;
	
	private String newContactNo ;
	
	private String newEmail ;

	public String getChineseName() {
		return chineseName;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public String getApplicantNo() {
		return applicantNo;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public String getProgram() {
		return program;
	}

	public int getAcademicYear() {
		return academicYear;
	}

	public String getOrderSeqNo() {
		return orderSeqNo;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public String getCashier() {
		return cashier;
	}

	public int getTotalQty() {
		return totalQty;
	}

	public Double getTotalFutPriceMOP() {
		return totalFutPriceMOP;
	}

	public Double getTotalFutPriceRMB() {
		return totalFutPriceRMB;
	}

	public Double getTotalNetPriceMOP() {
		return totalNetPriceMOP;
	}

	public Double getTotalNetPriceRMB() {
		return totalNetPriceRMB;
	}

	public int getTotalNotEngQty() {
		return totalNotEngQty;
	}

	public Double getTotalNotEngPriceMOP() {
		return totalNotEngPriceMOP;
	}

	public Double getTotalNotEngPriceRMB() {
		return totalNotEngPriceRMB;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getEdition() {
		return edition;
	}

	public String getPublishYear() {
		return publishYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public int getOrderEdQty() {
		return orderEdQty;
	}

	public int getNotEnoughQty() {
		return notEnoughQty;
	}

	public Double getFuturePriceMOP() {
		return futurePriceMOP;
	}

	public Double getFuturePriceRMB() {
		return futurePriceRMB;
	}

	public Double getNetPriceMOP() {
		return netPriceMOP;
	}

	public Double getNetPriceRMB() {
		return netPriceRMB;
	}

	public Date getBookDeliverDate() {
		return bookDeliverDate;
	}

	public String getBookDeliver() {
		return bookDeliver;
	}

	public String getContactNo() {
		return contactNo;
	}

	public String getEmail() {
		return email;
	}

	public String getStatus() {
		return status;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setApplicantNo(String applicantNo) {
		this.applicantNo = applicantNo;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public void setAcademicYear(int academicYear) {
		this.academicYear = academicYear;
	}

	public void setOrderSeqNo(String orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public void setTotalQty(int totalQty) {
		this.totalQty = totalQty;
	}

	public void setTotalFutPriceMOP(Double totalFutPriceMOP) {
		this.totalFutPriceMOP = totalFutPriceMOP;
	}

	public void setTotalFutPriceRMB(Double totalFutPriceRMB) {
		this.totalFutPriceRMB = totalFutPriceRMB;
	}

	public void setTotalNetPriceMOP(Double totalNetPriceMOP) {
		this.totalNetPriceMOP = totalNetPriceMOP;
	}

	public void setTotalNetPriceRMB(Double totalNetPriceRMB) {
		this.totalNetPriceRMB = totalNetPriceRMB;
	}

	public void setTotalNotEngQty(int totalNotEngQty) {
		this.totalNotEngQty = totalNotEngQty;
	}

	public void setTotalNotEngPriceMOP(Double totalNotEngPriceMOP) {
		this.totalNotEngPriceMOP = totalNotEngPriceMOP;
	}

	public void setTotalNotEngPriceRMB(Double totalNotEngPriceRMB) {
		this.totalNotEngPriceRMB = totalNotEngPriceRMB;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public void setPublishYear(String publishYear) {
		this.publishYear = publishYear;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setOrderEdQty(int orderEdQty) {
		this.orderEdQty = orderEdQty;
	}

	public void setNotEnoughQty(int notEnoughQty) {
		this.notEnoughQty = notEnoughQty;
	}

	public void setFuturePriceMOP(Double futurePriceMOP) {
		this.futurePriceMOP = futurePriceMOP;
	}

	public void setFuturePriceRMB(Double futurePriceRMB) {
		this.futurePriceRMB = futurePriceRMB;
	}

	public void setNetPriceMOP(Double netPriceMOP) {
		this.netPriceMOP = netPriceMOP;
	}

	public void setNetPriceRMB(Double netPriceRMB) {
		this.netPriceRMB = netPriceRMB;
	}

	public void setBookDeliverDate(Date bookDeliverDate) {
		this.bookDeliverDate = bookDeliverDate;
	}

	public void setBookDeliver(String bookDeliver) {
		this.bookDeliver = bookDeliver;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmercemount() {
		return amercemount;
	}

	public Double getFineforlatepay() {
		return fineforlatepay;
	}

	public void setAmercemount(Double amercemount) {
		this.amercemount = amercemount;
	}

	public void setFineforlatepay(Double fineforlatepay) {
		this.fineforlatepay = fineforlatepay;
	}

	public Double getPayfuturepricemop() {
		return payfuturepricemop;
	}

	public Double getPayfuturepricermb() {
		return payfuturepricermb;
	}

	public void setPayfuturepricemop(Double payfuturepricemop) {
		this.payfuturepricemop = payfuturepricemop;
	}

	public void setPayfuturepricermb(Double payfuturepricermb) {
		this.payfuturepricermb = payfuturepricermb;
	}

	public Double getTotalWithdrawPriceMop() {
		return totalWithdrawPriceMop;
	}

	public Double getTotalWithdrawPriceRmb() {
		return totalWithdrawPriceRmb;
	}

	public void setTotalWithdrawPriceMop(Double totalWithdrawPriceMop) {
		this.totalWithdrawPriceMop = totalWithdrawPriceMop;
	}

	public void setTotalWithdrawPriceRmb(Double totalWithdrawPriceRmb) {
		this.totalWithdrawPriceRmb = totalWithdrawPriceRmb;
	}

	public Double getWithdrawpriceMOP() {
		return withdrawpriceMOP;
	}

	public Double getWithdrawpriceRMB() {
		return withdrawpriceRMB;
	}

	public void setWithdrawpriceMOP(Double withdrawpriceMOP) {
		this.withdrawpriceMOP = withdrawpriceMOP;
	}

	public void setWithdrawpriceRMB(Double withdrawpriceRMB) {
		this.withdrawpriceRMB = withdrawpriceRMB;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public Date getUpddate() {
		return upddate;
	}

	public String getUpduid() {
		return upduid;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public void setUpduid(String upduid) {
		this.upduid = upduid;
	}

	public int getWithdrawqty2() {
		return withdrawqty2;
	}

	public void setWithdrawqty2(int withdrawqty2) {
		this.withdrawqty2 = withdrawqty2;
	}

	public Double getPaynetpricemop() {
		return paynetpricemop;
	}

	public Double getPaynetpricermb() {
		return paynetpricermb;
	}

	public void setPaynetpricemop(Double paynetpricemop) {
		this.paynetpricemop = paynetpricemop;
	}

	public void setPaynetpricermb(Double paynetpricermb) {
		this.paynetpricermb = paynetpricermb;
	}

	public double getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(double shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getNewContactNo() {
		return newContactNo;
	}

	public void setNewContactNo(String newContactNo) {
		this.newContactNo = newContactNo;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public int getReceivedQty() {
		return receivedQty;
	}

	public void setReceivedQty(int receivedQty) {
		this.receivedQty = receivedQty;
	}
}
