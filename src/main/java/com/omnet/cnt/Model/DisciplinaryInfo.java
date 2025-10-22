package com.omnet.cnt.Model;

import javax.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "INCIDENT")
public class DisciplinaryInfo {
	@Id
    @Column(name = "INCIDENT_SEQ_NUM")
    private Long incidentSeqNum;
	
	@Column(name = "D_REPORT_NUM")
	private String reportNumber;


    @Column(name = "COMMIT_NO", length = 20)
    private String commitNo;

    @Column(name = "D_REPORT_DATE")
    private LocalDate dateOfOccurrence;

    @Column(name = "D_REPORT_TIME", length = 10)
    private String timeOfOccurrence;

    @Column(name = "LOCATION", length = 100)
    private String locationOfOccurrence;

    @Column(name = "DREPORT_STATUS", length = 20)
    private String status;

    @Column(name = "REASON_FOR_EXCLUSION", length = 255)
    private String reasonForNullification;

    @Column(name = "HOUSING_UNIT", length = 20)
    private String housingUnit;

    @Column(name = "CLASS1_VIOLATION", length = 100)
    private String class1Violation;

    @Column(name = "CLASS2_VIOLATION", length = 100)
    private String class2Violation;

    @Column(name = "SUMMARY_VIOLATION", length = 100)
    private String summaryViolation;

    @Column(name = "COMMENTS", length = 500)
    private String comments;

    @Column(name = "DSPN_VIOL_SMRY_DESC", length = 255)
    private String dspnViolSmryDesc;
    


	private String witnessFirstName;   
    private String witnessLastName;     
    private String witnessMiddleName;   
    private String witnessSuffix;       
    private String dspnWitnessDelFlag;  

    
    private String lastName;
    private String firstName;
    private String mi;
    private String suffix;
    private String staffAssaultFlag;
    private String staffInjuriesFlag;
    private String industrialAccidentFlag;
    private String inmateAssaultFlag;
    private String resultOfIncident;
    private String resultOfSearch;
    private String sexRelatedFlag;
    private String stgRelatedFlag;
    private String assaultTypeCode;

    private String evidenceDisposition;
    private LocalDate dispositionDate;
    private String actionTaken;
    private String weaponNote;

    private LocalDate enteredByDate;
    private String enteredByTime;
    private String enteredByTitle;
    private LocalDate referredToDate;
    private String referredToTime;
    private String referredToComments;
    private String referredToTitle;

    private LocalDate decisionDate;
    private String decisionTime;
    private String decisionAddComments;
    private String decisionCode;
    private String decisionTitle;

    private LocalDate offenderDispositionDate;
    private String offenderDispositionTime;
    private String offenderDispositionReason;
    private String offenderDispositionCellFlag;
    private String dispositionCode;

    private LocalDate preHearingRefDate;
    private String preHearingRefTime;
    private String preHearingRefComments;
    private String preHearingRefTitle;

    private LocalDate approvedByDate;
    private String approvedByStatus;
    private String approvedByFollowUp;
    private String approvedByAddComments;
    private String approvedByTitle;

    private LocalDate deliveredByDate;
    private String deliveredByTime;
    private String deliveredByAddComments;
    private String deliveredByTitle;

    private String userIdEnteredBy;
    private String userIdReferredTo;
    private String userIdPreHearing;
    private String userIdApproved;
    private String userIdDeliveredBy;
    
    
    // Getters and Setters omitted for brevity, but should be added for all fields
    public Long getIncidentSeqNum() {
		return incidentSeqNum;
	}
	public void setIncidentSeqNum(Long incidentSeqNum) {
		this.incidentSeqNum = incidentSeqNum;
	}

