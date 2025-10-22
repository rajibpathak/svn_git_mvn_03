package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class ISCDateApprovedCaseNotes {

	@Id
	@JsonProperty("COMMIT_NO")
	private String COMMIT_NO;
	private Date isc_requested_date_box;
	private String isc_approved_flag;
	public String getCOMMIT_NO() {
		return COMMIT_NO;
	}
	public void setCOMMIT_NO(String cOMMIT_NO) {
		COMMIT_NO = cOMMIT_NO;
	}
	public Date getIsc_requested_date_box() {
		return isc_requested_date_box;
	}
	public void setIsc_requested_date_box(Date isc_requested_date_box) {
		this.isc_requested_date_box = isc_requested_date_box;
	}
	public String getIsc_approved_flag() {
		return isc_approved_flag;
	}
	public void setIsc_approved_flag(String isc_approved_flag) {
		this.isc_approved_flag = isc_approved_flag;
	}
	
	
	
}
