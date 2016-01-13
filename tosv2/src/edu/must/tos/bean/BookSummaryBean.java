package edu.must.tos.bean;

public class BookSummaryBean {

	private Book book = new Book();
		
	private String orderIntake = null;
	
	private int confirmQty = 0;
	
	private int unconfirmTake = 0;
	
	private int quantity = 0;
	
	private int befConfirmQty = 0;
	
	public String getOrderIntake() {
		return orderIntake;
	}
	
	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}
	
	public int getConfirmQty() {
		return confirmQty;
	}
	
	public void setConfirmQty(int confirmQty) {
		this.confirmQty = confirmQty;
	}
	
	public int getUnconfirmTake() {
		return unconfirmTake;
	}
	
	public void setUnconfirmTake(int unconfirmTake) {
		this.unconfirmTake = unconfirmTake;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public Book getBook() {
		return book;
	}

	public int getBefConfirmQty() {
		return befConfirmQty;
	}

	public void setBefConfirmQty(int befConfirmQty) {
		this.befConfirmQty = befConfirmQty;
	}
}
