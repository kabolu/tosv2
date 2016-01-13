package edu.must.tos.bean;

public class BookPurchasingBean {

	private int id;
	
	private String orderNo;
	
	private Book book = new Book();
	
	private BookPurchasing bookPurchase = new BookPurchasing();
	
	private int totalQuantitySum;
	
	private int totalConfirmSum;
	
	private int pQuantity = 0;
	
	private String changeRemarks = null;

	public int getTotalQuantitySum() {
		return totalQuantitySum;
	}

	public void setTotalQuantitySum(int totalQuantitySum) {
		this.totalQuantitySum = totalQuantitySum;
	}

	public int getTotalConfirmSum() {
		return totalConfirmSum;
	}

	public void setTotalConfirmSum(int totalConfirmSum) {
		this.totalConfirmSum = totalConfirmSum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setBookPurchase(BookPurchasing bookPurchase) {
		this.bookPurchase = bookPurchase;
	}

	public BookPurchasing getBookPurchase() {
		return bookPurchase;
	}

	public int getpQuantity() {
		return pQuantity;
	}

	public void setpQuantity(int pQuantity) {
		this.pQuantity = pQuantity;
	}

	public String getChangeRemarks() {
		return changeRemarks;
	}

	public void setChangeRemarks(String changeRemarks) {
		this.changeRemarks = changeRemarks;
	}

}
