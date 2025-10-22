package com.omnet.cnt.Model;

import java.sql.Timestamp;


public class warrantCheck {
	

	private String sbi_no;
	private String warrant_number;
	private String warrant_type;
	private Timestamp issue_date;
	private String complaint;
	private String stat_title;
	private String charge_seq;
	private String agency;
	private String agency_county;
	private String preparer_last;
	private String preparer_first;
	private String preparer_mi;
	private String preparer_suff;
	private String comments;
	
	
	
	public String getComplaint() {
		return complaint;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	public String getStat_title() {
		return stat_title;
	}
	public void setStat_title(String stat_title) {
		this.stat_title = stat_title;
	}
	public String getPreparer_last() {
		return preparer_last;
	}
	public void setPreparer_last(String preparer_last) {
		this.preparer_last = preparer_last;
	}
	public String getPreparer_first() {
		return preparer_first;
	}
	public void setPreparer_first(String preparer_first) {
		this.preparer_first = preparer_first;
	}
	public String getPreparer_mi() {
		return preparer_mi;
	}
	public void setPreparer_mi(String preparer_mi) {
		this.preparer_mi = preparer_mi;
	}
	public String getPreparer_suff() {
		return preparer_suff;
	}
	public void setPreparer_suff(String preparer_suff) {
		this.preparer_suff = preparer_suff;
	}
	public String getSbi_no() {
		return sbi_no;
	}
	public void setSbi_no(String sbi_no) {
		this.sbi_no = sbi_no;
	}
	public String getWarrant_number() {
		return warrant_number;
	}
	public void setWarrant_number(String warrant_number) {
		this.warrant_number = warrant_number;
	}
	public String getWarrant_type() {
		return warrant_type;
	}
	public void setWarrant_type(String warrant_type) {
		this.warrant_type = warrant_type;
	}
	public Timestamp getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(Timestamp issue_date) {
		this.issue_date = issue_date;
	}
	public String getCharge_seq() {
		return charge_seq;
	}
	public void setCharge_seq(String charge_seq) {
		this.charge_seq = charge_seq;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public String getAgency_county() {
		return agency_county;
	}
	public void setAgency_county(String agency_county) {
		this.agency_county = agency_county;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	
	

}
