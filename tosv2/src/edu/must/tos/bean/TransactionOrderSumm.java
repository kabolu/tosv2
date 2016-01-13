package edu.must.tos.bean;

public class TransactionOrderSumm {

	private String studentNo;
	
	private String orderSeqNo;
	
	private double mopAmount;
	
	private double rmbAmount;
	
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getOrderSeqNo() {
		return orderSeqNo;
	}
	public void setOrderSeqNo(String orderSeqNo) {
		this.orderSeqNo = orderSeqNo;
	}
	public double getMopAmount() {
		return mopAmount;
	}
	public void setMopAmount(double mopAmount) {
		this.mopAmount = mopAmount;
	}
	public double getRmbAmount() {
		return rmbAmount;
	}
	public void setRmbAmount(double rmbAmount) {
		this.rmbAmount = rmbAmount;
	}
	
}
