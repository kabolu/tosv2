package edu.must.tos.bean;

public class BookStockOutBean {

	private Integer outNo = null;
	
	private String intake = null;
	
	private String prNum = null;
	
	private Integer adjNum = null;
	
	private Double purchasePrice = null;
	
	private Double disCount = null;
	
	private Double costPrice = null;
	
	private String remarks = null;
	
	private String isp = null;
	
	private String ispDate = null;
	
	private Integer supplierNo = null;
	
	private String supplierName = null;
	
	private String supplierEngName = null;
	
	private Book book = new Book();
	
	private Integer quantity = null;
	
	private Double amount = null;
	
	private String outDate = null;
	
	private String paidCurrency = null;
	
	private String isbn = null;
	
	private Integer purchaseLeave = null;
	
	private String courseCode = null;
	
	private String lectCode = null;


	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierEngName() {
		return supplierEngName;
	}

	public void setSupplierEngName(String supplierEngName) {
		this.supplierEngName = supplierEngName;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public String getIntake() {
		return intake;
	}

	public void setPrNum(String prNum) {
		this.prNum = prNum;
	}

	public String getPrNum() {
		return prNum;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setIspDate(String ispDate) {
		this.ispDate = ispDate;
	}

	public String getIspDate() {
		return ispDate;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getLectCode() {
		return lectCode;
	}

	public void setLectCode(String lectCode) {
		this.lectCode = lectCode;
	}

	public Integer getOutNo() {
		return outNo;
	}

	public void setOutNo(Integer outNo) {
		this.outNo = outNo;
	}

	public Integer getAdjNum() {
		return adjNum;
	}

	public void setAdjNum(Integer adjNum) {
		this.adjNum = adjNum;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Double getDisCount() {
		return disCount;
	}

	public void setDisCount(Double disCount) {
		this.disCount = disCount;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(Integer supplierNo) {
		this.supplierNo = supplierNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPurchaseLeave() {
		return purchaseLeave;
	}

	public void setPurchaseLeave(Integer purchaseLeave) {
		this.purchaseLeave = purchaseLeave;
	}
}
