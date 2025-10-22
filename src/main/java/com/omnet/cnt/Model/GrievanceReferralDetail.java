package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GRIEVANCE_REFERRAL_DTL")
public class GrievanceReferralDetail {

    @Id
    @Column(name = "GRV_REFL_DTL_SEQ_NUM", nullable = false)
    private Long referralDetailSeqNum;

    @Column(name = "COMMIT_NO", length = 7)
    private String commitNumber;

    @ManyToOne
    @JoinColumn(name = "GRIEVANCE_SEQ_NUM", referencedColumnName = "GRIEVANCE_SEQ_NUM")
    private InmateGrievance grievance;

    @Column(name = "REFR_TO", length = 1)
    private String referTo;

    @Column(name = "REFR_TO_USER_ID", length = 8)
    private String referToUserId;

    @Column(name = "REFR_TO_COMT_TYPE", length = 10)
    private String referToCommitteeType;

    @Column(name = "REFR_TO_EXTERNAL_AGENCY", length = 10)
    private String referToExternalAgency;

    @Column(name = "REFR_DATE")
    private Date referralDate;

    @Column(name = "REFR_BY", length = 8)
    private String referredBy;

    @Lob
    @Column(name = "INFORMATION_REQST")
    private String informationRequested;

    @Lob
    @Column(name = "RESPONSE_RECEIVED")
    private String responseReceived;

    @Column(name = "RESPONSE_DATE")
    private Date responseDate;

    @Column(name = "CONFIDENTIAL_FLAG", length = 1)
    private String confidentialFlag;

    @Column(name = "COMPLETED_FLAG", length = 1)
    private String completedFlag;

    @Column(name = "INSERTED_USERID", nullable = false, length = 8)
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME", nullable = false)
    private Date insertedDateTime;

    @Column(name = "UPDATED_USERID", nullable = false, length = 8)
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME", nullable = false)
    private Date updatedDateTime;

    @Column(name = "SESSION_ID", nullable = false)
    private Long sessionId;

    @Column(name = "TERMINAL", nullable = false, length = 30)
    private String terminal;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "GRV_ADDRESSED_FLAG", length = 1)
    private String grievanceAddressedFlag;

    // Getters and Setters
    public Long getReferralDetailSeqNum() {
        return referralDetailSeqNum;
    }

    public void setReferralDetailSeqNum(Long referralDetailSeqNum) {
        this.referralDetailSeqNum = referralDetailSeqNum;
    }

    public String getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(String commitNumber) {
        this.commitNumber = commitNumber;
    }

    public InmateGrievance getGrievance() {
        return grievance;
    }

    public void setGrievance(InmateGrievance grievance) {
        this.grievance = grievance;
    }

    public String getReferTo() {
        return referTo;
    }

    public void setReferTo(String referTo) {
        this.referTo = referTo;
    }

    public String getReferToUserId() {
        return referToUserId;
    }

    public void setReferToUserId(String referToUserId) {
        this.referToUserId = referToUserId;
    }

    public String getReferToCommitteeType() {
        return referToCommitteeType;
    }

    public void setReferToCommitteeType(String referToCommitteeType) {
        this.referToCommitteeType = referToCommitteeType;
    }

    public String getReferToExternalAgency() {
        return referToExternalAgency;
    }

    public void setReferToExternalAgency(String referToExternalAgency) {
        this.referToExternalAgency = referToExternalAgency;
    }

    public Date getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(Date referralDate) {
        this.referralDate = referralDate;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public String getInformationRequested() {
        return informationRequested;
    }

    public void setInformationRequested(String informationRequested) {
        this.informationRequested = informationRequested;
    }

    public String getResponseReceived() {
        return responseReceived;
    }

    public void setResponseReceived(String responseReceived) {
        this.responseReceived = responseReceived;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getConfidentialFlag() {
        return confidentialFlag;
    }

    public void setConfidentialFlag(String confidentialFlag) {
        this.confidentialFlag = confidentialFlag;
    }

    public String getCompletedFlag() {
        return completedFlag;
    }

    public void setCompletedFlag(String completedFlag) {
        this.completedFlag = completedFlag;
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

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getGrievanceAddressedFlag() {
        return grievanceAddressedFlag;
    }

    public void setGrievanceAddressedFlag(String grievanceAddressedFlag) {
        this.grievanceAddressedFlag = grievanceAddressedFlag;
    }
}