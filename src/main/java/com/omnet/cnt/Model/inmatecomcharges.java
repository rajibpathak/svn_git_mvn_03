package com.omnet.cnt.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "INMATE_COMMITTED_CHARGES")
public class inmatecomcharges {

    @Id
    @Column(name = "COMMIT_NO", nullable = false, length = 50)
    private String commitNo;

    @Column(name = "CASE_NUM", length = 50)
    private String caseNum;

    @Column(name = "CHARGE_SEQ_NUM")
    private Integer chargeSeqNum;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "CHARGE_DESCRIPTION", length = 255)
    private String chargeDescription;

    @Column(name = "ACTIVE_FLAG", length = 1)
    private String activeFlag;

    @Column(name = "INSERTED_USERID", length = 50)
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME")
    private LocalDateTime insertedDateTime;

    @Column(name = "UPDATED_USERID", length = 50)
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME")
    private LocalDateTime updatedDateTime;

    @Column(name = "SESSION_ID", length = 50)
    private String sessionId;

    @Column(name = "TERMINAL", length = 50)
    private String terminal;

    @Column(name = "BAIL_AMOUNT")
    private BigDecimal bailAmount;

    @Column(name = "CHARGE_JURISDICTION", length = 100)
    private String chargeJurisdiction;

    @Column(name = "STATUE_TITLE", length = 100)
    private String statuteTitle;

    @Column(name = "STATUE_SECTION", length = 100)
    private String statuteSection;

    @Column(name = "STATUE_SUBSECTION", length = 50)
    private String statuteSubsection;

    @Column(name = "STATUE_CLASS", length = 50)
    private String statuteClass;

    @Column(name = "STATUE_TYPE", length = 50)
    private String statuteType;

    @Column(name = "STATUE_SHORT_DESC", length = 255)
    private String statuteShortDesc;

    @Column(name = "NCIC_CODE", length = 50)
    private String ncicCode;

    @Column(name = "SENTAC_CATEGORY", length = 50)
    private String sentacCategory;

    @Column(name = "CRIME_OCCURRED_DATE")
    private LocalDate crimeOccurredDate;

    @Column(name = "CRIMINAL_ACTION_NUMBER", length = 50)
    private String criminalActionNumber;

    @Column(name = "LEAD_CHARGE_INDICATOR", length = 1)
    private String leadChargeIndicator;

    @Column(name = "BAIL_TYPE", length = 50)
    private String bailType;

    @Column(name = "CHARGE_NUM", length = 50)
    private String chargeNum;

    @Column(name = "JUDGE_CODE", length = 50)
    private String judgeCode;

    @Column(name = "COURT_CODE", length = 50)
    private String courtCode;

    @Column(name = "COMMENTS", length = 500)
    private String comments;

    @Column(name = "BUCKET_NUM", length = 50)
    private String bucketNum;

    @Column(name = "DISPOSITION_CODE", length = 50)
    private String dispositionCode;

    @Column(name = "SEX_CHARGE", length = 1)
    private String sexCharge;

    @Column(name = "CONSOLIDATED_CASE_NUM", length = 50)
    private String consolidatedCaseNum;

    @Column(name = "CONSOLIDATED_CRA_NUM", length = 50)
    private String consolidatedCraNum;

    @Column(name = "BAIL_AMOUNT_TYPE", length = 50)
    private String bailAmountType;

	public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public Integer getChargeSeqNum() {
		return chargeSeqNum;
	}

	public void setChargeSeqNum(Integer chargeSeqNum) {
		this.chargeSeqNum = chargeSeqNum;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getInsertedUserId() {
		return insertedUserId;
	}

	public void setInsertedUserId(String insertedUserId) {
		this.insertedUserId = insertedUserId;
	}

	public LocalDateTime getInsertedDateTime() {
		return insertedDateTime;
	}

	public void setInsertedDateTime(LocalDateTime insertedDateTime) {
		this.insertedDateTime = insertedDateTime;
	}

	public String getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public LocalDateTime getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(LocalDateTime updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public BigDecimal getBailAmount() {
		return bailAmount;
	}

	public void setBailAmount(BigDecimal bailAmount) {
		this.bailAmount = bailAmount;
	}

	public String getChargeJurisdiction() {
		return chargeJurisdiction;
	}

	public void setChargeJurisdiction(String chargeJurisdiction) {
		this.chargeJurisdiction = chargeJurisdiction;
	}

	public String getStatuteTitle() {
		return statuteTitle;
	}

	public void setStatuteTitle(String statuteTitle) {
		this.statuteTitle = statuteTitle;
	}

	public String getStatuteSection() {
		return statuteSection;
	}

	public void setStatuteSection(String statuteSection) {
		this.statuteSection = statuteSection;
	}

	public String getStatuteSubsection() {
		return statuteSubsection;
	}

	public void setStatuteSubsection(String statuteSubsection) {
		this.statuteSubsection = statuteSubsection;
	}

	public String getStatuteClass() {
		return statuteClass;
	}

	public void setStatuteClass(String statuteClass) {
		this.statuteClass = statuteClass;
	}

	public String getStatuteType() {
		return statuteType;
	}

	public void setStatuteType(String statuteType) {
		this.statuteType = statuteType;
	}

	public String getStatuteShortDesc() {
		return statuteShortDesc;
	}

	public void setStatuteShortDesc(String statuteShortDesc) {
		this.statuteShortDesc = statuteShortDesc;
	}

	public String getNcicCode() {
		return ncicCode;
	}

	public void setNcicCode(String ncicCode) {
		this.ncicCode = ncicCode;
	}

	public String getSentacCategory() {
		return sentacCategory;
	}

	public void setSentacCategory(String sentacCategory) {
		this.sentacCategory = sentacCategory;
	}

	public LocalDate getCrimeOccurredDate() {
		return crimeOccurredDate;
	}

	public void setCrimeOccurredDate(LocalDate crimeOccurredDate) {
		this.crimeOccurredDate = crimeOccurredDate;
	}

	public String getCriminalActionNumber() {
		return criminalActionNumber;
	}

	public void setCriminalActionNumber(String criminalActionNumber) {
		this.criminalActionNumber = criminalActionNumber;
	}

	public String getLeadChargeIndicator() {
		return leadChargeIndicator;
	}

	public void setLeadChargeIndicator(String leadChargeIndicator) {
		this.leadChargeIndicator = leadChargeIndicator;
	}

	public String getBailType() {
		return bailType;
	}

	public void setBailType(String bailType) {
		this.bailType = bailType;
	}

	public String getChargeNum() {
		return chargeNum;
	}

	public void setChargeNum(String chargeNum) {
		this.chargeNum = chargeNum;
	}

	public String getJudgeCode() {
		return judgeCode;
	}

	public void setJudgeCode(String judgeCode) {
		this.judgeCode = judgeCode;
	}

	public String getCourtCode() {
		return courtCode;
	}

	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getBucketNum() {
		return bucketNum;
	}

	public void setBucketNum(String bucketNum) {
		this.bucketNum = bucketNum;
	}

	public String getDispositionCode() {
		return dispositionCode;
	}

	public void setDispositionCode(String dispositionCode) {
		this.dispositionCode = dispositionCode;
	}

	public String getSexCharge() {
		return sexCharge;
	}

	public void setSexCharge(String sexCharge) {
		this.sexCharge = sexCharge;
	}

	public String getConsolidatedCaseNum() {
		return consolidatedCaseNum;
	}

	public void setConsolidatedCaseNum(String consolidatedCaseNum) {
		this.consolidatedCaseNum = consolidatedCaseNum;
	}

	public String getConsolidatedCraNum() {
		return consolidatedCraNum;
	}

	public void setConsolidatedCraNum(String consolidatedCraNum) {
		this.consolidatedCraNum = consolidatedCraNum;
	}

	public String getBailAmountType() {
		return bailAmountType;
	}

	public void setBailAmountType(String bailAmountType) {
		this.bailAmountType = bailAmountType;
	}

	@Override
	public String toString() {
		return "inmatecomcharges [commitNo=" + commitNo + ", caseNum=" + caseNum + ", chargeSeqNum=" + chargeSeqNum
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", chargeDescription=" + chargeDescription
				+ ", activeFlag=" + activeFlag + ", insertedUserId=" + insertedUserId + ", insertedDateTime="
				+ insertedDateTime + ", updatedUserId=" + updatedUserId + ", updatedDateTime=" + updatedDateTime
				+ ", sessionId=" + sessionId + ", terminal=" + terminal + ", bailAmount=" + bailAmount
				+ ", chargeJurisdiction=" + chargeJurisdiction + ", statuteTitle=" + statuteTitle + ", statuteSection="
				+ statuteSection + ", statuteSubsection=" + statuteSubsection + ", statuteClass=" + statuteClass
				+ ", statuteType=" + statuteType + ", statuteShortDesc=" + statuteShortDesc + ", ncicCode=" + ncicCode
				+ ", sentacCategory=" + sentacCategory + ", crimeOccurredDate=" + crimeOccurredDate
				+ ", criminalActionNumber=" + criminalActionNumber + ", leadChargeIndicator=" + leadChargeIndicator
				+ ", bailType=" + bailType + ", chargeNum=" + chargeNum + ", judgeCode=" + judgeCode + ", courtCode="
				+ courtCode + ", comments=" + comments + ", bucketNum=" + bucketNum + ", dispositionCode="
				+ dispositionCode + ", sexCharge=" + sexCharge + ", consolidatedCaseNum=" + consolidatedCaseNum
				+ ", consolidatedCraNum=" + consolidatedCraNum + ", bailAmountType=" + bailAmountType + "]";
	}

	public inmatecomcharges() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

}
