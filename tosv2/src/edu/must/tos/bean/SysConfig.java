/**
 * 
 */
package edu.must.tos.bean;

import java.util.Date;

/**
 * @author Wangjiabo
 * @version 1.0
 * @date 2008-11-07
 */

/**
 * system config information
 */
public class SysConfig {

	private String scType;

	private String scKey;

	private String scValue1;

	private String scValue2;

	private String scValue3;

	private String scChnDesc;

	private String scEngDesc;

	private String actInd;

	private String creUid;

	private Date creDate;

	private String updUid;

	private Date updDate;

	public String getScType() {
		return scType;
	}

	public void setScType(String scType) {
		this.scType = scType;
	}

	public String getScKey() {
		return scKey;
	}

	public void setScKey(String scKey) {
		this.scKey = scKey;
	}

	public String getScValue1() {
		return scValue1;
	}

	public void setScValue1(String scValue1) {
		this.scValue1 = scValue1;
	}

	public String getScValue2() {
		return scValue2;
	}

	public void setScValue2(String scValue2) {
		this.scValue2 = scValue2;
	}

	public String getScChnDesc() {
		return scChnDesc;
	}

	public void setScChnDesc(String scChnDesc) {
		this.scChnDesc = scChnDesc;
	}

	public String getScEngDesc() {
		return scEngDesc;
	}

	public void setScEngDesc(String scEngDesc) {
		this.scEngDesc = scEngDesc;
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

	public String getScValue3() {
		return scValue3;
	}

	public void setScValue3(String scValue3) {
		this.scValue3 = scValue3;
	}
}
