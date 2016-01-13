package edu.must.tos.bean;

public class BookInventoryRecord {

	private String intake;

	private String isbn;

	private String title;
	
	private String supplierName = null;

	private int adjnumSum;
	
	private int outSum;

	private int stock;

	private int paidYSum;

	private int paidNSum;

	private int receiveNum;
	
	private int receiveANum;
	
	private int stocktake;
		
	private String currency;
	
	private Double unitPrice;

	private Double disCount;

	private Double favourablePrice;	

	public String getIntake() {
		return intake;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public int getAdjnumSum() {
		return adjnumSum;
	}

	public int getStock() {
		return stock;
	}

	public int getPaidYSum() {
		return paidYSum;
	}

	public int getPaidNSum() {
		return paidNSum;
	}

	public int getReceiveNum() {
		return receiveNum;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAdjnumSum(int adjnumSum) {
		this.adjnumSum = adjnumSum;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setPaidYSum(int paidYSum) {
		this.paidYSum = paidYSum;
	}

	public void setPaidNSum(int paidNSum) {
		this.paidNSum = paidNSum;
	}

	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}

	public int getOutSum() {
		return outSum;
	}

	public void setOutSum(int outSum) {
		this.outSum = outSum;
	}

	public int getStocktake() {
		return stocktake;
	}

	public void setStocktake(int stocktake) {
		this.stocktake = stocktake;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDisCount() {
		return disCount;
	}

	public void setDisCount(Double disCount) {
		this.disCount = disCount;
	}

	public Double getFavourablePrice() {
		return favourablePrice;
	}

	public void setFavourablePrice(Double favourablePrice) {
		this.favourablePrice = favourablePrice;
	}

	public int getReceiveANum() {
		return receiveANum;
	}

	public void setReceiveANum(int receiveANum) {
		this.receiveANum = receiveANum;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

}
