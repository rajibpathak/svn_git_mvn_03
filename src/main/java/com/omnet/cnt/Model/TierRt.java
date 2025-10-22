package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "INST_TIER_RT")
public class TierRt {
	
	@Id
	
	   @Column(name = "tier_num")
	    private String tierNum;

	    @Column(name = "tier_desc")
	    private String tierDesc;

	    @Column(name = "tier_status")
	    private String tierStatus;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "inst_num")
	    private String instNum;

	    @Column(name = "tier_sex")
	    private String tierSex;

	    @Column(name = "tier_census")
	    private String tierCensus;

	    @Column(name = "sec_cust_lvl_cd")
	    private String secCustLvlCd;

	    @Column(name = "program_id")
	    private String programId;

	    @Column(name = "rated_capacity")
	    private Integer ratedCapacity;

	    @Column(name = "hanicap_accessible_flag")
	    private Boolean hanicapAccessibleFlag;

	    @Column(name = "juvenile_flag")
	    private Boolean juvenileFlag;

	    @Column(name = "closed_custody_flag")
	    private Boolean closedCustodyFlag;

	    @Column(name = "tier_spcl_cond_desc")
	    private String tierSpclCondDesc;

	    @Column(name = "reason_offline_code")
	    private String reasonOfflineCode;

	    @Column(name = "bld_num")
	    private String bldNum;

	    @Column(name = "unit_id")
	    private String unitId;

	    @Column(name = "floor_num")
	    private String floorNum;

	    @Column(name = "current_capacity")
	    private Integer currentCapacity;

		public String getTierNum() {
			return tierNum;
		}

		public void setTierNum(String tierNum) {
			this.tierNum = tierNum;
		}

		public String getTierDesc() {
			return tierDesc;
		}

		public void setTierDesc(String tierDesc) {
			this.tierDesc = tierDesc;
		}

		public String getTierStatus() {
			return tierStatus;
		}

		public void setTierStatus(String tierStatus) {
			this.tierStatus = tierStatus;
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

		public String getTierSex() {
			return tierSex;
		}

		public void setTierSex(String tierSex) {
			this.tierSex = tierSex;
		}

		public String getTierCensus() {
			return tierCensus;
		}

		public void setTierCensus(String tierCensus) {
			this.tierCensus = tierCensus;
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

		public Boolean getHanicapAccessibleFlag() {
			return hanicapAccessibleFlag;
		}

		public void setHanicapAccessibleFlag(Boolean hanicapAccessibleFlag) {
			this.hanicapAccessibleFlag = hanicapAccessibleFlag;
		}

		public Boolean getJuvenileFlag() {
			return juvenileFlag;
		}

		public void setJuvenileFlag(Boolean juvenileFlag) {
			this.juvenileFlag = juvenileFlag;
		}

		public Boolean getClosedCustodyFlag() {
			return closedCustodyFlag;
		}

		public void setClosedCustodyFlag(Boolean closedCustodyFlag) {
			this.closedCustodyFlag = closedCustodyFlag;
		}

		public String getTierSpclCondDesc() {
			return tierSpclCondDesc;
		}

		public void setTierSpclCondDesc(String tierSpclCondDesc) {
			this.tierSpclCondDesc = tierSpclCondDesc;
		}

		public String getReasonOfflineCode() {
			return reasonOfflineCode;
		}

		public void setReasonOfflineCode(String reasonOfflineCode) {
			this.reasonOfflineCode = reasonOfflineCode;
		}

		public String getBldNum() {
			return bldNum;
		}

		public void setBldNum(String bldNum) {
			this.bldNum = bldNum;
		}

		public String getUnitId() {
			return unitId;
		}

		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}

		public String getFloorNum() {
			return floorNum;
		}

		public void setFloorNum(String floorNum) {
			this.floorNum = floorNum;
		}

		public Integer getCurrentCapacity() {
			return currentCapacity;
		}

		public void setCurrentCapacity(Integer currentCapacity) {
			this.currentCapacity = currentCapacity;
		}

		public TierRt(String tierNum, String tierDesc, String tierStatus, String status, String instNum, String tierSex,
				String tierCensus, String secCustLvlCd, String programId, Integer ratedCapacity,
				Boolean hanicapAccessibleFlag, Boolean juvenileFlag, Boolean closedCustodyFlag, String tierSpclCondDesc,
				String reasonOfflineCode, String bldNum, String unitId, String floorNum, Integer currentCapacity) {
			super();
			this.tierNum = tierNum;
			this.tierDesc = tierDesc;
			this.tierStatus = tierStatus;
			this.status = status;
			this.instNum = instNum;
			this.tierSex = tierSex;
			this.tierCensus = tierCensus;
			this.secCustLvlCd = secCustLvlCd;
			this.programId = programId;
			this.ratedCapacity = ratedCapacity;
			this.hanicapAccessibleFlag = hanicapAccessibleFlag;
			this.juvenileFlag = juvenileFlag;
			this.closedCustodyFlag = closedCustodyFlag;
			this.tierSpclCondDesc = tierSpclCondDesc;
			this.reasonOfflineCode = reasonOfflineCode;
			this.bldNum = bldNum;
			this.unitId = unitId;
			this.floorNum = floorNum;
			this.currentCapacity = currentCapacity;
		}

		public TierRt() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    

}
