package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class ContactPersonInsert {
	
	@Id
	@JsonProperty("commit_no")
	private String commit_no;
	@JsonProperty("CONT_SEQ_NO")
	private long CSMN_CONT_SEQ_NO;
	@JsonProperty("PRSN_SEQ_NO")
	private long CSMN_PRSN_SEQ_NO;
	@JsonProperty("RELATIONSHIP_SEQ_NUM")
	private long RELATIONSHIP_SEQ_NUM;
	@JsonProperty("cont_lname")
	private String cont_lname;
	@JsonProperty("cont_fname")
	private String cont_fname;
	@JsonProperty("cont_mname")
	private String cont_mname;
	@JsonProperty("cont_sname")
	private String cont_sname;
	@JsonProperty("RELATIONSHIP_CODE")
	private String RELATIONSHIP_CODE;
	
	public String getCommit_no() {
		return commit_no;
	}
	public void setCommit_no(String commit_no) {
		this.commit_no = commit_no;
	}
	public long getCSMN_CONT_SEQ_NO() {
		return CSMN_CONT_SEQ_NO;
	}
	public void setCSMN_CONT_SEQ_NO(long cSMN_CONT_SEQ_NO) {
		CSMN_CONT_SEQ_NO = cSMN_CONT_SEQ_NO;
	}
	public long getCSMN_PRSN_SEQ_NO() {
		return CSMN_PRSN_SEQ_NO;
	}
	public void setCSMN_PRSN_SEQ_NO(long cSMN_PRSN_SEQ_NO) {
		CSMN_PRSN_SEQ_NO = cSMN_PRSN_SEQ_NO;
	}
	public long getRELATIONSHIP_SEQ_NUM() {
		return RELATIONSHIP_SEQ_NUM;
	}
	public void setRELATIONSHIP_SEQ_NUM(long rELATIONSHIP_SEQ_NUM) {
		RELATIONSHIP_SEQ_NUM = rELATIONSHIP_SEQ_NUM;
	}
	public String getCont_lname() {
		return cont_lname;
	}
	public void setCont_lname(String cont_lname) {
		this.cont_lname = cont_lname;
	}
	public String getCont_fname() {
		return cont_fname;
	}
	public void setCont_fname(String cont_fname) {
		this.cont_fname = cont_fname;
	}
	public String getCont_mname() {
		return cont_mname;
	}
	public void setCont_mname(String cont_mname) {
		this.cont_mname = cont_mname;
	}
	public String getCont_sname() {
		return cont_sname;
	}
	public void setCont_sname(String cont_sname) {
		this.cont_sname = cont_sname;
	}
	public String getRELATIONSHIP_CODE() {
		return RELATIONSHIP_CODE;
	}
	public void setRELATIONSHIP_CODE(String rELATIONSHIP_CODE) {
		RELATIONSHIP_CODE = rELATIONSHIP_CODE;
	}
	
}
