package com.omnet.cnt.Model;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Enemy {
	@Id
    private String commitNo;
    private String enemyCommitNo;
    private String sbiNo;
    private String enemyNameFirst;
    private String enemyNameLast;
    private String enemyNameMiddle;
    private String enemyNameSuffix;
    private String reportingSourceCode;
    private Date inmtSourceDt;
    private String inmtEnmyTp;
    private String inmtActiveFlg;
    private String inmtVrfyFlg;
    private String enemyComment;
    private String enmyRowid;
    private String enemyStatus;
    
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public String getEnemyCommitNo() {
		return enemyCommitNo;
	}
	public void setEnemyCommitNo(String enemyCommitNo) {
		this.enemyCommitNo = enemyCommitNo;
	}
	public String getSbiNo() {
		return sbiNo;
	}
	public void setSbiNo(String sbiNo) {
		this.sbiNo = sbiNo;
	}
	public String getEnemyNameFirst() {
		return enemyNameFirst;
	}
	public void setEnemyNameFirst(String enemyNameFirst) {
		this.enemyNameFirst = enemyNameFirst;
	}
	public String getEnemyNameLast() {
		return enemyNameLast;
	}
	public void setEnemyNameLast(String enemyNameLast) {
		this.enemyNameLast = enemyNameLast;
	}
	public String getEnemyNameMiddle() {
		return enemyNameMiddle;
	}
	public void setEnemyNameMiddle(String enemyNameMiddle) {
		this.enemyNameMiddle = enemyNameMiddle;
	}
	public String getEnemyNameSuffix() {
		return enemyNameSuffix;
	}
	public void setEnemyNameSuffix(String enemyNameSuffix) {
		this.enemyNameSuffix = enemyNameSuffix;
	}
	public String getReportingSourceCode() {
		return reportingSourceCode;
	}
	public void setReportingSourceCode(String reportingSourceCode) {
		this.reportingSourceCode = reportingSourceCode;
	}
	public Date getInmtSourceDt() {
		return inmtSourceDt;
	}
	public void setInmtSourceDt(Date inmtSourceDt) {
		this.inmtSourceDt = inmtSourceDt;
	}
	public String getInmtEnmyTp() {
		return inmtEnmyTp;
	}
	public void setInmtEnmyTp(String inmtEnmyTp) {
		this.inmtEnmyTp = inmtEnmyTp;
	}
	public String getInmtActiveFlg() {
		return inmtActiveFlg;
	}
	public void setInmtActiveFlg(String inmtActiveFlg) {
		this.inmtActiveFlg = inmtActiveFlg;
	}
	public String getInmtVrfyFlg() {
		return inmtVrfyFlg;
	}
	public void setInmtVrfyFlg(String inmtVrfyFlg) {
		this.inmtVrfyFlg = inmtVrfyFlg;
	}
	public String getEnemyComment() {
		return enemyComment;
	}
	public void setEnemyComment(String enemyComment) {
		this.enemyComment = enemyComment;
	}
	public String getEnmyRowid() {
		return enmyRowid;
	}
	public void setEnmyRowid(String enmyRowid) {
		this.enmyRowid = enmyRowid;
	}
	public String getEnemyStatus() {
		return enemyStatus;
	}
	public void setEnemyStatus(String enemyStatus) {
		this.enemyStatus = enemyStatus;
	}
	public Enemy(String commitNo, String enemyCommitNo, String sbiNo, String enemyNameFirst, String enemyNameLast,
			String enemyNameMiddle, String enemyNameSuffix, String reportingSourceCode, Date inmtSourceDt,
			String inmtEnmyTp, String inmtActiveFlg, String inmtVrfyFlg, String enemyComment, String enmyRowid,
			String enemyStatus) {
		super();
		this.commitNo = commitNo;
		this.enemyCommitNo = enemyCommitNo;
		this.sbiNo = sbiNo;
		this.enemyNameFirst = enemyNameFirst;
		this.enemyNameLast = enemyNameLast;
		this.enemyNameMiddle = enemyNameMiddle;
		this.enemyNameSuffix = enemyNameSuffix;
		this.reportingSourceCode = reportingSourceCode;
		this.inmtSourceDt = inmtSourceDt;
		this.inmtEnmyTp = inmtEnmyTp;
		this.inmtActiveFlg = inmtActiveFlg;
		this.inmtVrfyFlg = inmtVrfyFlg;
		this.enemyComment = enemyComment;
		this.enmyRowid = enmyRowid;
		this.enemyStatus = enemyStatus;
	}
	public Enemy() {
		super();
		// TODO Auto-generated constructor stub
	}
    

}
