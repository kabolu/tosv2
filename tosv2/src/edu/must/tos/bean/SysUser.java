package edu.must.tos.bean;

import java.util.Date;

public class SysUser {

	private String userId;

	private String passwd;

	private String actInd;

	// ---新增
	private String userName;

	private String department;

	private String status;

	private String role;

	private int times;

	private String remarks;

	private Date credate;

	private String creuid;

	private Date upddate;

	private String upduid;
	
	private String email;
	private String address;
	private String contactNo;
	private String faxNo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getActInd() {
		return actInd;
	}

	public void setActInd(String actInd) {
		this.actInd = actInd;
	}

	public String getUserName() {
		return userName;
	}

	public String getDepartment() {
		return department;
	}

	public String getStatus() {
		return status;
	}

	public String getRole() {
		return role;
	}

	public int getTimes() {
		return times;
	}

	public String getRemarks() {
		return remarks;
	}

	public Date getCredate() {
		return credate;
	}

	public String getCreuid() {
		return creuid;
	}

	public Date getUpddate() {
		return upddate;
	}

	public String getUpduid() {
		return upduid;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setCredate(Date credate) {
		this.credate = credate;
	}

	public void setCreuid(String creuid) {
		this.creuid = creuid;
	}

	public void setUpddate(Date upddate) {
		this.upddate = upddate;
	}

	public void setUpduid(String upduid) {
		this.upduid = upduid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
}
