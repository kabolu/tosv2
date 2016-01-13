package edu.must.tos.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Stocktake {

	private String isbn;

	private int batch;

	private Timestamp stock_date;

	private int original_stock;

	private int after_stocktake;

	private int adjusted;

	private String upduid;

	private Date upddate;

	public int getAdjusted() {
		return adjusted;
	}

	public void setAdjusted(int adjusted) {
		this.adjusted = adjusted;
	}

	public int getAfter_stocktake() {
		return after_stocktake;
	}

	public void setAfter_stocktake(int after_stocktake) {
		this.after_stocktake = after_stocktake;
	}

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
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

	public Timestamp getStock_date() {
		return stock_date;
	}

	public void setStock_date(Timestamp stock_date) {
		this.stock_date = stock_date;
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
