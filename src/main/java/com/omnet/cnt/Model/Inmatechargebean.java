package com.omnet.cnt.Model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Inmatechargebean {
	
	    @JsonProperty("COMMIT_NO")
	    private String commitNo;

	    @JsonProperty("CASE_NUM")
	    private String caseNum;

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
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
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
		public Double getBailAmount() {
			return bailAmount;
		}
		public void setBailAmount(Double bailAmount) {
			this.bailAmount = bailAmount;
		}
		public Integer getChargeNum() {
			return chargeNum;
		}
		public void setChargeNum(Integer chargeNum) {
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
		public String getChargeJurisdiction() {
			return chargeJurisdiction;
		}
		public void setChargeJurisdiction(String chargeJurisdiction) {
			this.chargeJurisdiction = chargeJurisdiction;
		}
		public String getStatueTitle() {
			return statueTitle;
		}
		public void setStatueTitle(String statueTitle) {
			this.statueTitle = statueTitle;
		}
		public String getStatueSection() {
			return statueSection;
		}
		public void setStatueSection(String statueSection) {
			this.statueSection = statueSection;
		}
		public String getStatueSubsection() {
			return statueSubsection;
		}
		public void setStatueSubsection(String statueSubsection) {
			this.statueSubsection = statueSubsection;
		}
		public String getStatueType() {
			return statueType;
		}
		public void setStatueType(String statueType) {
			this.statueType = statueType;
		}
		public String getStatueClass() {
			return statueClass;
		}
		public void setStatueClass(String statueClass) {
			this.statueClass = statueClass;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
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
		public String getDispositionCode() {
			return dispositionCode;
		}
		public void setDispositionCode(String dispositionCode) {
			this.dispositionCode = dispositionCode;
		}
		 @JsonProperty("CHARGE_DESCRIPTION")
		    private String chargeDescription;

		    @JsonProperty("START_DATE")
		    private String startDate;

		    @JsonProperty("END_DATE")
		    private String endDate;

		    @JsonProperty("DISPOSITION_CODE")
		    private String dispositionCode;

		    @JsonProperty("BAIL_AMOUNT")
		    private Double bailAmount;

		    @JsonProperty("BAIL_AMOUNT_TYPE")
		    private String bailAmountType;

		    @JsonProperty("ACTIVE_FLAG")
		    private String activeFlag;

		    @JsonProperty("CHARGE_NUM")
		    private Integer chargeNum;

		    @JsonProperty("CHARGE_SEQ_NUM")
		    private Integer chargeSeqNum;

		    @JsonProperty("JUDGE_CODE")
		    private String judgeCode;

		    @JsonProperty("COURT_CODE")
		    private String courtCode;

		    @JsonProperty("CHARGE_JURISDICTION")
		    private String chargeJurisdiction;

		    @JsonProperty("STATUE_TITLE")
		    private String statueTitle;

		    @JsonProperty("STATUE_SECTION")
		    private String statueSection;

		    @JsonProperty("STATUE_SUBSECTION")
		    private String statueSubsection;

		    @JsonProperty("STATUE_TYPE")
		    private String statueType;

		    @JsonProperty("STATUE_CLASS")
		    private String statueClass;

		    @JsonProperty("COMMENTS")
		    private String comments;

		    @JsonProperty("NCIC_CODE")
		    private String ncicCode;

		    public String getBailType() {
				return bailType;
			}

			public void setBailType(String bailType) {
				this.bailType = bailType;
			}
			@JsonProperty("SENTAC_CATEGORY")
		    private String sentacCategory;
		    
		    @JsonProperty("BAIL_TYPE")
		    private String bailType;
		    
		    @JsonProperty("CONSOLIDATED_CRA_NUM")
		    private String consolidatedCraNum;

		    public String getConsolidatedCraNum() {
		        return consolidatedCraNum;
		    }

		    public void setConsolidatedCraNum(String consolidatedCraNum) {
		        this.consolidatedCraNum = consolidatedCraNum;
		    }
		    
		    
}