	public String getReportNumber() {
	    return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
	    this.reportNumber = reportNumber;
	}
	
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public LocalDate getDateOfOccurrence() {
		return dateOfOccurrence;
	}
	public void setDateOfOccurrence(LocalDate dateOfOccurrence) {
		this.dateOfOccurrence = dateOfOccurrence;
	}
	public String getTimeOfOccurrence() {
		return timeOfOccurrence;
	}
	public void setTimeOfOccurrence(String timeOfOccurrence) {
		this.timeOfOccurrence = timeOfOccurrence;
	}
	public String getLocationOfOccurrence() {
		return locationOfOccurrence;
	}
	public void setLocationOfOccurrence(String locationOfOccurrence) {
		this.locationOfOccurrence = locationOfOccurrence;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReasonForNullification() {
		return reasonForNullification;
	}
	public void setReasonForNullification(String reasonForNullification) {
		this.reasonForNullification = reasonForNullification;
	}
	public String getHousingUnit() {
		return housingUnit;
	}
	public void setHousingUnit(String housingUnit) {
		this.housingUnit = housingUnit;
	}
	public String getClass1Violation() {
		return class1Violation;
	}
	public void setClass1Violation(String class1Violation) {
		this.class1Violation = class1Violation;
	}
	public String getClass2Violation() {
		return class2Violation;
	}
	public void setClass2Violation(String class2Violation) {
		this.class2Violation = class2Violation;
	}
	public String getSummaryViolation() {
		return summaryViolation;
	}
	public void setSummaryViolation(String summaryViolation) {
		this.summaryViolation = summaryViolation;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getDspnViolSmryDesc() {
		return dspnViolSmryDesc;
	}
	public void setDspnViolSmryDesc(String dspnViolSmryDesc) {
		this.dspnViolSmryDesc = dspnViolSmryDesc;
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
	public String getMi() {
		return mi;
	}
	public void setMi(String mi) {
		this.mi = mi;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getStaffAssaultFlag() {
		return staffAssaultFlag;
	}
	public void setStaffAssaultFlag(String staffAssaultFlag) {
		this.staffAssaultFlag = staffAssaultFlag;
	}
	public String getStaffInjuriesFlag() {
		return staffInjuriesFlag;
	}
	public void setStaffInjuriesFlag(String staffInjuriesFlag) {
		this.staffInjuriesFlag = staffInjuriesFlag;
	}
	public String getIndustrialAccidentFlag() {
		return industrialAccidentFlag;
	}
	public void setIndustrialAccidentFlag(String industrialAccidentFlag) {
		this.industrialAccidentFlag = industrialAccidentFlag;
	}
	public String getInmateAssaultFlag() {
		return inmateAssaultFlag;
	}
	public void setInmateAssaultFlag(String inmateAssaultFlag) {
		this.inmateAssaultFlag = inmateAssaultFlag;
	}
	public String getResultOfIncident() {
		return resultOfIncident;
	}
	public void setResultOfIncident(String resultOfIncident) {
		this.resultOfIncident = resultOfIncident;
	}
	public String getResultOfSearch() {
		return resultOfSearch;
	}
	public void setResultOfSearch(String resultOfSearch) {
		this.resultOfSearch = resultOfSearch;
	}
	public String getSexRelatedFlag() {
		return sexRelatedFlag;
	}
	public void setSexRelatedFlag(String sexRelatedFlag) {
		this.sexRelatedFlag = sexRelatedFlag;
	}
	public String getStgRelatedFlag() {
		return stgRelatedFlag;
	}
	public void setStgRelatedFlag(String stgRelatedFlag) {
		this.stgRelatedFlag = stgRelatedFlag;
	}
	public String getAssaultTypeCode() {
		return assaultTypeCode;
	}
	public void setAssaultTypeCode(String assaultTypeCode) {
		this.assaultTypeCode = assaultTypeCode;
	}
	public String getEvidenceDisposition() {
		return evidenceDisposition;
	}
	public void setEvidenceDisposition(String evidenceDisposition) {
		this.evidenceDisposition = evidenceDisposition;
	}
	public LocalDate getDispositionDate() {
		return dispositionDate;
	}
	public void setDispositionDate(LocalDate dispositionDate) {
		this.dispositionDate = dispositionDate;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
	public String getWeaponNote() {
		return weaponNote;
	}
	public void setWeaponNote(String weaponNote) {
		this.weaponNote = weaponNote;
	}
	public LocalDate getEnteredByDate() {
		return enteredByDate;
	}
	public void setEnteredByDate(LocalDate enteredByDate) {
		this.enteredByDate = enteredByDate;
	}
	public String getEnteredByTime() {
		return enteredByTime;
	}
	public void setEnteredByTime(String enteredByTime) {
		this.enteredByTime = enteredByTime;
	}
	public String getEnteredByTitle() {
		return enteredByTitle;
	}
	public void setEnteredByTitle(String enteredByTitle) {
		this.enteredByTitle = enteredByTitle;
	}
	public LocalDate getReferredToDate() {
		return referredToDate;
	}
	public void setReferredToDate(LocalDate referredToDate) {
		this.referredToDate = referredToDate;
	}
	public String getReferredToTime() {
		return referredToTime;
	}
	public void setReferredToTime(String referredToTime) {
		this.referredToTime = referredToTime;
	}
	public String getReferredToComments() {
		return referredToComments;
	}
	public void setReferredToComments(String referredToComments) {
		this.referredToComments = referredToComments;
	}
	public String getReferredToTitle() {
		return referredToTitle;
	}
	public void setReferredToTitle(String referredToTitle) {
		this.referredToTitle = referredToTitle;
	}
	public LocalDate getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(LocalDate decisionDate) {
		this.decisionDate = decisionDate;
	}
	public String getDecisionTime() {
		return decisionTime;
	}
	public void setDecisionTime(String decisionTime) {
		this.decisionTime = decisionTime;
	}
	public String getDecisionAddComments() {
		return decisionAddComments;
	}
	public void setDecisionAddComments(String decisionAddComments) {
		this.decisionAddComments = decisionAddComments;
	}
	public String getDecisionCode() {
		return decisionCode;
	}
	public void setDecisionCode(String decisionCode) {
		this.decisionCode = decisionCode;
	}
	public String getDecisionTitle() {
		return decisionTitle;
	}
	public void setDecisionTitle(String decisionTitle) {
		this.decisionTitle = decisionTitle;
	}
	public LocalDate getOffenderDispositionDate() {
		return offenderDispositionDate;
	}
	public void setOffenderDispositionDate(LocalDate offenderDispositionDate) {
		this.offenderDispositionDate = offenderDispositionDate;
	}
	public String getOffenderDispositionTime() {
		return offenderDispositionTime;
	}
	public void setOffenderDispositionTime(String offenderDispositionTime) {
		this.offenderDispositionTime = offenderDispositionTime;
	}
	public String getOffenderDispositionReason() {
		return offenderDispositionReason;
	}
	public void setOffenderDispositionReason(String offenderDispositionReason) {
		this.offenderDispositionReason = offenderDispositionReason;
	}
	public String getOffenderDispositionCellFlag() {
		return offenderDispositionCellFlag;
	}
	public void setOffenderDispositionCellFlag(String offenderDispositionCellFlag) {
		this.offenderDispositionCellFlag = offenderDispositionCellFlag;
	}
	public String getDispositionCode() {
		return dispositionCode;
	}
	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	public LocalDate getPreHearingRefDate() {
		return preHearingRefDate;
	}
	public void setPreHearingRefDate(LocalDate preHearingRefDate) {
		this.preHearingRefDate = preHearingRefDate;
	}
	public String getPreHearingRefTime() {
		return preHearingRefTime;
	}
	public void setPreHearingRefTime(String preHearingRefTime) {
		this.preHearingRefTime = preHearingRefTime;
	}
	public String getPreHearingRefComments() {
		return preHearingRefComments;
	}
	public void setPreHearingRefComments(String preHearingRefComments) {
		this.preHearingRefComments = preHearingRefComments;
	}
	public String getPreHearingRefTitle() {
		return preHearingRefTitle;
	}
	public void setPreHearingRefTitle(String preHearingRefTitle) {
		this.preHearingRefTitle = preHearingRefTitle;
	}
	public LocalDate getApprovedByDate() {
		return approvedByDate;
	}
	public void setApprovedByDate(LocalDate approvedByDate) {
		this.approvedByDate = approvedByDate;
	}
	public String getApprovedByStatus() {
		return approvedByStatus;
	}
	public void setApprovedByStatus(String approvedByStatus) {
		this.approvedByStatus = approvedByStatus;
	}
	public String getApprovedByFollowUp() {
		return approvedByFollowUp;
	}
	public void setApprovedByFollowUp(String approvedByFollowUp) {
		this.approvedByFollowUp = approvedByFollowUp;
	}
	public String getApprovedByAddComments() {
		return approvedByAddComments;
	}
	public void setApprovedByAddComments(String approvedByAddComments) {
		this.approvedByAddComments = approvedByAddComments;
	}
	public String getApprovedByTitle() {
		return approvedByTitle;
	}
	public void setApprovedByTitle(String approvedByTitle) {
		this.approvedByTitle = approvedByTitle;
	}
	public LocalDate getDeliveredByDate() {
		return deliveredByDate;
	}
	public void setDeliveredByDate(LocalDate deliveredByDate) {
		this.deliveredByDate = deliveredByDate;
	}
	public String getDeliveredByTime() {
		return deliveredByTime;
	}
	public void setDeliveredByTime(String deliveredByTime) {
		this.deliveredByTime = deliveredByTime;
	}
	public String getDeliveredByAddComments() {
		return deliveredByAddComments;
	}
	public void setDeliveredByAddComments(String deliveredByAddComments) {
		this.deliveredByAddComments = deliveredByAddComments;
	}
	public String getDeliveredByTitle() {
		return deliveredByTitle;
	}
	public void setDeliveredByTitle(String deliveredByTitle) {
		this.deliveredByTitle = deliveredByTitle;
	}
	public String getUserIdEnteredBy() {
		return userIdEnteredBy;
	}
	public void setUserIdEnteredBy(String userIdEnteredBy) {
		this.userIdEnteredBy = userIdEnteredBy;
	}
	public String getUserIdReferredTo() {
		return userIdReferredTo;
	}
	public void setUserIdReferredTo(String userIdReferredTo) {
		this.userIdReferredTo = userIdReferredTo;
	}
	public String getUserIdPreHearing() {
		return userIdPreHearing;
	}
	public void setUserIdPreHearing(String userIdPreHearing) {
		this.userIdPreHearing = userIdPreHearing;
	}
	public String getUserIdApproved() {
		return userIdApproved;
	}
	public void setUserIdApproved(String userIdApproved) {
		this.userIdApproved = userIdApproved;
	}
	public String getUserIdDeliveredBy() {
		return userIdDeliveredBy;
	}
	public void setUserIdDeliveredBy(String userIdDeliveredBy) {
		this.userIdDeliveredBy = userIdDeliveredBy;
	}
	
	 // Witness-specific fields
    private Long witnessSeqNum;         
    public Long getWitnessSeqNum() {
		return witnessSeqNum;
	}

	public void setWitnessSeqNum(Long witnessSeqNum) {
		this.witnessSeqNum = witnessSeqNum;
	}

	public String getWitnessFirstName() {
		return witnessFirstName;
	}

	public void setWitnessFirstName(String witnessFirstName) {
		this.witnessFirstName = witnessFirstName;
	}

	public String getWitnessLastName() {
		return witnessLastName;
	}

	public void setWitnessLastName(String witnessLastName) {
		this.witnessLastName = witnessLastName;
	}

	public String getWitnessMiddleName() {
		return witnessMiddleName;
	}

	public void setWitnessMiddleName(String witnessMiddleName) {
		this.witnessMiddleName = witnessMiddleName;
	}

	public String getWitnessSuffix() {
		return witnessSuffix;
	}

	public void setWitnessSuffix(String witnessSuffix) {
		this.witnessSuffix = witnessSuffix;
	}

	public String getDspnWitnessDelFlag() {
		return dspnWitnessDelFlag;
	}

	public void setDspnWitnessDelFlag(String dspnWitnessDelFlag) {
		this.dspnWitnessDelFlag = dspnWitnessDelFlag;
	}
	

}
