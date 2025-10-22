package com.omnet.cnt.Model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Inst_Unit_rt")
public class InstUnitRt {
	
	// Constructors
    public InstUnitRt() {
    }
	 @Id

	 @Column(name = "unit_id")
	 private String unitId;

	 @Column(name = "unit_desc")
	 private String unitDesc;

	 @Column(name = "unit_status")
	 private String unitStatus;

	 @Column(name = "status")
	 private String status;

	 @Column(name = "inst_num")
	 private String instNum;

	 @Column(name = "unit_sex")
	 private String unitSex;

	 @Column(name = "unit_census")
	 private Integer unitCensus;

	 @Column(name = "unit_order_num")
	 private Integer unitOrderNum;

	 @Column(name = "security_level_code")
	 private String securityLevelCode;

	 @Column(name = "housing_type_code")
	 private String housingTypeCode;

	 @Column(name = "sec_cust_lvl_cd")
	 private String secCustLvlCd;

	 @Column(name = "program_id")
	 private String programId;

	 @Column(name = "rated_capacity")
	 private Integer ratedCapacity;

	 @Column(name = "bld_sex")
	 private String bldSex;

	 @Column(name = "handicap_accessible_flag")
	 private String handicapAccessibleFlag;

	 @Column(name = "juvenile_flag")
	 private String juvenileFlag;

	 @Column(name = "closed_custody_flag")
	 private String closedCustodyFlag;

	 @Column(name = "bld_spcl_cond_desc")
	 private String bldSpclCondDesc;

	 @Column(name = "reason_offline_code")
	 private String reasonOfflineCode;

	 @Column(name = "bld_num")
	 private String bldNum;

	 @Column(name = "current_capacity")
	 private Integer currentCapacity;

	    
	    
		public InstUnitRt(String unitId, String unitDesc, String unitStatus, String status, String instNum, String unitSex,
				Integer unitCensus, Integer unitOrderNum, String securityLevelCode, String housingTypeCode,
				String secCustLvlCd, String programId, Integer ratedCapacity, String bldSex,
				String handicapAccessibleFlag, String juvenileFlag, String closedCustodyFlag, String bldSpclCondDesc,
				String reasonOfflineCode, String bldNum, Integer currentCapacity) {
			super();
			this.unitId = unitId;
			this.unitDesc = unitDesc;
			this.unitStatus = unitStatus;
			this.status = status;
			this.instNum = instNum;
			this.unitSex = unitSex;
			this.unitCensus = unitCensus;
			this.unitOrderNum = unitOrderNum;
			this.securityLevelCode = securityLevelCode;
			this.housingTypeCode = housingTypeCode;
			this.secCustLvlCd = secCustLvlCd;
			this.programId = programId;
			this.ratedCapacity = ratedCapacity;
			this.bldSex = bldSex;
			this.handicapAccessibleFlag = handicapAccessibleFlag;
			this.juvenileFlag = juvenileFlag;
			this.closedCustodyFlag = closedCustodyFlag;
			this.bldSpclCondDesc = bldSpclCondDesc;
			this.reasonOfflineCode = reasonOfflineCode;
			this.bldNum = bldNum;
			this.currentCapacity = currentCapacity;
		}
		public String getUnitId() {
			return unitId;
		}
		public void setUnitId(String unitId) {
			this.unitId = unitId;
		}
		public String getUnitDesc() {
			return unitDesc;
		}
		public void setUnitDesc(String unitDesc) {
			this.unitDesc = unitDesc;
		}
		public String getUnitStatus() {
			return unitStatus;
		}
		public void setUnitStatus(String unitStatus) {
			this.unitStatus = unitStatus;
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
		public String getUnitSex() {
			return unitSex;
		}
		public void setUnitSex(String unitSex) {
			this.unitSex = unitSex;
		}
		public Integer getUnitCensus() {
			return unitCensus;
		}
		public void setUnitCensus(Integer unitCensus) {
			this.unitCensus = unitCensus;
		}
		public Integer getUnitOrderNum() {
			return unitOrderNum;
		}
		public void setUnitOrderNum(Integer unitOrderNum) {
			this.unitOrderNum = unitOrderNum;
		}
		public String getSecurityLevelCode() {
			return securityLevelCode;
		}
		public void setSecurityLevelCode(String securityLevelCode) {
			this.securityLevelCode = securityLevelCode;
		}
		public String getHousingTypeCode() {
			return housingTypeCode;
		}
		public void setHousingTypeCode(String housingTypeCode) {
			this.housingTypeCode = housingTypeCode;
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
		public String getHandicapAccessibleFlag() {
			return handicapAccessibleFlag;
		}
		public void setHandicapAccessibleFlag(String handicapAccessibleFlag) {
			this.handicapAccessibleFlag = handicapAccessibleFlag;
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
		public String getBldNum() {
			return bldNum;
		}
		public void setBldNum(String bldNum) {
			this.bldNum = bldNum;
		}
		public Integer getCurrentCapacity() {
			return currentCapacity;
		}
		public void setCurrentCapacity(Integer currentCapacity) {
			this.currentCapacity = currentCapacity;
		}

}
