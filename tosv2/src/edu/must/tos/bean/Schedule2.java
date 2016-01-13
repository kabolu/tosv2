package edu.must.tos.bean;

import java.util.Date;

public class Schedule2 {

	private String studentNo;

	private String intake;

	private String periodNo;

	private String type;

	private String actInd;

	private String creUid;

	private Date credDate;

	private String updUid;

	private Date updDate;
	
	private int orderSeqNo;

	public String getStudentNo() {
		return studentNo;
	}

	public String getIntake() {
		return intake;
	}

	public String getPeriodNo() {
		return periodNo;
	}

	public String getType() {
		return type;
	}

	public String getActInd() {
		return actInd;
	}

	public String getCreUid() {
		return creUid;
	}

	public Date getCredDate() {
		return credDate;
	}

	public String getUpdUid() {
		return updUid;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public void setPeriodNo(String periodNo) {
		this.periodNo = periodNo;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}

	public void setCredDate(Date credDate) {
		this.credDate = credDate;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}
}
