package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Inst_Cell_rt")
public class CellRt {
	
	@Id
	
	@Column(name = "cell_no")
    private String cellNo;

    @Column(name = "cell_desc")
    private String cellDesc;

    @Column(name = "cell_status")
    private String cellStatus;

    @Column(name = "status")
    private String status;

    @Column(name = "inst_num")
    private String instNum;

    @Column(name = "cell_sex")
    private String cellSex;

    @Column(name = "cell_census")
    private String cellCensus;

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

    @Column(name = "cell_spcl_cond_desc")
    private String cellSpclCondDesc;

    @Column(name = "reason_offline_code")
    private String reasonOfflineCode;

    @Column(name = "bld_num")
    private String bldNum;

    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "floor_num")
    private String floorNum;

    @Column(name = "tier_num")
    private String tierNum;

    @Column(name = "current_capacity")
    private Integer currentCapacity;

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getCellDesc() {
		return cellDesc;
	}

	public void setCellDesc(String cellDesc) {
		this.cellDesc = cellDesc;
	}

	public String getCellStatus() {
		return cellStatus;
	}

	public void setCellStatus(String cellStatus) {
		this.cellStatus = cellStatus;
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

	public String getCellSex() {
		return cellSex;
	}

	public void setCellSex(String cellSex) {
		this.cellSex = cellSex;
	}

	public String getCellCensus() {
		return cellCensus;
	}

	public void setCellCensus(String cellCensus) {
		this.cellCensus = cellCensus;
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

	public String getCellSpclCondDesc() {
		return cellSpclCondDesc;
	}

	public void setCellSpclCondDesc(String cellSpclCondDesc) {
		this.cellSpclCondDesc = cellSpclCondDesc;
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

	public String getTierNum() {
		return tierNum;
	}

	public void setTierNum(String tierNum) {
		this.tierNum = tierNum;
	}

	public Integer getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(Integer currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public CellRt(String cellNo, String cellDesc, String cellStatus, String status, String instNum, String cellSex,
			String cellCensus, String secCustLvlCd, String programId, Integer ratedCapacity,
			Boolean hanicapAccessibleFlag, Boolean juvenileFlag, Boolean closedCustodyFlag, String cellSpclCondDesc,
			String reasonOfflineCode, String bldNum, String unitId, String floorNum, String tierNum,
			Integer currentCapacity) {
		super();
		this.cellNo = cellNo;
		this.cellDesc = cellDesc;
		this.cellStatus = cellStatus;
		this.status = status;
		this.instNum = instNum;
		this.cellSex = cellSex;
		this.cellCensus = cellCensus;
		this.secCustLvlCd = secCustLvlCd;
		this.programId = programId;
		this.ratedCapacity = ratedCapacity;
		this.hanicapAccessibleFlag = hanicapAccessibleFlag;
		this.juvenileFlag = juvenileFlag;
		this.closedCustodyFlag = closedCustodyFlag;
		this.cellSpclCondDesc = cellSpclCondDesc;
		this.reasonOfflineCode = reasonOfflineCode;
		this.bldNum = bldNum;
		this.unitId = unitId;
		this.floorNum = floorNum;
		this.tierNum = tierNum;
		this.currentCapacity = currentCapacity;
	}

	public CellRt() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
