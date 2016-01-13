package edu.must.tos.bean;

public class BookInventoryInfo {

	private Integer no;

	private String isbn;

	private String PRnum;

	private String PRdate;

	private int PRsum;

	private int stock;

	private int adjnum;

	private String remarks;

	private double purchasePrice;

	private double discount;

	private double costPrice;

	private String invoiceDate;
	
	private Book book = new Book();

	public int getAdjnum() {
		return adjnum;
	}

	public void setAdjnum(int adjnum) {
		this.adjnum = adjnum;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPRdate() {
		return PRdate;
	}

	public void setPRdate(String rdate) {
		PRdate = rdate;
	}

	public String getPRnum() {
		return PRnum;
	}

	public void setPRnum(String rnum) {
		PRnum = rnum;
	}

	public int getPRsum() {
		return PRsum;
	}

	public void setPRsum(int rsum) {
		PRsum = rsum;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

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

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
}
