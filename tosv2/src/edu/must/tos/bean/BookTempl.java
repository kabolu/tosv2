package edu.must.tos.bean;

import java.util.Date;

public class BookTempl {
	
	private String facultyCode = null;

	private String intake = null;

	private String courseCode = null;

	private Integer year = null;

	private String stud_grp = null;
	
	private String majorCode = null;

	private String actInd = null;

	private String creUid = null;

	private Date creDate = null;

	private String updUid = null;

	private Date updDate = null;

	public String getFacultyCode() {
		return facultyCode;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

	public String getIntake() {
		return intake;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getActInd() {
		return actInd;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public String getCreUid() {
		return creUid;
	}

	public void setCreUid(String creUid) {
		this.creUid = creUid;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getStud_grp() {
		return stud_grp;
	}

	public void setStud_grp(String stud_grp) {
		this.stud_grp = stud_grp;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
}
