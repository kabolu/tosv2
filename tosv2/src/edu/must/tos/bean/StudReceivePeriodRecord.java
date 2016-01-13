package edu.must.tos.bean;

import java.util.Date;

public class StudReceivePeriodRecord {

	private String orderseqNo;

	private String orderIntake;

	private String studentNo;

	private String chineseName;

	private int academicyear;

	private String facultyCode;

	private String facChnName;

	private String programCode;

	private String proChnName;

	private String periodNo;

	private Date starttime;

	private Date endtime;
	
	private Integer count = null;
	
	private String type = null;
	
	private Integer maxNo = null;
	
	private Integer receivedNumber = null;
	
	private Integer leavingNumber = null;

	public String getOrderseqNo() {
		return orderseqNo;
	}

	public String getOrderIntake() {
		return orderIntake;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public String getChineseName() {
		return chineseName;
	}

	public int getAcademicyear() {
		return academicyear;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public String getFacChnName() {
		return facChnName;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getProChnName() {
		return proChnName;
	}

	public String getPeriodNo() {
		return periodNo;
	}

	public void setOrderseqNo(String orderseqNo) {
		this.orderseqNo = orderseqNo;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public void setAcademicyear(int academicyear) {
		this.academicyear = academicyear;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

	public void setFacChnName(String facChnName) {
		this.facChnName = facChnName;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public void setProChnName(String proChnName) {
		this.proChnName = proChnName;
	}

	public void setPeriodNo(String periodNo) {
		this.periodNo = periodNo;
	}

	public Date getStarttime() {
		return starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMaxNo() {
		return maxNo;
	}

	public void setMaxNo(Integer maxNo) {
		this.maxNo = maxNo;
	}

	public Integer getReceivedNumber() {
		return receivedNumber;
	}

	public void setReceivedNumber(Integer receivedNumber) {
		this.receivedNumber = receivedNumber;
	}

	public Integer getLeavingNumber() {
		return leavingNumber;
	}

	public void setLeavingNumber(Integer leavingNumber) {
		this.leavingNumber = leavingNumber;
	}

}
