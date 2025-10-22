package com.omnet.cnt.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "GRIEVANCE_REW_DTL")
public class GrievanceReviewDetail {

    @Id
    @Column(name = "GRVN_REW_SEQ_NO", nullable = false)
    private Long reviewSequenceNo;

    @Column(name = "COMMIT_NO", nullable = false, length = 7)
    private String commitNumber;

    @ManyToOne
    @JoinColumn(name = "GRIEVANCE_SEQ_NUM", referencedColumnName = "GRIEVANCE_SEQ_NUM", nullable = false)
    private InmateGrievance grievance;

    @Column(name = "MEDI_PRVD_CD", length = 6)
    private String medicalProviderCode;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private OmnetUser user;

    @Column(name = "RESN_CD", length = 6)
    private String reasonCode;

    @Column(name = "GRVN_REW_CMTY_TP", nullable = false, length = 3)
    private String committeeType;

    @Column(name = "GRVN_REW_ASGN_DT")
    private Date assignedDate;

    @Column(name = "GRVN_REW_RCPT_DT")
    private Date receivedDate;

    @Column(name = "GRVN_REW_RECM_DT")
    private Date recommendationDate;

    @Column(name = "GRVN_REW_VOTE_ABSN_FLG", length = 1)
    private String abstainVoteFlag;

    @Column(name = "GRVN_REW_VOTE_DENY_FLG", length = 1)
    private String denyVoteFlag;

    @Column(name = "GRVN_REW_VOTE_UPLD_FLG", length = 1)
    private String upholdVoteFlag;

    @Column(name = "GRVN_REW_VOTE_TOT_ABSN")
    private Integer totalAbstainVotes;

    @Column(name = "GRVN_REW_VOTE_TOT_DENY")
    private Integer totalDenyVotes;

    @Column(name = "GRVN_REW_VOTE_TOT_UPLD")
    private Integer totalUpholdVotes;

    @Column(name = "GRVN_REW_FRWD_DT")
    private Date forwardedDate;

    @Column(name = "GRVN_REW_FRWD_TO_FLG", length = 1)
    private String forwardToRGC_SMEFlag;

    @Column(name = "GRVN_REFR_TO_PC_IND", length = 1)
    private String referToPCIndicator;

    @Column(name = "GRVN_REFR_CMTY", length = 3)
    private String referCommittee;

    @Column(name = "GRVN_REFR_DUE_DT")
    private Date referDueDate;

    @Column(name = "GRVN_DECN_DT")
    private Date decisionDate;

    @Column(name = "GRVN_DECN_DESC", length = 1000)
    private String decisionDescription;

    @Column(name = "GRVN_DECN_COMNT", length = 1000)
    private String decisionComments;

    @Column(name = "GRVN_DECN_NTF_FLG", length = 1)
    private String offenderSignatureFlag;

    @Column(name = "GRVN_DECN_NTF_DT")
    private Date offenderSignatureDate;

    @Column(name = "GRVN_REQ_DESC", length = 1000)
    private String requestDescription;

    @Column(name = "GRVN_REW_UD_IND", length = 1)
    private String updateIndicator;

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

    @Column(name = "FRWD_MED_PROVIDER_FLG", length = 1)
    private String forwardToMGCFlag;

    @Column(name = "NOTIFY_WARDEN_FLG", length = 1)
    private String notifyWardenFlag;

    @Column(name = "REQUESTED_INFO", length = 2000)
    private String requestedInformation;

    @Column(name = "FRWD_TO_MEDICALPROVIDER_FLG2", length = 1)
    private String forwardToMedicalProviderFlag2;

    @Column(name = "FRWD_MED_PROVIDER_DATE")
    private Date forwardToMGCDate;

    @Lob
    @Column(name = "GRVN_REW_RECM_COMNT")
    private String recommendationComments;

    @Lob
    @Column(name = "GRVN_REW_RECM_DESC")
    private String recommendationDescription;

    public Long getReviewSequenceNo() {
        return reviewSequenceNo;
    }

