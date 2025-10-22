/*
Document   : Screen Setup Table
Author     : Jamal Abraar
last update: 25/10/2023
*/

package com.omnet.cnt.Model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Omnet_Screens")
public class Screens {

	@Id
	@Column(name = "Screen_Code", length = 10, nullable = false)
	private String screenCode;
	
	@Column(name = "Screen_Name", length = 100, nullable = false)
	private String screenName;
	
	@Column(name = "screen_url", length = 500)
	private String screenUrl;
	
	@Column(name = "Status", length = 1)
	private String status;
	
	@Column(name = "inserted_user_id", length = 30)
	private String insertedUserId;
	
	@Column(name = "inserted_date_time")
	private Timestamp insertedDateTime;
	
	@Column(name = "updated_user_id", length = 30)
	private String updatedUserId;
	
	@Column(name = "updated_date_time")
	private Timestamp updatedDateTime;
	
	// Getters and setters
	
	public String getScreenCode() {
	    return screenCode;
	}
	
	public void setScreenCode(String screenCode) {
	    this.screenCode = screenCode;
	}
	
	public String getScreenName() {
	    return screenName;
	}
	
	public void setScreenName(String screenName) {
	    this.screenName = screenName;
	}
	
	public String getScreenUrl() {
	    return screenUrl;
	}
	
	public void setScreenUrl(String screenUrl) {
	    this.screenUrl = screenUrl;
	}
	
	public String getStatus() {
	    return status;
	}
	
	public void setStatus(String status) {
	    this.status = status;
	}
	
	public String getInsertedUserId() {
	    return insertedUserId;
	}
	
	public void setInsertedUserId(String insertedUserId) {
	    this.insertedUserId = insertedUserId;
	}
	
	public Timestamp getInsertedDateTime() {
	    return insertedDateTime;
	}
	
	public void setInsertedDateTime(Timestamp insertedDateTime) {
	    this.insertedDateTime = insertedDateTime;
	}
	
	public String getUpdatedUserId() {
	    return updatedUserId;
	}
	
	public void setUpdatedUserId(String updatedUserId) {
	    this.updatedUserId = updatedUserId;
	}
	
	public Timestamp getUpdatedDateTime() {
	    return updatedDateTime;
	}
	
	public void setUpdatedDateTime(Timestamp updatedDateTime) {
	    this.updatedDateTime = updatedDateTime;
	}
	
	// Constructors
	
	public Screens(String screenCode, String screenName, String screenUrl, String status, String insertedUserId, Timestamp insertedDateTime) {
	    this.screenCode = screenCode;
	    this.screenName = screenName;
	    this.screenUrl = screenUrl;
	    this.status = status;
	    this.insertedUserId = insertedUserId;
	    this.insertedDateTime = insertedDateTime;
	}
	
	public Screens() {
	}
}
