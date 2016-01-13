package edu.must.tos.bean;

import java.util.*;

public class Model {

	private Book book;

	private List<Price> price;

	private List<BookRel> bookRel;

	public Model(Book book, List price, List bookRel) {
		this.book = book;
		this.price = price;
		this.bookRel = bookRel;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}

	public void setPrice(List priceList) {
		this.price = priceList;
	}

	public void setEachPrice(Price p) {
		if (this.price == null)
			this.price = new ArrayList<Price>();
		price.add(p);
	}

	public List getPrice() {
		return price;
	}

	public void setEachBookRel(BookRel br) {
		if (this.bookRel == null)
			this.bookRel = new ArrayList<BookRel>();
		bookRel.add(br);
	}

	public void setBookRel(List bookRelList) {
		this.bookRel = bookRelList;
	}

	public List getBookRel() {
		return bookRel;
	}

}
