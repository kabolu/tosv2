package edu.must.tos.bean;

public class DeliverSummary {

	private String date;
	
	private String upduid;
	
	private String programCode;
	
	private String programName;
	
	private String facultyCode;
	
	private int orderCount;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUpduid() {
		return upduid;
	}

	public void setUpduid(String upduid) {
		this.upduid = upduid;
	}

	public String getProgramCode() {
		return programCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

}
