package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Amendment {
	
	@Id
	@JsonProperty("COMMIT_NO")
	private String commit_no;
	@JsonProperty("CSMN_CONT_SEQ_NO")
	private long CSMN_CONT_SEQ_NO;
	@JsonProperty("csmn_cont_comnt_amendment")
	private String csmn_cont_comnt_amendment;
	public String getCOMMIT_NO() {
		return commit_no;
	}
	public void setCOMMIT_NO(String cOMMIT_NO) {
		commit_no = cOMMIT_NO;
	}
	public long getCSMN_CONT_SEQ_NO() {
		return CSMN_CONT_SEQ_NO;
	}
	public void setCSMN_CONT_SEQ_NO(long cSMN_CONT_SEQ_NO) {
		CSMN_CONT_SEQ_NO = cSMN_CONT_SEQ_NO;
	}
	public String getCsmn_cont_comnt_amendment() {
		return csmn_cont_comnt_amendment;
	}
	public void setCsmn_cont_comnt_amendment(String csmn_cont_comnt_amendment) {
		this.csmn_cont_comnt_amendment = csmn_cont_comnt_amendment;
	}
	
	
	
}
