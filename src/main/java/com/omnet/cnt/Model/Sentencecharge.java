package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SENTENCE_CHARGES")
public class Sentencecharge {
	
	@Id
    @Column(name = "COMMIT_NO")
    private String commitNo;

    @Column(name = "CASE_NUM")
    private String caseNum;

    @Column(name = "CRA_NUM")
    private String craNum;

    @Column(name = "OFFENSE_DATE")
    private Date offenseDate;

    @Column(name = "TIS_NTIS")
    private String tisNtis;

    @Column(name = "SENT_START_DATE")
    private Date sentStartDate;

    @Column(name = "SENT_END_DATE")
    private Date sentEndDate;

    @Column(name = "JAIL_CREDIT_YEARS")
    private Integer jailCreditYears;

    @Column(name = "JAIL_CREDIT_MONTHS")
    private Integer jailCreditMonths;

    @Column(name = "JAIL_CREDIT_DAYS")
    private Integer jailCreditDays;

    @Column(name = "MANDATORY_YEARS")
    private Integer mandatoryYears;

    @Column(name = "MANDATORY_MONTHS")
    private Integer mandatoryMonths;

    @Column(name = "MANDATORY_DAYS")
    private Integer mandatoryDays;

    @Column(name = "MANDATORY_SNTC_LVL_CD")
    private String mandatorySntcLvlCd;

    @Column(name = "CONS_CONC_IND")
    private String consConcInd;

    @Column(name = "CONS_CONC_SNTC_LVL_CD")
    private String consConcSntcLvlCd;

    @Column(name = "CONS_CONC_CASE_NUM")
    private String consConcCaseNum;

    @Column(name = "CONS_CONC_CRA_NUM")
    private String consConcCraNum;

    @Column(name = "STATUTE_IND")
    private String statuteInd;

    @Column(name = "CAL_START_DATE")
    private Date calStartDate;

    @Column(name = "CAL_ME_DATE")
    private Date calMeDate;

    @Column(name = "CAL_MIN_DATE")
    private Date calMinDate;

    @Column(name = "CAL_STR_DATE")
    private Date calStrDate;

    @Column(name = "CAL_ADJ_DATE")
    private Date calAdjDate;

    @Column(name = "CAL_PE_DATE")
    private Date calPeDate;

    @Column(name = "CAL_CR_DATE")
    private Date calCrDate;

    @Column(name = "MED_INDEFINITE_FLAG")
    private String medIndefiniteFlag;

    @Column(name = "COMPLETION_FLAG")
    private String completionFlag;

