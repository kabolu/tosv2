package edu.must.tos.bean;

import java.util.Date;

public class SurveyUser {

	private Integer suRefNo = null;
	private Integer suSmRefNo = null;
	private String suType = null;
	private String suUser = null;
	private String suIntake = null;
	private String suActInd = null;
	private Date suCreDate = null;
	private String suCreUid = null;
	private Date suUpdDate = null;
	private String suUpdUid = null;
	private String suIsFinished = null;

	public Integer getSuRefNo() {
		return suRefNo;
	}

	public void setSuRefNo(Integer suRefNo) {
		this.suRefNo = suRefNo;
	}

	public Integer getSuSmRefNo() {
		return suSmRefNo;
	}

	public void setSuSmRefNo(Integer suSmRefNo) {
		this.suSmRefNo = suSmRefNo;
	}

	public String getSuType() {
		return suType;
	}

	public void setSuType(String suType) {
		this.suType = suType;
	}

	public String getSuUser() {
		return suUser;
	}

	public void setSuUser(String suUser) {
		this.suUser = suUser;
	}

	public String getSuIntake() {
		return suIntake;
	}

	public void setSuIntake(String suIntake) {
		this.suIntake = suIntake;
	}

	public String getSuActInd() {
		return suActInd;
	}

	public void setSuActInd(String suActInd) {
		this.suActInd = suActInd;
	}

	public Date getSuCreDate() {
		return suCreDate;
	}

	public void setSuCreDate(Date suCreDate) {
		this.suCreDate = suCreDate;
	}

	public String getSuCreUid() {
		return suCreUid;
	}

	public void setSuCreUid(String suCreUid) {
		this.suCreUid = suCreUid;
	}

	public Date getSuUpdDate() {
		return suUpdDate;
	}

	public void setSuUpdDate(Date suUpdDate) {
		this.suUpdDate = suUpdDate;
	}

	public String getSuUpdUid() {
		return suUpdUid;
	}

	public void setSuUpdUid(String suUpdUid) {
		this.suUpdUid = suUpdUid;
	}

	public String getSuIsFinished() {
		return suIsFinished;
	}

	public void setSuIsFinished(String suIsFinished) {
		this.suIsFinished = suIsFinished;
	}

}
