package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BUILDING")
public class Building {
	
	// Constructors
    public Building() {
    }
	  @Id
	    @Column(name = "bld_num", length = 15)
	    private String bldNum;

	    @Column(name = "bld_name", length = 30)
	    private String bldName;

	    @Column(name = "bld_status", length = 1)
	    private String bldStatus;

	    @Column(name = "status", length = 1)
	    private String status;

	    @Column(name = "inst_num", length = 6)
	    private String instNum;

	    @Column(name = "SEC_CUST_LVL_CD", length = 1)
	    private String secCustLvlCd;

	    @Column(name = "PROGRAM_ID", length = 1)
	    private String programId;

	    @Column(name = "RATED_CAPACITY")
	    private Integer ratedCapacity;

	    @Column(name = "BLD_SEX", length = 1)
	    private String bldSex;

	    @Column(name = "JUVENILE_FLAG", length = 1)
	    private String juvenileFlag;

	    @Column(name = "CLOSED_CUSTODY_FLAG", length = 1)
	    private String closedCustodyFlag;

	    @Column(name = "BLD_SPCL_COND_DESC", length = 40)
	    private String bldSpclCondDesc;

	    @Column(name = "REASON_OFFLINE_CODE", length = 4)
	    private String reasonOfflineCode;

	    @Column(name = "CURRENT_CAPACITY")
	    private Integer currentCapacity;

	    @Column(name = "BLD_CENSUS")
	    private Integer bldCensus;

	    @Column(name = "DORM_FLAG", length = 1)
	    private String dormFlag;

	    @Column(name = "INST_SECURITY_LEVEL", length = 2)
	    private String instSecurityLevel;

	    @Column(name = "RUNNING_COUNT")
	    private Integer runningCount;

	    @Column(name = "INSERTED_USER_ID", length = 8)
	    private String insertedUserId;


	    @Column(name = "UPDATED_USER_ID", length = 8)
	    private String updatedUserId;
	    
	    

		public Building(String bldNum, String bldName, String bldStatus, String status, String instNum,
				String secCustLvlCd, String programId, Integer ratedCapacity, String bldSex,
				 String juvenileFlag, String closedCustodyFlag, String bldSpclCondDesc,
				String reasonOfflineCode, Integer currentCapacity, Integer bldCensus, String dormFlag,
				String instSecurityLevel, Integer runningCount) {
			super();
			this.bldNum = bldNum;
			this.bldName = bldName;
			this.bldStatus = bldStatus;
			this.status = status;
			this.instNum = instNum;
			this.secCustLvlCd = secCustLvlCd;
			this.programId = programId;
			this.ratedCapacity = ratedCapacity;
			this.bldSex = bldSex;
			this.juvenileFlag = juvenileFlag;
			this.closedCustodyFlag = closedCustodyFlag;
			this.bldSpclCondDesc = bldSpclCondDesc;
			this.reasonOfflineCode = reasonOfflineCode;
			this.currentCapacity = currentCapacity;
			this.bldCensus = bldCensus;
			this.dormFlag = dormFlag;
			this.instSecurityLevel = instSecurityLevel;
			this.runningCount = runningCount;
		}

		public String getBldNum() {
			return bldNum;
		}

		public void setBldNum(String bldNum) {
			this.bldNum = bldNum;
		}

		public String getBldName() {
			return bldName;
		}

		public void setBldName(String bldName) {
			this.bldName = bldName;
		}

		public String getBldStatus() {
			return bldStatus;
		}

		public void setBldStatus(String bldStatus) {
			this.bldStatus = bldStatus;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getInstNum() {
			return instNum;
		}

		public void setInstNum(String instNum) {
			this.instNum = instNum;
		}

		public String getSecCustLvlCd() {
			return secCustLvlCd;
		}

		public void setSecCustLvlCd(String secCustLvlCd) {
			this.secCustLvlCd = secCustLvlCd;
		}

		public String getProgramId() {
			return programId;
		}

		public void setProgramId(String programId) {
			this.programId = programId;
		}

		public Integer getRatedCapacity() {
			return ratedCapacity;
		}

		public void setRatedCapacity(Integer ratedCapacity) {
			this.ratedCapacity = ratedCapacity;
		}

		public String getBldSex() {
			return bldSex;
		}

		public void setBldSex(String bldSex) {
			this.bldSex = bldSex;
		}

		public String getJuvenileFlag() {
			return juvenileFlag;
		}

		public void setJuvenileFlag(String juvenileFlag) {
			this.juvenileFlag = juvenileFlag;
		}

		public String getClosedCustodyFlag() {
			return closedCustodyFlag;
		}

		public void setClosedCustodyFlag(String closedCustodyFlag) {
			this.closedCustodyFlag = closedCustodyFlag;
		}

		public String getBldSpclCondDesc() {
			return bldSpclCondDesc;
		}

		public void setBldSpclCondDesc(String bldSpclCondDesc) {
			this.bldSpclCondDesc = bldSpclCondDesc;
		}

		public String getReasonOfflineCode() {
			return reasonOfflineCode;
		}

		public void setReasonOfflineCode(String reasonOfflineCode) {
			this.reasonOfflineCode = reasonOfflineCode;
		}

		public Integer getCurrentCapacity() {
			return currentCapacity;
		}

		public void setCurrentCapacity(Integer currentCapacity) {
			this.currentCapacity = currentCapacity;
		}

		public Integer getBldCensus() {
			return bldCensus;
		}

		public void setBldCensus(Integer bldCensus) {
			this.bldCensus = bldCensus;
		}

		public String getDormFlag() {
			return dormFlag;
		}

		public void setDormFlag(String dormFlag) {
			this.dormFlag = dormFlag;
		}

		public String getInstSecurityLevel() {
			return instSecurityLevel;
		}

		public void setInstSecurityLevel(String instSecurityLevel) {
			this.instSecurityLevel = instSecurityLevel;
		}

		public Integer getRunningCount() {
			return runningCount;
		}

		public void setRunningCount(Integer runningCount) {
			this.runningCount = runningCount;
		}

}