    public void setReviewSequenceNo(Long reviewSequenceNo) {
        this.reviewSequenceNo = reviewSequenceNo;
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

    public String getMedicalProviderCode() {
        return medicalProviderCode;
    }

    public void setMedicalProviderCode(String medicalProviderCode) {
        this.medicalProviderCode = medicalProviderCode;
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

    public String getCommitteeType() {
        return committeeType;
    }

    public void setCommitteeType(String committeeType) {
        this.committeeType = committeeType;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getRecommendationDate() {
        return recommendationDate;
    }

    public void setRecommendationDate(Date recommendationDate) {
        this.recommendationDate = recommendationDate;
    }

    public String getAbstainVoteFlag() {
        return abstainVoteFlag;
    }

    public void setAbstainVoteFlag(String abstainVoteFlag) {
        this.abstainVoteFlag = abstainVoteFlag;
    }

    public String getDenyVoteFlag() {
        return denyVoteFlag;
    }

    public void setDenyVoteFlag(String denyVoteFlag) {
        this.denyVoteFlag = denyVoteFlag;
    }

    public String getUpholdVoteFlag() {
        return upholdVoteFlag;
    }

    public void setUpholdVoteFlag(String upholdVoteFlag) {
        this.upholdVoteFlag = upholdVoteFlag;
    }

    public Integer getTotalAbstainVotes() {
        return totalAbstainVotes;
    }

    public void setTotalAbstainVotes(Integer totalAbstainVotes) {
        this.totalAbstainVotes = totalAbstainVotes;
    }

    public Integer getTotalDenyVotes() {
        return totalDenyVotes;
    }

    public void setTotalDenyVotes(Integer totalDenyVotes) {
        this.totalDenyVotes = totalDenyVotes;
    }

    public Integer getTotalUpholdVotes() {
        return totalUpholdVotes;
    }

    public void setTotalUpholdVotes(Integer totalUpholdVotes) {
        this.totalUpholdVotes = totalUpholdVotes;
    }

    public Date getForwardedDate() {
        return forwardedDate;
    }

    public void setForwardedDate(Date forwardedDate) {
        this.forwardedDate = forwardedDate;
    }

    public String getForwardToRGC_SMEFlag() {
        return forwardToRGC_SMEFlag;
    }

    public void setForwardToRGC_SMEFlag(String forwardToRGC_SMEFlag) {
        this.forwardToRGC_SMEFlag = forwardToRGC_SMEFlag;
    }

    public String getReferToPCIndicator() {
        return referToPCIndicator;
    }

    public void setReferToPCIndicator(String referToPCIndicator) {
        this.referToPCIndicator = referToPCIndicator;
    }

    public String getReferCommittee() {
        return referCommittee;
    }

    public void setReferCommittee(String referCommittee) {
        this.referCommittee = referCommittee;
    }

    public Date getReferDueDate() {
        return referDueDate;
    }

    public void setReferDueDate(Date referDueDate) {
        this.referDueDate = referDueDate;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(Date decisionDate) {
        this.decisionDate = decisionDate;
    }

    public String getDecisionDescription() {
        return decisionDescription;
    }

    public void setDecisionDescription(String decisionDescription) {
        this.decisionDescription = decisionDescription;
    }

    public String getDecisionComments() {
        return decisionComments;
    }

    public void setDecisionComments(String decisionComments) {
        this.decisionComments = decisionComments;
    }

    public String getOffenderSignatureFlag() {
        return offenderSignatureFlag;
    }

    public void setOffenderSignatureFlag(String offenderSignatureFlag) {
        this.offenderSignatureFlag = offenderSignatureFlag;
    }

    public Date getOffenderSignatureDate() {
        return offenderSignatureDate;
    }

    public void setOffenderSignatureDate(Date offenderSignatureDate) {
        this.offenderSignatureDate = offenderSignatureDate;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getUpdateIndicator() {
        return updateIndicator;
    }

    public void setUpdateIndicator(String updateIndicator) {
        this.updateIndicator = updateIndicator;
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

    public String getForwardToMGCFlag() {
        return forwardToMGCFlag;
    }

    public void setForwardToMGCFlag(String forwardToMGCFlag) {
        this.forwardToMGCFlag = forwardToMGCFlag;
    }

    public String getNotifyWardenFlag() {
        return notifyWardenFlag;
    }

    public void setNotifyWardenFlag(String notifyWardenFlag) {
        this.notifyWardenFlag = notifyWardenFlag;
    }

    public String getRequestedInformation() {
        return requestedInformation;
    }

    public void setRequestedInformation(String requestedInformation) {
        this.requestedInformation = requestedInformation;
    }

    public String getForwardToMedicalProviderFlag2() {
        return forwardToMedicalProviderFlag2;
    }

    public void setForwardToMedicalProviderFlag2(String forwardToMedicalProviderFlag2) {
        this.forwardToMedicalProviderFlag2 = forwardToMedicalProviderFlag2;
    }

    public Date getForwardToMGCDate() {
        return forwardToMGCDate;
    }

    public void setForwardToMGCDate(Date forwardToMGCDate) {
        this.forwardToMGCDate = forwardToMGCDate;
    }

    public String getRecommendationComments() {
        return recommendationComments;
    }

    public void setRecommendationComments(String recommendationComments) {
        this.recommendationComments = recommendationComments;
    }

    public String getRecommendationDescription() {
        return recommendationDescription;
    }

    public void setRecommendationDescription(String recommendationDescription) {
        this.recommendationDescription = recommendationDescription;
    }

}