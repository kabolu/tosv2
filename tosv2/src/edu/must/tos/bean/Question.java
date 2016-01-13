package edu.must.tos.bean;

import java.util.Date;

public class Question {

	private Integer id;
	
	private String question;
	
	private String answer;
	
	private String actInd;
	
	private String creUid;
	
	private Date creDate;
	
	private String updUid;
	
	private Date updDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
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
}
