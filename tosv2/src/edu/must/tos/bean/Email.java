package edu.must.tos.bean;

public class Email {

	private String emMailType = null;
	private String emSender = null;
	private String emSubject = null;
	private String emCc = null;
	private String emBcc = null;
	private String emActInd = null;
	private String emContent = null;

	public String getEmMailType() {
		return emMailType;
	}

	public void setEmMailType(String emMailType) {
		this.emMailType = emMailType;
	}

	public String getEmSender() {
		return emSender;
	}

	public void setEmSender(String emSender) {
		this.emSender = emSender;
	}

	public String getEmSubject() {
		return emSubject;
	}

	public void setEmSubject(String emSubject) {
		this.emSubject = emSubject;
	}

	public String getEmCc() {
		return emCc;
	}

	public void setEmCc(String emCc) {
		this.emCc = emCc;
	}

	public String getEmBcc() {
		return emBcc;
	}

	public void setEmBcc(String emBcc) {
		this.emBcc = emBcc;
	}

	public String getEmActInd() {
		return emActInd;
	}

	public void setEmActInd(String emActInd) {
		this.emActInd = emActInd;
	}

	public String getEmContent() {
		return emContent;
	}

	public void setEmContent(String emContent) {
		this.emContent = emContent;
	}
}
