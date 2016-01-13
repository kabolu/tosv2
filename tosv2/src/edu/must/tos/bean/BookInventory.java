package edu.must.tos.bean;

import java.util.Date;

public class BookInventory {

	private String isbn;

	private int stock;

	private Date upddate;

	private String upduid;

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Date getUpddate() {
		return upddate;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public String getUpduid() {
		return upduid;
	}

	public void setUpduid(String upduid) {
		this.upduid = upduid;
	}

}
