package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GRIEVANCE_INVST_DTL")
public class GrievanceInvestigationDetail {

    @Id
    @Column(name = "GRVN_INVST_SEQ_NO", nullable = false)
    private Long investigationSequenceNo;
    
    @Column(name = "GRVN_INVST_DT")
    private Date investigationDate;
    
    @Column(name = "GRVN_INVST_DESC_OLD", length = 2000)
    private String investigationDescriptionOld;
    
    @Column(name = "GRVN_INVST_FRWD_DT")
    private Date investigationForwardedDate;
    
    @Column(name = "COMMIT_NO", nullable = false, length = 7)
    private String commitNumber;
    
    @ManyToOne
    @JoinColumn(name = "GRIEVANCE_SEQ_NUM", referencedColumnName = "GRIEVANCE_SEQ_NUM", nullable = false)
    private InmateGrievance grievance;
    
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private OmnetUser user;
    
    @Column(name = "RESN_CD", length = 6)
    private String reasonCode;
    
    @Column(name = "GRVN_RESN_COMNT_OLD", length = 1000)
    private String reasonCommentOld;
    
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
    
    @Column(name = "FORWARDED_FLAG", length = 1)
    private String forwardedFlag;
    
    @Column(name = "DATE_SENT")
    private Date dateSent;
    
    @Lob
    @Column(name = "GRVN_RESN_COMNT")
    private String reasonComment;
    
    @Lob
    @Column(name = "GRVN_INVST_DESC")
    private String investigationDescription;

    // Getters and Setters
    public Long getInvestigationSequenceNo() {
        return investigationSequenceNo;
    }

    public void setInvestigationSequenceNo(Long investigationSequenceNo) {
        this.investigationSequenceNo = investigationSequenceNo;
    }

    public Date getInvestigationDate() {
        return investigationDate;
    }

    public void setInvestigationDate(Date investigationDate) {
        this.investigationDate = investigationDate;
    }

    public String getInvestigationDescriptionOld() {
        return investigationDescriptionOld;
    }

    public void setInvestigationDescriptionOld(String investigationDescriptionOld) {
        this.investigationDescriptionOld = investigationDescriptionOld;
    }

    public Date getInvestigationForwardedDate() {
        return investigationForwardedDate;
    }

    public void setInvestigationForwardedDate(Date investigationForwardedDate) {
        this.investigationForwardedDate = investigationForwardedDate;
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

    public OmnetUser getUser() {
        return user;
    }

    public void setUser(OmnetUser user) {
        this.user = user;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonCommentOld() {
        return reasonCommentOld;
    }

    public void setReasonCommentOld(String reasonCommentOld) {
        this.reasonCommentOld = reasonCommentOld;
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

    public String getForwardedFlag() {
        return forwardedFlag;
    }

    public void setForwardedFlag(String forwardedFlag) {
        this.forwardedFlag = forwardedFlag;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getReasonComment() {
        return reasonComment;
    }

    public void setReasonComment(String reasonComment) {
        this.reasonComment = reasonComment;
    }

    public String getInvestigationDescription() {
        return investigationDescription;
    }

    public void setInvestigationDescription(String investigationDescription) {
        this.investigationDescription = investigationDescription;
    }
}