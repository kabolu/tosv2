package edu.must.tos.bean;

import java.util.Date;

public class BookPurchasingCond {

	private Date creDate;
	
	private String creUid;
	
	private String fromDate;
	
	private String toDate;
	
	private String orderBy;
	
	private String intake;
	
	public Date getCreDate() {
		return creDate;
	}
	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}
	public String getCreUid() {
		return creUid;
	}
	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getIntake() {
		return intake;
	}
	public void setIntake(String intake) {
		this.intake = intake;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
}
