package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SENTENCE_LEVEL")
public class SentenceLevel {
	@Id

    @Column(name = "SNTC_LVL_CD", length = 3)
    private String sntcLvlCd;

    @Column(name = "SNTC_LVL_DESC", length = 30)
    private String sntcLvlDesc;

    @Column(name = "INSERTED_USER_ID", length = 8)
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME")
    private Date insertedDateTime;

    @Column(name = "UPDATED_USER_ID", length = 8)
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME")
    private Date updatedDateTime;

    @Column(name = "TERMINAL", length = 30)
    private String terminal;
    
    @Column(name = "SESSIONID")
    private Long sessionId;
    
    

	
	public SentenceLevel(String sntcLvlCd, String sntcLvlDesc, String insertedUserId, Date insertedDateTime,
			String updatedUserId, Date updatedDateTime, String terminal, Long sessionId) {
		super();
		this.sntcLvlCd = sntcLvlCd;
		this.sntcLvlDesc = sntcLvlDesc;
		this.insertedUserId = insertedUserId;
		this.insertedDateTime = insertedDateTime;
		this.updatedUserId = updatedUserId;
		this.updatedDateTime = updatedDateTime;
		this.terminal = terminal;
		this.sessionId = sessionId;
	}

	public String getSntcLvlCd() {
		return sntcLvlCd;
	}

	public void setSntcLvlCd(String sntcLvlCd) {
		this.sntcLvlCd = sntcLvlCd;
	}

	public String getSntcLvlDesc() {
		return sntcLvlDesc;
	}

	public void setSntcLvlDesc(String sntcLvlDesc) {
		this.sntcLvlDesc = sntcLvlDesc;
	}

	public String getInsertedUserId() {
		return insertedUserId;
	}

	public void setInsertedUserId(String insertedUserId) {
		this.insertedUserId = insertedUserId;
	}

	public Date getInsertedDateTime() {
		return insertedDateTime;
	}

	public void setInsertedDateTime(Date insertedDateTime) {
		this.insertedDateTime = insertedDateTime;
	}

	public String getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
    

}
