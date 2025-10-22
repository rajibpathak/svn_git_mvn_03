package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GRIEVANCE_APPEAL_DTL")
public class GrievanceAppealDetail {

    @Id
    @Column(name = "GRVN_APPEAL_SEQ_NO", nullable = false)
    private Long appealSequenceNo;

    @Column(name = "GRVN_APPEAL_DT")
    private Date appealDate;

    @Column(name = "GRVN_APPEAL_DESC", length = 2000)
    private String appealDescription;

    @Column(name = "GRVN_APPEAL_REMDY_REQ", length = 4000)
    private String remedyRequested;

    @Column(name = "GRVN_APPEAL_STATUS", length = 10)
    private String appealStatus;

    @Column(name = "GRVN_APPEAL_LVL", length = 3)
    private String appealLevel;

    @Column(name = "COMMIT_NO", nullable = false, length = 7)
    private String commitNumber;

    @ManyToOne
    @JoinColumn(name = "GRIEVANCE_SEQ_NUM", referencedColumnName = "GRIEVANCE_SEQ_NUM", nullable = false)
    private InmateGrievance grievance;

    @Column(name = "RESN_CD", length = 6)
    private String reasonCode;

    @Column(name = "INSERTED_USERID", length = 8)
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME")
    private Date insertedDateTime;

    @Column(name = "UPDATED_USERID", length = 8)
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME")
    private Date updatedDateTime;

    @Column(name = "SESSION_ID")
    private Long sessionId;

    @Column(name = "TERMINAL", length = 30)
    private String terminal;

    @Column(name = "FRWD_TO_BGO_FLAG", length = 1)
    private String forwardToBGOFlag;

    @Column(name = "FRWD_TO_BC_FLAG", length = 1)
    private String forwardToBCFlag;

    // Getters and Setters
    public Long getAppealSequenceNo() {
        return appealSequenceNo;
    }

    public void setAppealSequenceNo(Long appealSequenceNo) {
        this.appealSequenceNo = appealSequenceNo;
    }

    public Date getAppealDate() {
        return appealDate;
    }

    public void setAppealDate(Date appealDate) {
        this.appealDate = appealDate;
    }

    public String getAppealDescription() {
        return appealDescription;
    }

    public void setAppealDescription(String appealDescription) {
        this.appealDescription = appealDescription;
    }

    public String getRemedyRequested() {
        return remedyRequested;
    }

    public void setRemedyRequested(String remedyRequested) {
        this.remedyRequested = remedyRequested;
    }

    public String getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(String appealStatus) {
        this.appealStatus = appealStatus;
    }

    public String getAppealLevel() {
        return appealLevel;
    }

    public void setAppealLevel(String appealLevel) {
        this.appealLevel = appealLevel;
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

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
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

    public String getForwardToBGOFlag() {
        return forwardToBGOFlag;
    }

    public void setForwardToBGOFlag(String forwardToBGOFlag) {
        this.forwardToBGOFlag = forwardToBGOFlag;
    }

    public String getForwardToBCFlag() {
        return forwardToBCFlag;
    }

    public void setForwardToBCFlag(String forwardToBCFlag) {
        this.forwardToBCFlag = forwardToBCFlag;
    }
}