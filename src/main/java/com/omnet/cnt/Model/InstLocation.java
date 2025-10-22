	/**
	 * Document: InstLocation.java
	 * Author: Jamal Abraar
	 * Date Created: 26-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InstLocation {
	
	@Column(name= "INST_NUM")
	private String instNum;
	
	@Id
	@Column(name= "COUNT_LOC_CODE")
	private String countLocCode;
	
	@Column(name= "COUNT_LOC_DESC")
	private String countLocDesc;
	
	@Column(name= "COUNT_LOC_STATUS")
	private String countLocStatus;
	
	@Column(name = "LOCATION_TYPE")
	private String locationType;
	
	@Column(name = "BLD_NUM")
	private String bldNum;
	
	@Column(name = "UNIT_ID")
	private String unitId;
	
	@Column(name = "FLOOR_NUM")
	private String floorNum;
	
	@Column(name = "TIER_NUM")
	private String tierNum;
	
	@Column(name = "CELL_NO")
	private String cellNo;
	
	@Column(name = "BED_NO")
	private String bedNo;

	public String getInstNum() {
		return instNum;
	}

	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	public String getCountLocCode() {
		return countLocCode;
	}

	public void setCountLocCode(String countLocCode) {
		this.countLocCode = countLocCode;
	}

	public String getCountLocDesc() {
		return countLocDesc;
	}

	public void setCountLocDesc(String countLocDesc) {
		this.countLocDesc = countLocDesc;
	}

	public String getCountLocStatus() {
		return countLocStatus;
	}

	public void setCountLocStatus(String countLocStatus) {
		this.countLocStatus = countLocStatus;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
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

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public InstLocation(String instNum, String countLocCode, String countLocDesc, String countLocStatus,
			String locationType, String bldNum, String unitId, String floorNum, String tierNum, String cellNo,
			String bedNo) {
		super();
		this.instNum = instNum;
		this.countLocCode = countLocCode;
		this.countLocDesc = countLocDesc;
		this.countLocStatus = countLocStatus;
		this.locationType = locationType;
		this.bldNum = bldNum;
		this.unitId = unitId;
		this.floorNum = floorNum;
		this.tierNum = tierNum;
		this.cellNo = cellNo;
		this.bedNo = bedNo;
	}

	public InstLocation() {
		super();
	}	
	
}
