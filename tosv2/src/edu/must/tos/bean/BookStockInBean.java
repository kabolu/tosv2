package edu.must.tos.bean;

public class BookStockInBean {

	private String supplierName;

	private String intake;

	private String prNum;

	private String indate;

	private String paidStatus;

	private String paiddate;

	private String paidCurrency;

	private String invoiceDate;
	
	private Integer no;

	private String isbn;

	private double purchasePrice;

	private double discount;

	private double costPrice;

	private Integer adjNum;

	private double totalPrice;

	private String remarks;
	
	private String actind;
	
	private Book book = new Book();
	
	private BookStockIn bookStockIn = new BookStockIn();

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getIndate() {
		return indate;
	}

	public void setIndate(String indate) {
		this.indate = indate;
	}

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

	public String getPaiddate() {
		return paiddate;
	}

	public void setPaiddate(String paiddate) {
		this.paiddate = paiddate;
	}

	public String getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(String paidStatus) {
		this.paidStatus = paidStatus;
	}

	public String getPrNum() {
		return prNum;
	}

	public void setPrNum(String prNum) {
		this.prNum = prNum;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getAdjNum() {
		return adjNum;
	}

	public void setAdjNum(Integer adjNum) {
		this.adjNum = adjNum;
	}

	public String getPaidCurrency() {
		return paidCurrency;
	}

	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public BookStockIn getBookStockIn() {
		return bookStockIn;
	}

	public void setBookStockIn(BookStockIn bookStockIn) {
		this.bookStockIn = bookStockIn;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getActind() {
		return actind;
	}

	public void setActind(String actind) {
		this.actind = actind;
	}

}
