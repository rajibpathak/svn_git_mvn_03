	/**
	 * Document: MassOffenderMovement.java
	 * Author: Jamal Abraar
	 * Date Created: 05-Oct-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.classes;

import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MassOffenderMovement {
	
	@Column(name = "ROWID")
	private String movRowid;
	
	
	private String instNum;
	private String commitNo;
	private String sbiMstSbiNo;
	private String lastName;
	private String firstName;
	private String midName;
	private String surName;
	private Timestamp dateTimeOfDep;
	private Timestamp dateTimeOfArr;
	private String countLocCodeFrom;
	private String countLocCodeTo;
	private String countLocDesc;
	private String comments;
	private String inmateMoved;
	private String activityTypeDesc;
	private String houLocDesc;
	private String rgHouRel;

	
	public String getActivityTypeDesc() {
		return activityTypeDesc;
	}
	
	public void setActivityTypeDesc(String activityTypeDesc) {
		this.activityTypeDesc = activityTypeDesc;
	}

	public String getMovRowid() {
		return movRowid;
	}

	public void setMovRowid(String movRowid) {
		this.movRowid = movRowid;
	}

	public String getInstNum() {
		return instNum;
	}

	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public String getSbiMstSbiNo() {
		return sbiMstSbiNo;
	}

	public void setSbiMstSbiNo(String sbiMstSbiNo) {
		this.sbiMstSbiNo = sbiMstSbiNo;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMidName() {
		return midName;
	}

	public void setMidName(String midName) {
		this.midName = midName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public Timestamp getDateTimeOfDep() {
		return dateTimeOfDep;
	}

	public void setDateTimeOfDep(Timestamp dateTimeOfDep) {
		this.dateTimeOfDep = dateTimeOfDep;
	}

	public Timestamp getDateTimeOfArr() {
		return dateTimeOfArr;
	}

	public void setDateTimeOfArr(Timestamp dateTimeOfArr) {
		this.dateTimeOfArr = dateTimeOfArr;
	}

	public String getCountLocCodeFrom() {
		return countLocCodeFrom;
	}

	public void setCountLocCodeFrom(String countLocCodeFrom) {
		this.countLocCodeFrom = countLocCodeFrom;
	}

	public String getCountLocCodeTo() {
		return countLocCodeTo;
	}

	public void setCountLocCodeTo(String countLocCodeTo) {
		this.countLocCodeTo = countLocCodeTo;
	}
	
	public String getCountLocDesc() {
		return countLocDesc;
	}

	public void setCountLocDesc(String countLocDesc) {
		this.countLocDesc = countLocDesc;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInmateMoved() {
		return inmateMoved;
	}

	public void setInmateMoved(String inmateMoved) {
		this.inmateMoved = inmateMoved;
	}

	public String getHouLocDesc() {
		return houLocDesc;
	}

	public void setHouLocDesc(String houLocDesc) {
		this.houLocDesc = houLocDesc;
	}

	public String getRgHouRel() {
		return rgHouRel;
	}

	public void setRgHouRel(String rgHouRel) {
		this.rgHouRel = rgHouRel;
	}
	
	
	@JsonInclude(JsonInclude.Include.ALWAYS)
	public static MassOffenderMovement massOffenderMovement(Map<String, Object> row) {
		MassOffenderMovement massOffenderMovementData = new MassOffenderMovement();
		
		massOffenderMovementData.setMovRowid((String) row.get("ROWIDTOCHAR(MOV.ROWID)"));
	//	massOffenderMovementData.setMovRowid((String) row.get("ROWID"));
		massOffenderMovementData.setInstNum((String) row.get("INST_NUM"));
		massOffenderMovementData.setCommitNo((String) row.get("COMMIT_NO"));
		//System.out.println("here checking SBI_MST_SBI_NO : " + row.get("SBI_MST_SBI_NO"));
		//System.out.println("here checking COMMIT_LNAME : " + row.get("COMMIT_LNAME"));
		massOffenderMovementData.setSbiMstSbiNo((String) row.get("SBI_MST_SBI_NO"));
		massOffenderMovementData.setLastName((String) row.get("COMMIT_LNAME"));
		massOffenderMovementData.setFirstName((String) row.get("COMMIT_FNAME"));
		massOffenderMovementData.setMidName((String) row.get("COMMIT_MNAME"));
		massOffenderMovementData.setSurName((String) row.get("COMMIT_SNAME"));
		massOffenderMovementData.setDateTimeOfDep((Timestamp) row.get("DATE_TIME_OF_DEP"));
		massOffenderMovementData.setDateTimeOfArr((Timestamp) row.get("DATE_TIME_OF_ARR"));
		massOffenderMovementData.setCountLocCodeFrom((String) row.get("COUNT_LOC_CODE_FROM"));
		massOffenderMovementData.setCountLocCodeTo((String) row.get("COUNT_LOC_CODE_TO"));
		massOffenderMovementData.setCountLocDesc((String) row.get("COUNT_LOC_DESC"));
		massOffenderMovementData.setComments((String) row.get("COMMENTS"));
		massOffenderMovementData.setInmateMoved((String) row.get("INMATE_MOVED"));
		massOffenderMovementData.setActivityTypeDesc((String) row.get("ACTIVITY_TYPE_DESC"));
		massOffenderMovementData.setHouLocDesc((String) row.get("HOU_LOC_DESC"));
		massOffenderMovementData.setRgHouRel((String) row.get("RG_HOU_REL"));
	    
	    return massOffenderMovementData;
	}
	
}

