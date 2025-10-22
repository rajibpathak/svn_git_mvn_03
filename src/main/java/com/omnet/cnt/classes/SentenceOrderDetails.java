package com.omnet.cnt.classes;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class SentenceOrderDetails {
	
	@Id
	private String n;
	@JsonProperty("COMMIT_NO")
	private String commitNo;
	@JsonProperty("SOH_SEQ_NUM")
	private BigDecimal SOH_SEQ_NUM;
	@JsonProperty("SOH_CAL_SEQ_NUM")
    private BigDecimal sohCalSeqNum;
	@JsonProperty("SOH_SNTC_LVL_SEQ_NUM")
    private BigDecimal sohSntcLvlSeqNum;
	@JsonProperty("SOH_SNTC_ORD_SEQ_NUM")
    private BigDecimal sohSntcOrdSeqNum;
    private String seqNum;
    public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	@JsonProperty("ORDER_TYPE")
	private String orderType;
    @JsonProperty("ORDER_TYPE_DESC")
    private String ORDER_TYPE_DESC;
    @JsonProperty("CASE_NUM")
    private String SOH_CASE_NUM;
    @JsonProperty("SHORT_DESC")
    private String SOH_CHARGE_DESC;
    @JsonProperty("CRA_NUM")
    private String craNum;
    @JsonProperty("CHARGE_NUM")
    private String SOH_CHARGE_NUM;
    @JsonProperty("COURT_CODE")
    private String courtCode;
    @JsonProperty("JUDGE_CODE")
    private String judgeCode;
    @JsonProperty("OFFENSE_DATE")
    private Date offenseDate;
    @JsonProperty("NCIC_CODE")
    private String ncicCode;
    @JsonProperty("SOH_SENT_DATE")
    private Date sohSentDate;
    @JsonProperty("SOH_SNTC_LVL_CD")
    private String SOH_SNTC_LVL_CD;
    @JsonProperty("CASE_NUM_OLD")
    private String caseNumOld;
    @JsonProperty("SENTENCE_CATEGORY")
    private String sentenceCategory;
    
    @JsonProperty("LENGTH_YY")
    private BigDecimal lengthYy;
    @JsonProperty("LENGTH_MM")
    private BigDecimal lengthMm;
    @JsonProperty("LENGTH_DD")
    private BigDecimal lengthDd;
    @JsonProperty("EFFECTIVE_DATE")
    private Date SOH_EFF_DATE;
    @JsonProperty("HELD_AT")
    private String heldAt;
    @JsonProperty("JAIL_CREDIT_YY")
    private BigDecimal jailCreditYy;
    @JsonProperty("JAIL_CREDIT_MM")
    private BigDecimal jailCreditMm;
    @JsonProperty("JAIL_CREDIT_DD")
    private BigDecimal jailCreditDd;
    @JsonProperty("MIN_MAND_YY")
    private BigDecimal minMandYy;
    @JsonProperty("MIN_MAND_MM")
    private BigDecimal minMandMm;
    @JsonProperty("MIN_MAND_DD")
    private BigDecimal minMandDd;
    @JsonProperty("ABS_LEN_YY")
    private BigDecimal absLenYy;
    @JsonProperty("ABS_LEN_MM")
    private BigDecimal absLenMm;
    @JsonProperty("ABS_LEN_DD")
    private BigDecimal absLenDd;
    @JsonProperty("DEFERRED_FLAG")
    private String deferredFlag;
    @JsonProperty("DEFERRED_DATE")
    private Date deferredDate;
    @JsonProperty("TIS_NTIS")
    private String SOH_TIS_NTIS;
    @JsonProperty("SOH_SNTC_TP_CD")
    private String SOH_SNTC_TP_CD;
    @JsonProperty("FLAG_4204K")
    private String flag4204k;
    
    @JsonProperty("SOH_STATUTE_JURISDICTION")
    private String sohStatuteJurisdiction;
    @JsonProperty("SOH_STATUTE_TITLE")
    private String sohStatuteTitle;
    @JsonProperty("SOH_STATUTE_SECTION")
    private String sohStatuteSection;
    @JsonProperty("SOH_STATUTE_SUBSECTION")
    private String sohStatuteSubsection;
    @JsonProperty("SOH_STATUTE_TYPE")
    private String sohStatuteType;
    @JsonProperty("SOH_STATUTE_CLASS")
    private String sohStatuteClass;
    private String sohSentenceCategory;
    private String deferredInstNum;
    @JsonProperty("SOH_WEEKENDER_DAYS")
    private BigDecimal sohWeekenderDays;
    private String sohIndefiniteFlag;
    private String sohGdtmCalcFlag;
    @JsonProperty("SOH_MERIT_DAYS")
    private BigDecimal sohMeritDays;
    @JsonProperty("SOH_RELEASE_DATE")
    private Date sohReleaseDate;
    @JsonProperty("SOH_RELEASE_TIME")
    private String sohReleaseTime;
    @JsonProperty("ELIGIBLE_FR_PAROLE_FLAG")
    private String eligibleFrParoleFlag;
    private String sohStatus;
    @JsonProperty("APRV_FLAG")
    private String aprvFlag;
    @JsonProperty("APRV_USER_ID")
    private String aprvUserId;
    @JsonProperty("APRV_DATE")
    private Date aprvDate;
    @JsonProperty("APRV_TIME")
    private String aprvTime;
    private String aprvReasonCode;
    private String aprvUserComnts;
    private String sprvDecision;
    @JsonProperty("SPRV_USER_ID")
    private String sprvUserId;
    @JsonProperty("SPRV_DATE")
    private Date sprvDate;
    @JsonProperty("SPRV_TIME")
    private String sprvTime;
    private String sprvUserComnts;
    private BigDecimal refSohSeqNum;
    private String sohModificationType;
    private String sentNewFlag;
    private String sohReleaseComnts;
    private String sohReleaseBy;
    private String sohReleaseMethod;
    private String sohAdmissMethod;
    private String sohCancelFlag;
    public String getOrderTypeDesc() {
		return ORDER_TYPE_DESC;
	}
	public void setOrderTypeDesc(String orderTypeDesc) {
		this.ORDER_TYPE_DESC = orderTypeDesc;
	}
	public String getChargeDesc() {
		return SOH_CHARGE_DESC;
	}
	public void setChargeDesc(String chargeDesc) {
		this.SOH_CHARGE_DESC = chargeDesc;
	}
	public String getSentenceTypeCd() {
		return SOH_SNTC_TP_CD;
	}
	public void setSentenceTypeCd(String sentenceTypeCd) {
		this.SOH_SNTC_TP_CD = sentenceTypeCd;
	}
	public String getDeferredInstNum() {
		return deferredInstNum;
	}
	public void setDeferredInstNum(String deferredInstNum) {
		this.deferredInstNum = deferredInstNum;
	}
	public String getAprvFlag() {
		return aprvFlag;
	}
	public void setAprvFlag(String aprvFlag) {
		this.aprvFlag = aprvFlag;
	}
	public String getAprvUserId() {
		return aprvUserId;
	}
	public void setAprvUserId(String aprvUserId) {
		this.aprvUserId = aprvUserId;
	}
	public Date getAprvDate() {
		return aprvDate;
	}
	public void setAprvDate(Date aprvDate) {
		this.aprvDate = aprvDate;
	}
	public String getAprvTime() {
		return aprvTime;
	}
	public void setAprvTime(String aprvTime) {
		this.aprvTime = aprvTime;
	}
	public String getAprvReasonCode() {
		return aprvReasonCode;
	}
	public void setAprvReasonCode(String aprvReasonCode) {
		this.aprvReasonCode = aprvReasonCode;
	}
	public String getAprvUserComnts() {
		return aprvUserComnts;
	}
	public void setAprvUserComnts(String aprvUserComnts) {
		this.aprvUserComnts = aprvUserComnts;
	}
	public String getSprvUserId() {
		return sprvUserId;
	}
	public void setSprvUserId(String sprvUserId) {
		this.sprvUserId = sprvUserId;
	}
	public Date getSprvDate() {
		return sprvDate;
	}
	public void setSprvDate(Date sprvDate) {
		this.sprvDate = sprvDate;
	}
	public String getSprvTime() {
		return sprvTime;
	}
	public void setSprvTime(String sprvTime) {
		this.sprvTime = sprvTime;
	}
	public String getSprvUserComnts() {
		return sprvUserComnts;
	}
	public void setSprvUserComnts(String sprvUserComnts) {
		this.sprvUserComnts = sprvUserComnts;
	}
	public String getSohCancelFlag() {
		return sohCancelFlag;
	}
	public void setSohCancelFlag(String sohCancelFlag) {
		this.sohCancelFlag = sohCancelFlag;
	}
	public String getSohCancelBy() {
		return sohCancelBy;
	}
	public void setSohCancelBy(String sohCancelBy) {
		this.sohCancelBy = sohCancelBy;
	}
	public Date getSohCancelDate() {
		return sohCancelDate;
	}
	public void setSohCancelDate(Date sohCancelDate) {
		this.sohCancelDate = sohCancelDate;
	}
	public String getSohCancelTime() {
		return sohCancelTime;
	}
	public void setSohCancelTime(String sohCancelTime) {
		this.sohCancelTime = sohCancelTime;
	}
	public String getSohCancelComnts() {
		return sohCancelComnts;
	}
	public void setSohCancelComnts(String sohCancelComnts) {
		this.sohCancelComnts = sohCancelComnts;
	}
	private String sohCancelBy;
    private Date sohCancelDate;
    private String sohCancelTime;
    private String sohCancelComnts;
    
    @JsonProperty("SENTENCE_TYPE")
    private String sentenceType;
    
    private String sohNcicCode;
    private BigDecimal fines;
    private BigDecimal courtCosts;
    private BigDecimal restitution;
    private BigDecimal drugAlchlRehabitFund;
    private Double victimCompensation;
    private Double moneyOwed;
    private Double monthlyPayment;
    private Date firstDueDate;
    @JsonProperty("FACILITY")
    private String facility;
    private String sohIntrptSent;
    private Date sohIntrptDate;
    private String sohDocDiscretion;
    private Boolean paroleVViolFlag;
    private Date paroleVIncarcDate;
    private Date paroleVParoleDate;
    private Date paroleVOriginalEffDt;
    private BigDecimal paroleVOrgnlCalSeqNum;
    private Date paroleVMed;
    private Date paroleVWarrantDate;
    private BigDecimal paroleVAbscondTime;
    private Boolean paroleVAllGdtm;
    private BigDecimal paroleVGoodTime;
    private Boolean paroleVEligibleFrParole;
    private Date crVCrDate;
    private Date crVOrigninalEffDate;
    private BigDecimal crVOrgnlCalSeqNum;
    private String crVCrViolation;
    private Date crVIncurcDate;
    private Date crVMed;
    private Date crVWarrantDate;
    private BigDecimal crVAbscondDays;
    private String crVAllGdtm;
    private BigDecimal crVGoodTime;
    
    private String insUpdFlag;
    private String shortDesc;
    private String sentenceOrderFlag;
    
    @JsonProperty("CONS_CONC_FLAG")
    private String consConcFlag;
    @JsonProperty("CONS_CONC_SNTC_ORD_SEQ_NUM")
    private BigDecimal consConcSntcOrdSeqNum;
    @JsonProperty("CONS_CONC_SOH_SEQ_NUM")
    private BigDecimal consConcSohSeqNum;
    @JsonProperty("CONS_CONC_LVL5")
    private String consConcLvl5;
    @JsonProperty("CONS_CONC_LVL4")
    private String consConcLvl4;
    @JsonProperty("CONS_CONC_LVL4H")
    private String consConcLvl4h;
    @JsonProperty("CONS_CONC_LVL3")
    private String consConcLvl3;
    @JsonProperty("CONS_CONC_LVL2")
    private String consConcLvl2;
    @JsonProperty("CONS_CONC_LVL1")
    private String consConcLvl1;
    @JsonProperty("CONS_CONC_LVL1R")
    private String consConcLvl1r;
    private String soh4382aFlag;
    
    public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public BigDecimal getSohCalSeqNum() {
		return sohCalSeqNum;
	}
	public void setSohCalSeqNum(BigDecimal sohCalSeqNum) {
		this.sohCalSeqNum = sohCalSeqNum;
	}
	public BigDecimal getSohSntcLvlSeqNum() {
		return sohSntcLvlSeqNum;
	}
	public void setSohSntcLvlSeqNum(BigDecimal sohSntcLvlSeqNum) {
		this.sohSntcLvlSeqNum = sohSntcLvlSeqNum;
	}
	public BigDecimal getSohSntcOrdSeqNum() {
		return sohSntcOrdSeqNum;
	}
	public void setSohSntcOrdSeqNum(BigDecimal sohSntcOrdSeqNum) {
		this.sohSntcOrdSeqNum = sohSntcOrdSeqNum;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCaseNum() {
		return SOH_CASE_NUM;
	}
	public void setCaseNum(String caseNum) {
		this.SOH_CASE_NUM = caseNum;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getCraNum() {
		return craNum;
	}
	public void setCraNum(String craNum) {
		this.craNum = craNum;
	}
	public String getChargeNum() {
		return SOH_CHARGE_NUM;
	}
	public void setChargeNum(String chargeNum) {
		this.SOH_CHARGE_NUM = chargeNum;
	}
	public String getCourtCode() {
		return courtCode;
	}
	public void setCourtCode(String courtCode) {
		this.courtCode = courtCode;
	}
	public String getJudgeCode() {
		return judgeCode;
	}
	public void setJudgeCode(String judgeCode) {
		this.judgeCode = judgeCode;
	}
	public Date getOffenseDate() {
		return offenseDate;
	}
	public void setOffenseDate(Date offenseDate) {
		this.offenseDate = offenseDate;
	}
	public String getCaseNumOld() {
		return caseNumOld;
	}
	public void setCaseNumOld(String caseNumOld) {
		this.caseNumOld = caseNumOld;
	}
	public String getSentenceCategory() {
		return sentenceCategory;
	}
	public void setSentenceCategory(String sentenceCategory) {
		this.sentenceCategory = sentenceCategory;
	}
	public String getNcicCode() {
		return ncicCode;
	}
	public void setNcicCode(String ncicCode) {
		this.ncicCode = ncicCode;
	}
	public BigDecimal getSohSeqNum() {
		return SOH_SEQ_NUM;
	}
	public void setSohSeqNum(BigDecimal sohSeqNum) {
		this.SOH_SEQ_NUM = sohSeqNum;
	}
	public Date getSohSentDate() {
		return sohSentDate;
	}
	public void setSohSentDate(Date sohSentDate) {
		this.sohSentDate = sohSentDate;
	}
	public String getSohSntcLvlCd() {
		return SOH_SNTC_LVL_CD;
	}
	public void setSohSntcLvlCd(String sohSntcLvlCd) {
		this.SOH_SNTC_LVL_CD = sohSntcLvlCd;
	}
	public BigDecimal getLengthYy() {
		return lengthYy;
	}
	public void setLengthYy(BigDecimal lengthYy) {
		this.lengthYy = lengthYy;
	}
	public BigDecimal getLengthMm() {
		return lengthMm;
	}
	public void setLengthMm(BigDecimal lengthMm) {
		this.lengthMm = lengthMm;
	}
	public BigDecimal getLengthDd() {
		return lengthDd;
	}
	public void setLengthDd(BigDecimal lengthDd) {
		this.lengthDd = lengthDd;
	}
	public Date getEffectiveDate() {
		return SOH_EFF_DATE;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.SOH_EFF_DATE = effectiveDate;
	}
	public String getHeldAt() {
		return heldAt;
	}
	public void setHeldAt(String heldAt) {
		this.heldAt = heldAt;
	}
	public BigDecimal getJailCreditYy() {
		return jailCreditYy;
	}
	public void setJailCreditYy(BigDecimal jailCreditYy) {
		this.jailCreditYy = jailCreditYy;
	}
	public BigDecimal getJailCreditMm() {
		return jailCreditMm;
	}
	public void setJailCreditMm(BigDecimal jailCreditMm) {
		this.jailCreditMm = jailCreditMm;
	}
	public BigDecimal getJailCreditDd() {
		return jailCreditDd;
	}
	public void setJailCreditDd(BigDecimal jailCreditDd) {
		this.jailCreditDd = jailCreditDd;
	}
	public BigDecimal getMinMandYy() {
		return minMandYy;
	}
	public void setMinMandYy(BigDecimal minMandYy) {
		this.minMandYy = minMandYy;
	}
	public BigDecimal getMinMandMm() {
		return minMandMm;
	}
	public void setMinMandMm(BigDecimal minMandMm) {
		this.minMandMm = minMandMm;
	}
	public BigDecimal getMinMandDd() {
		return minMandDd;
	}
	public void setMinMandDd(BigDecimal minMandDd) {
		this.minMandDd = minMandDd;
	}
	public BigDecimal getAbsLenYy() {
		return absLenYy;
	}
	public void setAbsLenYy(BigDecimal absLenYy) {
		this.absLenYy = absLenYy;
	}
	public BigDecimal getAbsLenMm() {
		return absLenMm;
	}
	public void setAbsLenMm(BigDecimal absLenMm) {
		this.absLenMm = absLenMm;
	}
	public BigDecimal getAbsLenDd() {
		return absLenDd;
	}
	public void setAbsLenDd(BigDecimal absLenDd) {
		this.absLenDd = absLenDd;
	}
	public String getDeferredFlag() {
		return deferredFlag;
	}
	public void setDeferredFlag(String deferredFlag) {
		this.deferredFlag = deferredFlag;
	}
	public Date getDeferredDate() {
		return deferredDate;
	}
	public void setDeferredDate(Date deferredDate) {
		this.deferredDate = deferredDate;
	}
	public String getTisNtis() {
		return SOH_TIS_NTIS;
	}
	public void setTisNtis(String tisNtis) {
		this.SOH_TIS_NTIS = tisNtis;
	}
	public String getSentenceType() {
		return sentenceType;
	}
	public void setSentenceType(String sentenceType) {
		this.sentenceType = sentenceType;
	}
	public String getFlag4204k() {
		return flag4204k;
	}
	public void setFlag4204k(String flag4204k) {
		this.flag4204k = flag4204k;
	}
	public String getSohStatuteJurisdiction() {
		return sohStatuteJurisdiction;
	}
	public void setSohStatuteJurisdiction(String sohStatuteJurisdiction) {
		this.sohStatuteJurisdiction = sohStatuteJurisdiction;
	}
	public String getSohStatuteTitle() {
		return sohStatuteTitle;
	}
	public void setSohStatuteTitle(String sohStatuteTitle) {
		this.sohStatuteTitle = sohStatuteTitle;
	}
	public String getSohStatuteSection() {
		return sohStatuteSection;
	}
	public void setSohStatuteSection(String sohStatuteSection) {
		this.sohStatuteSection = sohStatuteSection;
	}
	public String getSohStatuteSubsection() {
		return sohStatuteSubsection;
	}
	public void setSohStatuteSubsection(String sohStatuteSubsection) {
		this.sohStatuteSubsection = sohStatuteSubsection;
	}
	public String getSohStatuteType() {
		return sohStatuteType;
	}
	public void setSohStatuteType(String sohStatuteType) {
		this.sohStatuteType = sohStatuteType;
	}
	public String getSohStatuteClass() {
		return sohStatuteClass;
	}
	public void setSohStatuteClass(String sohStatuteClass) {
		this.sohStatuteClass = sohStatuteClass;
	}
	public String getSohSentenceCategory() {
		return sohSentenceCategory;
	}
	public void setSohSentenceCategory(String sohSentenceCategory) {
		this.sohSentenceCategory = sohSentenceCategory;
	}
	public String getSohNcicCode() {
		return sohNcicCode;
	}
	public void setSohNcicCode(String sohNcicCode) {
		this.sohNcicCode = sohNcicCode;
	}
	public BigDecimal getFines() {
		return fines;
	}
	public void setFines(BigDecimal fines) {
		this.fines = fines;
	}
	public BigDecimal getCourtCosts() {
		return courtCosts;
	}
	public void setCourtCosts(BigDecimal courtCosts) {
		this.courtCosts = courtCosts;
	}
	public BigDecimal getRestitution() {
		return restitution;
	}
	public void setRestitution(BigDecimal restitution) {
		this.restitution = restitution;
	}
	public BigDecimal getDrugAlchlRehabitFund() {
		return drugAlchlRehabitFund;
	}
	public void setDrugAlchlRehabitFund(BigDecimal drugAlchlRehabitFund) {
		this.drugAlchlRehabitFund = drugAlchlRehabitFund;
	}
	public Double getVictimCompensation() {
		return victimCompensation;
	}
	public void setVictimCompensation(Double victimCompensation) {
		this.victimCompensation = victimCompensation;
	}
	public Double getMoneyOwed() {
		return moneyOwed;
	}
	public void setMoneyOwed(Double moneyOwed) {
		this.moneyOwed = moneyOwed;
	}
	public Double getMonthlyPayment() {
		return monthlyPayment;
	}
	public void setMonthlyPayment(Double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}
	public Date getFirstDueDate() {
		return firstDueDate;
	}
	public void setFirstDueDate(Date firstDueDate) {
		this.firstDueDate = firstDueDate;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public BigDecimal getSohWeekenderDays() {
		return sohWeekenderDays;
	}
	public void setSohWeekenderDays(BigDecimal sohWeekenderDays) {
		this.sohWeekenderDays = sohWeekenderDays;
	}
	public String getSohIntrptSent() {
		return sohIntrptSent;
	}
	public void setSohIntrptSent(String sohIntrptSent) {
		this.sohIntrptSent = sohIntrptSent;
	}
	public Date getSohIntrptDate() {
		return sohIntrptDate;
	}
	public void setSohIntrptDate(Date sohIntrptDate) {
		this.sohIntrptDate = sohIntrptDate;
	}
	public String getSohDocDiscretion() {
		return sohDocDiscretion;
	}
	public void setSohDocDiscretion(String sohDocDiscretion) {
		this.sohDocDiscretion = sohDocDiscretion;
	}
	public String getSohIndefiniteFlag() {
		return sohIndefiniteFlag;
	}
	public void setSohIndefiniteFlag(String sohIndefiniteFlag) {
		this.sohIndefiniteFlag = sohIndefiniteFlag;
	}
	public String getSohGdtmCalcFlag() {
		return sohGdtmCalcFlag;
	}
	public void setSohGdtmCalcFlag(String sohGdtmCalcFlag) {
		this.sohGdtmCalcFlag = sohGdtmCalcFlag;
	}
	public BigDecimal getSohMeritDays() {
		return sohMeritDays;
	}
	public void setSohMeritDays(BigDecimal sohMeritDays) {
		this.sohMeritDays = sohMeritDays;
	}
	public Date getSohReleaseDate() {
		return sohReleaseDate;
	}
	public void setSohReleaseDate(Date sohReleaseDate) {
		this.sohReleaseDate = sohReleaseDate;
	}
	public String getSohReleaseTime() {
		return sohReleaseTime;
	}
	public void setSohReleaseTime(String sohReleaseTime) {
		this.sohReleaseTime = sohReleaseTime;
	}
	public String getEligibleFrParoleFlag() {
		return eligibleFrParoleFlag;
	}
	public void setEligibleFrParoleFlag(String eligibleFrParoleFlag) {
		this.eligibleFrParoleFlag = eligibleFrParoleFlag;
	}
	public Boolean getParoleVViolFlag() {
		return paroleVViolFlag;
	}
	public void setParoleVViolFlag(Boolean paroleVViolFlag) {
		this.paroleVViolFlag = paroleVViolFlag;
	}
	public Date getParoleVIncarcDate() {
		return paroleVIncarcDate;
	}
	public void setParoleVIncarcDate(Date paroleVIncarcDate) {
		this.paroleVIncarcDate = paroleVIncarcDate;
	}
	public Date getParoleVParoleDate() {
		return paroleVParoleDate;
	}
	public void setParoleVParoleDate(Date paroleVParoleDate) {
		this.paroleVParoleDate = paroleVParoleDate;
	}
	public Date getParoleVOriginalEffDt() {
		return paroleVOriginalEffDt;
	}
	public void setParoleVOriginalEffDt(Date paroleVOriginalEffDt) {
		this.paroleVOriginalEffDt = paroleVOriginalEffDt;
	}
	public BigDecimal getParoleVOrgnlCalSeqNum() {
		return paroleVOrgnlCalSeqNum;
	}
	public void setParoleVOrgnlCalSeqNum(BigDecimal paroleVOrgnlCalSeqNum) {
		this.paroleVOrgnlCalSeqNum = paroleVOrgnlCalSeqNum;
	}
	public Date getParoleVMed() {
		return paroleVMed;
	}
	public void setParoleVMed(Date paroleVMed) {
		this.paroleVMed = paroleVMed;
	}
	public Date getParoleVWarrantDate() {
		return paroleVWarrantDate;
	}
	public void setParoleVWarrantDate(Date paroleVWarrantDate) {
		this.paroleVWarrantDate = paroleVWarrantDate;
	}
	public BigDecimal getParoleVAbscondTime() {
		return paroleVAbscondTime;
	}
	public void setParoleVAbscondTime(BigDecimal paroleVAbscondTime) {
		this.paroleVAbscondTime = paroleVAbscondTime;
	}
	public Boolean getParoleVAllGdtm() {
		return paroleVAllGdtm;
	}
	public void setParoleVAllGdtm(Boolean paroleVAllGdtm) {
		this.paroleVAllGdtm = paroleVAllGdtm;
	}
	public BigDecimal getParoleVGoodTime() {
		return paroleVGoodTime;
	}
	public void setParoleVGoodTime(BigDecimal paroleVGoodTime) {
		this.paroleVGoodTime = paroleVGoodTime;
	}
	public Boolean getParoleVEligibleFrParole() {
		return paroleVEligibleFrParole;
	}
	public void setParoleVEligibleFrParole(Boolean paroleVEligibleFrParole) {
		this.paroleVEligibleFrParole = paroleVEligibleFrParole;
	}
	public Date getCrVCrDate() {
		return crVCrDate;
	}
	public void setCrVCrDate(Date crVCrDate) {
		this.crVCrDate = crVCrDate;
	}
	public Date getCrVOrigninalEffDate() {
		return crVOrigninalEffDate;
	}
	public void setCrVOrigninalEffDate(Date crVOrigninalEffDate) {
		this.crVOrigninalEffDate = crVOrigninalEffDate;
	}
	public BigDecimal getCrVOrgnlCalSeqNum() {
		return crVOrgnlCalSeqNum;
	}
	public void setCrVOrgnlCalSeqNum(BigDecimal crVOrgnlCalSeqNum) {
		this.crVOrgnlCalSeqNum = crVOrgnlCalSeqNum;
	}
	public String getCrVCrViolation() {
		return crVCrViolation;
	}
	public void setCrVCrViolation(String crVCrViolation) {
		this.crVCrViolation = crVCrViolation;
	}
	public Date getCrVIncurcDate() {
		return crVIncurcDate;
	}
	public void setCrVIncurcDate(Date crVIncurcDate) {
		this.crVIncurcDate = crVIncurcDate;
	}
	public Date getCrVMed() {
		return crVMed;
	}
	public void setCrVMed(Date crVMed) {
		this.crVMed = crVMed;
	}
	public Date getCrVWarrantDate() {
		return crVWarrantDate;
	}
	public void setCrVWarrantDate(Date crVWarrantDate) {
		this.crVWarrantDate = crVWarrantDate;
	}
	public BigDecimal getCrVAbscondDays() {
		return crVAbscondDays;
	}
	public void setCrVAbscondDays(BigDecimal crVAbscondDays) {
		this.crVAbscondDays = crVAbscondDays;
	}
	public String getCrVAllGdtm() {
		return crVAllGdtm;
	}
	public void setCrVAllGdtm(String crVAllGdtm) {
		this.crVAllGdtm = crVAllGdtm;
	}
	public BigDecimal getCrVGoodTime() {
		return crVGoodTime;
	}
	public void setCrVGoodTime(BigDecimal crVGoodTime) {
		this.crVGoodTime = crVGoodTime;
	}
	public String getSohStatus() {
		return sohStatus;
	}
	public void setSohStatus(String sohStatus) {
		this.sohStatus = sohStatus;
	}
	public String getSprvDecision() {
		return sprvDecision;
	}
	public void setSprvDecision(String sprvDecision) {
		this.sprvDecision = sprvDecision;
	}
	public BigDecimal getRefSohSeqNum() {
		return refSohSeqNum;
	}
	public void setRefSohSeqNum(BigDecimal refSohSeqNum) {
		this.refSohSeqNum = refSohSeqNum;
	}
	public String getInsUpdFlag() {
		return insUpdFlag;
	}
	public void setInsUpdFlag(String insUpdFlag) {
		this.insUpdFlag = insUpdFlag;
	}
	public String getSohModificationType() {
		return sohModificationType;
	}
	public void setSohModificationType(String sohModificationType) {
		this.sohModificationType = sohModificationType;
	}
	public String getSentNewFlag() {
		return sentNewFlag;
	}
	public void setSentNewFlag(String sentNewFlag) {
		this.sentNewFlag = sentNewFlag;
	}
	public String getSentenceOrderFlag() {
		return sentenceOrderFlag;
	}
	public void setSentenceOrderFlag(String sentenceOrderFlag) {
		this.sentenceOrderFlag = sentenceOrderFlag;
	}
	public String getSohReleaseComnts() {
		return sohReleaseComnts;
	}
	public void setSohReleaseComnts(String sohReleaseComnts) {
		this.sohReleaseComnts = sohReleaseComnts;
	}
	public String getSohReleaseBy() {
		return sohReleaseBy;
	}
	public void setSohReleaseBy(String sohReleaseBy) {
		this.sohReleaseBy = sohReleaseBy;
	}
	public String getSohReleaseMethod() {
		return sohReleaseMethod;
	}
	public void setSohReleaseMethod(String sohReleaseMethod) {
		this.sohReleaseMethod = sohReleaseMethod;
	}
	public String getSohAdmissMethod() {
		return sohAdmissMethod;
	}
	public void setSohAdmissMethod(String sohAdmissMethod) {
		this.sohAdmissMethod = sohAdmissMethod;
	}
	public String getConsConcFlag() {
		return consConcFlag;
	}
	public void setConsConcFlag(String consConcFlag) {
		this.consConcFlag = consConcFlag;
	}
	public BigDecimal getConsConcSntcOrdSeqNum() {
		return consConcSntcOrdSeqNum;
	}
	public void setConsConcSntcOrdSeqNum(BigDecimal consConcSntcOrdSeqNum) {
		this.consConcSntcOrdSeqNum = consConcSntcOrdSeqNum;
	}
	public BigDecimal getConsConcSohSeqNum() {
		return consConcSohSeqNum;
	}
	public void setConsConcSohSeqNum(BigDecimal consConcSohSeqNum) {
		this.consConcSohSeqNum = consConcSohSeqNum;
	}
	public String getConsConcLvl5() {
		return consConcLvl5;
	}
	public void setConsConcLvl5(String consConcLvl5) {
		this.consConcLvl5 = consConcLvl5;
	}
	public String getConsConcLvl4() {
		return consConcLvl4;
	}
	public void setConsConcLvl4(String consConcLvl4) {
		this.consConcLvl4 = consConcLvl4;
	}
	public String getConsConcLvl4h() {
		return consConcLvl4h;
	}
	public void setConsConcLvl4h(String consConcLvl4h) {
		this.consConcLvl4h = consConcLvl4h;
	}
	public String getConsConcLvl3() {
		return consConcLvl3;
	}
	public void setConsConcLvl3(String consConcLvl3) {
		this.consConcLvl3 = consConcLvl3;
	}
	public String getConsConcLvl2() {
		return consConcLvl2;
	}
	public void setConsConcLvl2(String consConcLvl2) {
		this.consConcLvl2 = consConcLvl2;
	}
	public String getConsConcLvl1() {
		return consConcLvl1;
	}
	public void setConsConcLvl1(String consConcLvl1) {
		this.consConcLvl1 = consConcLvl1;
	}
	public String getConsConcLvl1r() {
		return consConcLvl1r;
	}
	public void setConsConcLvl1r(String consConcLvl1r) {
		this.consConcLvl1r = consConcLvl1r;
	}
	public String getSoh4382aFlag() {
		return soh4382aFlag;
	}
	public void setSoh4382aFlag(String soh4382aFlag) {
		this.soh4382aFlag = soh4382aFlag;
	}
}
