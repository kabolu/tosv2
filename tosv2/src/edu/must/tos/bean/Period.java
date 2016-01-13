package edu.must.tos.bean;

import java.util.Date;

public class Period {

	private int periodNo;

	private String intake;

	private String type;

	private Date startTime;

	private Date endTime;

	private int maxNo;

	private String actInd;

	private String creUid;

	private Date creDate;

	private String updUid;

	private Date updDate;

	public int getPeriodNo() {
		return periodNo;
	}

	public String getIntake() {
		return intake;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public int getMaxNo() {
		return maxNo;
	}

	public String getActInd() {
		return actInd;
	}

	public String getCreUid() {
		return creUid;
	}

	public Date getCreDate() {
		return creDate;
	}

	public String getUpdUid() {
		return updUid;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setPeriodNo(int periodNo) {
		this.periodNo = periodNo;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setMaxNo(int maxNo) {
		this.maxNo = maxNo;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
