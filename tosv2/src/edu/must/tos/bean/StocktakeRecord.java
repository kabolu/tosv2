package edu.must.tos.bean;

public class StocktakeRecord {

	private String isbn;

	private String title;

	private String author;

	private String edition;

	private String publisher;

	private String publishYear;

	private int after_stocktake1;

	private int adjnumsum;

	private int original_stock;

	private int after_stocktake2;

	private int stock;

	public int getAdjnumsum() {
		return adjnumsum;
	}

	public void setAdjnumsum(int adjnumsum) {
		this.adjnumsum = adjnumsum;
	}

	public int getAfter_stocktake1() {
		return after_stocktake1;
	}

	public void setAfter_stocktake1(int after_stocktake1) {
		this.after_stocktake1 = after_stocktake1;
	}

	public int getAfter_stocktake2() {
		return after_stocktake2;
	}

	public void setAfter_stocktake2(int after_stocktake2) {
		this.after_stocktake2 = after_stocktake2;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getOriginal_stock() {
		return original_stock;
	}

	public void setOriginal_stock(int original_stock) {
		this.original_stock = original_stock;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishYear() {
		return publishYear;
	}

	public void setPublishYear(String publishYear) {
		this.publishYear = publishYear;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
