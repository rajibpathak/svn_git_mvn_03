package com.omnet.cnt.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Document: EmergencyAttention.java
 * Author: Jamal Abraar
 * Date Created: 20-Nov-2024
 * Last Updated: 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmergencyAttention {
	
	private String commitNo;
	private Integer emergencySeqNum;
	private Timestamp dateLogged;
	private String immediateAttnFlag;
	private String mentalHealthFlag;
	private String suicideFlag;
	private String threatToStaff;
	private String medicalComments;
	private String threatComments;
	
	public String getCommitNo() {
		return commitNo;
	}
	
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public Integer getEmergencySeqNum() {
		return emergencySeqNum;
	}

	public void setEmergencySeqNum(Integer emergencySeqNum) {
		this.emergencySeqNum = emergencySeqNum;
	}

	public Timestamp getDateLogged() {
		return dateLogged;
	}

	public void setDateLogged(Timestamp dateLogged) {
		this.dateLogged = dateLogged;
	}

	public String getImmediateAttnFlag() {
		return immediateAttnFlag;
	}

	public void setImmediateAttnFlag(String immediateAttnFlag) {
		this.immediateAttnFlag = immediateAttnFlag;
	}

	public String getMentalHealthFlag() {
		return mentalHealthFlag;
	}

	public void setMentalHealthFlag(String mentalHealthFlag) {
		this.mentalHealthFlag = mentalHealthFlag;
	}

	public String getSuicideFlag() {
		return suicideFlag;
	}

	public void setSuicideFlag(String suicideFlag) {
		this.suicideFlag = suicideFlag;
	}

	public String getThreatToStaff() {
		return threatToStaff;
	}

	public void setThreatToStaff(String threatToStaff) {
		this.threatToStaff = threatToStaff;
	}

	public String getMedicalComments() {
		return medicalComments;
	}

	public void setMedicalComments(String medicalComments) {
		this.medicalComments = medicalComments;
	}

	public String getThreatComments() {
		return threatComments;
	}

	public void setThreatComments(String threatComments) {
		this.threatComments = threatComments;
	}
	
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public static EmergencyAttention emergencyAttention(Map<String, Object> row) {
		EmergencyAttention emergencyAttentionData = new EmergencyAttention();
		
		emergencyAttentionData.setCommitNo((String) row.get("COMMIT_NO"));
	    BigDecimal emgySeqNum = (BigDecimal) row.get("EMERG_ATTN_SEQ_NUM");
	    emergencyAttentionData.setEmergencySeqNum(emgySeqNum != null ? emgySeqNum.intValue() : null);
		
		emergencyAttentionData.setDateLogged((Timestamp) row.get("DATE_LOGGED"));
		emergencyAttentionData.setImmediateAttnFlag((String) row.get("REQ_IMME_ATTN_FLAG"));
	    emergencyAttentionData.setMentalHealthFlag((String) row.get("MENTAL_HEALTH_CON_FLAG"));
	    emergencyAttentionData.setSuicideFlag((String) row.get("SUICIDE_REM_FLAG"));
	    emergencyAttentionData.setThreatToStaff((String) row.get("THREAT_TO_STAFF_FLAG"));
	    emergencyAttentionData.setMedicalComments((String) row.get("MEDICAL_COMMENTS"));
	    emergencyAttentionData.setThreatComments((String) row.get("THREAT_COMMENTS"));
	    
	    return emergencyAttentionData;
	}
}
