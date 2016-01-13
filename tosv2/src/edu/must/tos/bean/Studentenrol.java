package edu.must.tos.bean;

public class Studentenrol {

	private String studentNO;

	private String courseCode;

	private String courseIntake;

	private String classCode;

	private int credit;

	private String creditType;
	
	private String majorCode ;

	public String getStudentNO() {
		return studentNO;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getCourseIntake() {
		return courseIntake;
	}

	public String getClassCode() {
		return classCode;
	}

	public int getCredit() {
		return credit;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setStudentNO(String studentNO) {
		this.studentNO = studentNO;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setCourseIntake(String courseIntake) {
		this.courseIntake = courseIntake;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public String getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(String majorCode) {
		this.majorCode = majorCode;
	}

}
