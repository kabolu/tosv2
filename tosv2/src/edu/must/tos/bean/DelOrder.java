package edu.must.tos.bean;

/**
 * @author Administrator
 * 
 */
public class DelOrder {
	
	private int orderSeqNo;

	private String studentNo;

	private String isbn;

	private String title;

	private String orderIntake;

	private int courseBookCount;

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
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

	public String getOrderIntake() {
		return orderIntake;
	}

	public void setOrderIntake(String orderIntake) {
		this.orderIntake = orderIntake;
	}

	public int getCourseBookCount() {
		return courseBookCount;
	}

	public void setCourseBookCount(int courseBookCount) {
		this.courseBookCount = courseBookCount;
	}

	public int getOrderSeqNo() {
		return orderSeqNo;
	}

	public void setOrderSeqNo(int orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}
}
