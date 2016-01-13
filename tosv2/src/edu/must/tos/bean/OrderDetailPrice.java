package edu.must.tos.bean;

public class OrderDetailPrice {

	private String studentno;
	
	private int orderseqno;
	
	private String isbn;
	
	private String title;
	
	private Integer confirmqty;
	
	private Integer withdrawqty;
	
	private Integer withdrawqty2;
	
	private double paidamount;
	
	private String paidcurrency;
	
	private double futureprice;
	
	private double netprice;
	
	private double withdrawprice;

	public String getStudentno() {
		return studentno;
	}

	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}

	public int getOrderseqno() {
		return orderseqno;
	}

	public void setOrderseqno(int orderseqno) {
		this.orderseqno = orderseqno;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getConfirmqty() {
		return confirmqty;
	}

	public void setConfirmqty(Integer confirmqty) {
		this.confirmqty = confirmqty;
	}

	public Integer getWithdrawqty() {
		return withdrawqty;
	}

	public void setWithdrawqty(Integer withdrawqty) {
		this.withdrawqty = withdrawqty;
	}

	public Integer getWithdrawqty2() {
		return withdrawqty2;
	}

	public void setWithdrawqty2(Integer withdrawqty2) {
		this.withdrawqty2 = withdrawqty2;
	}

	public double getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(double paidamount) {
		this.paidamount = paidamount;
	}

	public String getPaidcurrency() {
		return paidcurrency;
	}

	public void setPaidcurrency(String paidcurrency) {
		this.paidcurrency = paidcurrency;
	}

	public double getFutureprice() {
		return futureprice;
	}

	public void setFutureprice(double futureprice) {
		this.futureprice = futureprice;
	}

	public double getNetprice() {
		return netprice;
	}

	public void setNetprice(double netprice) {
		this.netprice = netprice;
	}

	public double getWithdrawprice() {
		return withdrawprice;
	}

	public void setWithdrawprice(double withdrawprice) {
		this.withdrawprice = withdrawprice;
	}
}