    @Column(name = "SENTENCE_TYPE_CODE")
    private String sentenceTypeCode;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "CATEGORY_DESC")
    private String categoryDesc;

    @Column(name = "JAIL_CREDIT_APPLIED_FLAG")
    private String jailCreditAppliedFlag;

    @Column(name = "INDEFINITE_FLAG")
    private String indefiniteFlag;

    @Column(name = "ORDER_DATE")
    private Date orderDate;

    @Column(name = "MONEY_OWED")
    private Double moneyOwed;

    @Column(name = "FINES")
    private Double fines;

    @Column(name = "VICTIM_COMPENSATION")
    private Double victimCompensation;

    @Column(name = "DRUG_ALCHL_REHABIT_FUND")
    private Double drugAlchlRehabitFund;

    @Column(name = "RESTITUTION")
    private Double restitution;

    @Column(name = "COURT_COSTS")
    private Double courtCosts;

    @Column(name = "STATUTE_JURISDICTION")
    private String statuteJurisdiction;

    @Column(name = "STATUTE_TITLE")
    private String statuteTitle;

    @Column(name = "STATUTE_SECTION")
    private String statuteSection;

    @Column(name = "STATUTE_SUBSECTION")
    private String statuteSubsection;

    @Column(name = "STATUTE_TYPE")
    private String statuteType;

    @Column(name = "STATUTE_CLASS")
    private String statuteClass;

    @Column(name = "NCIC_CODE")
    private String ncicCode;

    @Column(name = "SENTENCE_CATEGORY")
    private String sentenceCategory;

    @Column(name = "VOP_CONTEMPT_FLG")
    private String vopContemptFlg;

    @Column(name = "OLD_CHRG_SEQ")
    private String oldChrgSeq;
    
    

	public Sentencecharge(String commitNo, String caseNum, String craNum, Date offenseDate, String tisNtis,
			Date sentStartDate, Date sentEndDate, Integer jailCreditYears, Integer jailCreditMonths,
			Integer jailCreditDays, Integer mandatoryYears, Integer mandatoryMonths, Integer mandatoryDays,
			String mandatorySntcLvlCd, String consConcInd, String consConcSntcLvlCd, String consConcCaseNum,
			String consConcCraNum, String statuteInd, Date calStartDate, Date calMeDate, Date calMinDate,
			Date calStrDate, Date calAdjDate, Date calPeDate, Date calCrDate, String medIndefiniteFlag,
			String completionFlag, String sentenceTypeCode, String category, String categoryDesc,
			String jailCreditAppliedFlag, String indefiniteFlag, Date orderDate, Double moneyOwed, Double fines,
			Double victimCompensation, Double drugAlchlRehabitFund, Double restitution, Double courtCosts,
			String statuteJurisdiction, String statuteTitle, String statuteSection, String statuteSubsection,
			String statuteType, String statuteClass, String ncicCode, String sentenceCategory, String vopContemptFlg,
			String oldChrgSeq) {
		super();
		this.commitNo = commitNo;
		this.caseNum = caseNum;
		this.craNum = craNum;
		this.offenseDate = offenseDate;
		this.tisNtis = tisNtis;
		this.sentStartDate = sentStartDate;
		this.sentEndDate = sentEndDate;
		this.jailCreditYears = jailCreditYears;
		this.jailCreditMonths = jailCreditMonths;
		this.jailCreditDays = jailCreditDays;
		this.mandatoryYears = mandatoryYears;
		this.mandatoryMonths = mandatoryMonths;
		this.mandatoryDays = mandatoryDays;
		this.mandatorySntcLvlCd = mandatorySntcLvlCd;
		this.consConcInd = consConcInd;
		this.consConcSntcLvlCd = consConcSntcLvlCd;
		this.consConcCaseNum = consConcCaseNum;
		this.consConcCraNum = consConcCraNum;
		this.statuteInd = statuteInd;
		this.calStartDate = calStartDate;
		this.calMeDate = calMeDate;
		this.calMinDate = calMinDate;
		this.calStrDate = calStrDate;
		this.calAdjDate = calAdjDate;
		this.calPeDate = calPeDate;
		this.calCrDate = calCrDate;
		this.medIndefiniteFlag = medIndefiniteFlag;
		this.completionFlag = completionFlag;
		this.sentenceTypeCode = sentenceTypeCode;
		this.category = category;
		this.categoryDesc = categoryDesc;
		this.jailCreditAppliedFlag = jailCreditAppliedFlag;
		this.indefiniteFlag = indefiniteFlag;
		this.orderDate = orderDate;
		this.moneyOwed = moneyOwed;
		this.fines = fines;
		this.victimCompensation = victimCompensation;
		this.drugAlchlRehabitFund = drugAlchlRehabitFund;
		this.restitution = restitution;
		this.courtCosts = courtCosts;
		this.statuteJurisdiction = statuteJurisdiction;
		this.statuteTitle = statuteTitle;
		this.statuteSection = statuteSection;
		this.statuteSubsection = statuteSubsection;
		this.statuteType = statuteType;
		this.statuteClass = statuteClass;
		this.ncicCode = ncicCode;
		this.sentenceCategory = sentenceCategory;
		this.vopContemptFlg = vopContemptFlg;
		this.oldChrgSeq = oldChrgSeq;
	}

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

	public String getCraNum() {
		return craNum;
	}

	public void setCraNum(String craNum) {
		this.craNum = craNum;
	}

	public Date getOffenseDate() {
		return offenseDate;
	}

	public void setOffenseDate(Date offenseDate) {
		this.offenseDate = offenseDate;
	}

	public String getTisNtis() {
		return tisNtis;
	}

	public void setTisNtis(String tisNtis) {
		this.tisNtis = tisNtis;
	}

	public Date getSentStartDate() {
		return sentStartDate;
	}

	public void setSentStartDate(Date sentStartDate) {
		this.sentStartDate = sentStartDate;
	}

	public Date getSentEndDate() {
		return sentEndDate;
	}

	public void setSentEndDate(Date sentEndDate) {
		this.sentEndDate = sentEndDate;
	}

	public Integer getJailCreditYears() {
		return jailCreditYears;
	}

	public void setJailCreditYears(Integer jailCreditYears) {
		this.jailCreditYears = jailCreditYears;
	}

	public Integer getJailCreditMonths() {
		return jailCreditMonths;
	}

	public void setJailCreditMonths(Integer jailCreditMonths) {
		this.jailCreditMonths = jailCreditMonths;
	}

	public Integer getJailCreditDays() {
		return jailCreditDays;
	}

	public void setJailCreditDays(Integer jailCreditDays) {
		this.jailCreditDays = jailCreditDays;
	}

	public Integer getMandatoryYears() {
		return mandatoryYears;
	}

	public void setMandatoryYears(Integer mandatoryYears) {
		this.mandatoryYears = mandatoryYears;
	}

	public Integer getMandatoryMonths() {
		return mandatoryMonths;
	}

	public void setMandatoryMonths(Integer mandatoryMonths) {
		this.mandatoryMonths = mandatoryMonths;
	}

	public Integer getMandatoryDays() {
		return mandatoryDays;
	}

	public void setMandatoryDays(Integer mandatoryDays) {
		this.mandatoryDays = mandatoryDays;
	}

	public String getMandatorySntcLvlCd() {
		return mandatorySntcLvlCd;
	}

	public void setMandatorySntcLvlCd(String mandatorySntcLvlCd) {
		this.mandatorySntcLvlCd = mandatorySntcLvlCd;
	}

	public String getConsConcInd() {
		return consConcInd;
	}

	public void setConsConcInd(String consConcInd) {
		this.consConcInd = consConcInd;
	}

	public String getConsConcSntcLvlCd() {
		return consConcSntcLvlCd;
	}

	public void setConsConcSntcLvlCd(String consConcSntcLvlCd) {
		this.consConcSntcLvlCd = consConcSntcLvlCd;
	}

	public String getConsConcCaseNum() {
		return consConcCaseNum;
	}

	public void setConsConcCaseNum(String consConcCaseNum) {
		this.consConcCaseNum = consConcCaseNum;
	}

	public String getConsConcCraNum() {
		return consConcCraNum;
	}

	public void setConsConcCraNum(String consConcCraNum) {
		this.consConcCraNum = consConcCraNum;
	}

	public String getStatuteInd() {
		return statuteInd;
	}

	public void setStatuteInd(String statuteInd) {
		this.statuteInd = statuteInd;
	}

	public Date getCalStartDate() {
		return calStartDate;
	}

	public void setCalStartDate(Date calStartDate) {
		this.calStartDate = calStartDate;
	}

	public Date getCalMeDate() {
		return calMeDate;
	}

	public void setCalMeDate(Date calMeDate) {
		this.calMeDate = calMeDate;
	}

	public Date getCalMinDate() {
		return calMinDate;
	}

	public void setCalMinDate(Date calMinDate) {
		this.calMinDate = calMinDate;
	}

	public Date getCalStrDate() {
		return calStrDate;
	}

	public void setCalStrDate(Date calStrDate) {
		this.calStrDate = calStrDate;
	}

	public Date getCalAdjDate() {
		return calAdjDate;
	}

	public void setCalAdjDate(Date calAdjDate) {
		this.calAdjDate = calAdjDate;
	}

	public Date getCalPeDate() {
		return calPeDate;
	}

	public void setCalPeDate(Date calPeDate) {
		this.calPeDate = calPeDate;
	}

	public Date getCalCrDate() {
		return calCrDate;
	}

	public void setCalCrDate(Date calCrDate) {
		this.calCrDate = calCrDate;
	}

	public String getMedIndefiniteFlag() {
		return medIndefiniteFlag;
	}

	public void setMedIndefiniteFlag(String medIndefiniteFlag) {
		this.medIndefiniteFlag = medIndefiniteFlag;
	}

	public String getCompletionFlag() {
		return completionFlag;
	}

	public void setCompletionFlag(String completionFlag) {
		this.completionFlag = completionFlag;
	}

	public String getSentenceTypeCode() {
		return sentenceTypeCode;
	}

	public void setSentenceTypeCode(String sentenceTypeCode) {
		this.sentenceTypeCode = sentenceTypeCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getJailCreditAppliedFlag() {
		return jailCreditAppliedFlag;
	}

	public void setJailCreditAppliedFlag(String jailCreditAppliedFlag) {
		this.jailCreditAppliedFlag = jailCreditAppliedFlag;
	}

	public String getIndefiniteFlag() {
		return indefiniteFlag;
	}

	public void setIndefiniteFlag(String indefiniteFlag) {
		this.indefiniteFlag = indefiniteFlag;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Double getMoneyOwed() {
		return moneyOwed;
	}

	public void setMoneyOwed(Double moneyOwed) {
		this.moneyOwed = moneyOwed;
	}

	public Double getFines() {
		return fines;
	}

	public void setFines(Double fines) {
		this.fines = fines;
	}

	public Double getVictimCompensation() {
		return victimCompensation;
	}

	public void setVictimCompensation(Double victimCompensation) {
		this.victimCompensation = victimCompensation;
	}

	public Double getDrugAlchlRehabitFund() {
		return drugAlchlRehabitFund;
	}

	public void setDrugAlchlRehabitFund(Double drugAlchlRehabitFund) {
		this.drugAlchlRehabitFund = drugAlchlRehabitFund;
	}

	public Double getRestitution() {
		return restitution;
	}

	public void setRestitution(Double restitution) {
		this.restitution = restitution;
	}

	public Double getCourtCosts() {
		return courtCosts;
	}

	public void setCourtCosts(Double courtCosts) {
		this.courtCosts = courtCosts;
	}

	public String getStatuteJurisdiction() {
		return statuteJurisdiction;
	}

	public void setStatuteJurisdiction(String statuteJurisdiction) {
		this.statuteJurisdiction = statuteJurisdiction;
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

	public String getStatuteType() {
		return statuteType;
	}

	public void setStatuteType(String statuteType) {
		this.statuteType = statuteType;
	}

	public String getStatuteClass() {
		return statuteClass;
	}

	public void setStatuteClass(String statuteClass) {
		this.statuteClass = statuteClass;
	}

	public String getNcicCode() {
		return ncicCode;
	}

	public void setNcicCode(String ncicCode) {
		this.ncicCode = ncicCode;
	}

	public String getSentenceCategory() {
		return sentenceCategory;
	}

	public void setSentenceCategory(String sentenceCategory) {
		this.sentenceCategory = sentenceCategory;
	}

	public String getVopContemptFlg() {
		return vopContemptFlg;
	}

	public void setVopContemptFlg(String vopContemptFlg) {
		this.vopContemptFlg = vopContemptFlg;
	}

	public String getOldChrgSeq() {
		return oldChrgSeq;
	}

	public void setOldChrgSeq(String oldChrgSeq) {
		this.oldChrgSeq = oldChrgSeq;
	}
    

}
