package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "GANG_RT")
public class Gangrt {
    @Id

    private String gangCode;

    @Column(name = "GANG_NAME", nullable = false, length = 30)
    private String gangName;

    @Column(name = "GANG_ACTIVITY_STATUS_CODE", nullable = false, length = 4)
    private String gangActivityStatusCode;

    @Column(name = "STATUS", nullable = true, length = 1)
    private String status;

    @Column(name = "GANG_ALTERNATE_CODE", nullable = true, length = 2)
    private String gangAlternateCode;

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

    // Getters and setters
    public String getGangCode() {
        return gangCode;
    }

    public void setGangCode(String gangCode) {
        this.gangCode = gangCode;
    }

    public String getGangName() {
        return gangName;
    }

    public void setGangName(String gangName) {
        this.gangName = gangName;
    }

    public String getGangActivityStatusCode() {
        return gangActivityStatusCode;
    }

    public void setGangActivityStatusCode(String gangActivityStatusCode) {
        this.gangActivityStatusCode = gangActivityStatusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGangAlternateCode() {
        return gangAlternateCode;
    }

    public void setGangAlternateCode(String gangAlternateCode) {
        this.gangAlternateCode = gangAlternateCode;
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

	public Gangrt(String gangCode, String gangName, String gangActivityStatusCode, String status,
			String gangAlternateCode, String insertedUserId, Date insertedDateTime, String updatedUserId,
			Date updatedDateTime, String terminal, Long sessionId) {
		super();
		this.gangCode = gangCode;
		this.gangName = gangName;
		this.gangActivityStatusCode = gangActivityStatusCode;
		this.status = status;
		this.gangAlternateCode = gangAlternateCode;
		this.insertedUserId = insertedUserId;
		this.insertedDateTime = insertedDateTime;
		this.updatedUserId = updatedUserId;
		this.updatedDateTime = updatedDateTime;
		this.terminal = terminal;
		this.sessionId = sessionId;
	}

	public Gangrt() {
		super();
	}
    
}
