package edu.must.tos.bean;

public class NewCourseInfo {

	private String intake;

	private String region;

	private String facultyCode;

	private String programCode;
	
	private String majorCode;

	private String status;

	private String isbn;

	private String courseCode;

	private String awardType;

	public String getIntake() {
		return intake;
	}

	public String getRegion() {
		return region;
	}

	public String getFacultyCode() {
		return facultyCode;
	}

	public String getProgramCode() {
		return programCode;
	}

	public String getStatus() {
		return status;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setIntake(String intake) {
		this.intake = intake;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setFacultyCode(String facultyCode) {
		this.facultyCode = facultyCode;
	}

	public void setProgramCode(String programCode) {
		this.programCode = programCode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}
}
