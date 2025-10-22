package com.omnet.cnt.Model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INM_RH_RSN_STATUS")
public class Reasonstatus {
	    @Id
	    @Column(name = "RH_RSN_STATUS_SEQ_NUM")
	    private Long id; // Assuming this is a sequence-generated ID

	    @Column(name = "COMMIT_NO", nullable = false)
	    private String commitNo;

	    @Column(name = "RH_REASON_CD")
	    private String rhReasonCd;

	    @Column(name = "ACTIVE_DATE")
	    private Date activeDate;

	    @Column(name = "INACTIVE_DATE")
	    private Date inactiveDate;

	    @Column(name = "INSERTED_USER_ID", nullable = false, length = 8)
	    private String insertedUserId;

	    @Column(name = "INSERTED_DATE_TIME", nullable = false)
	    private Date insertedDateTime;

	    @Column(name = "UPDATED_USER_ID", nullable = false, length = 8)
	    private String updatedUserId;

	    @Column(name = "UPDATED_DATE_TIME", nullable = false)
	    private Date updatedDateTime;

	    @Column(name = "TERMINAL", nullable = false, length = 30)
	    private String terminal;

	    @Column(name = "SESSIONID", nullable = false)
	    private Long sessionId;

	    @Column(name = "ENTERED_BY_USERID", length = 8)
	    private String enteredByUserId;

	    @Column(name = "ENTERED_BY_DATE_TIME")
	    private Date enteredByDateTime;

	    @Column(name = "STAY_START_DATE")
	    private Date stayStartDate;

	    @Column(name = "BED_NO", length = 15)
	    private String bedNo;

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getCommitNo() {
	        return commitNo;
	    }

	    public void setCommitNo(String commitNo) {
	        this.commitNo = commitNo;
	    }

	    public String getRhReasonCd() {
	        return rhReasonCd;
	    }

	    public void setRhReasonCd(String rhReasonCd) {
	        this.rhReasonCd = rhReasonCd;
	    }

	    public Date getActiveDate() {
	        return activeDate;
	    }

	    public void setActiveDate(Date activeDate) {
	        this.activeDate = activeDate;
	    }

	    public Date getInactiveDate() {
	        return inactiveDate;
	    }

	    public void setInactiveDate(Date inactiveDate) {
	        this.inactiveDate = inactiveDate;
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

	    public String getEnteredByUserId() {
	        return enteredByUserId;
	    }

	    public void setEnteredByUserId(String enteredByUserId) {
	        this.enteredByUserId = enteredByUserId;
	    }

	    public Date getEnteredByDateTime() {
	        return enteredByDateTime;
	    }

	    public void setEnteredByDateTime(Date enteredByDateTime) {
	        this.enteredByDateTime = enteredByDateTime;
	    }

	    public Date getStayStartDate() {
	        return stayStartDate;
	    }

	    public void setStayStartDate(Date stayStartDate) {
	        this.stayStartDate = stayStartDate;
	    }

	    public String getBedNo() {
	        return bedNo;
	    }

	    public void setBedNo(String bedNo) {
	        this.bedNo = bedNo;
	    }
	}


