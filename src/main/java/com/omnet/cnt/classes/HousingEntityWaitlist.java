/**
	 * Document: HousingEntityWaitlist.java
	 * Author: Jamal Abraar
	 * Date Created: 08-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HousingEntityWaitlist {
	private String commitNo;
	private String sbiNo;
	private String lastName;
	private String firstName;
	private String middleName;
	private String suffix;
	private String sex;
	private Integer docWLSeqNum;
	private Timestamp datePlacedOnwait;
	private String timeAddedToWait;
	private String movementScheduledFlag;
	private String institution;
	private String building;
	private String unit;
	private String floor;
	private String tier;
	private String cell;
	private String bed;
	private String instituionNum;
	private String buildingNum;
	private String unitNum;
	private String floorNum;
	private String tierNum;
	private String cellNum;
	private String bedNum;          
	
    @JsonProperty("commitNo")
    public String getCommitNo() {
        return commitNo;
    }

    @JsonProperty("commitNo")
    public void setCommitNo(String commitNo) {
        this.commitNo = commitNo;
    }

    @JsonProperty("sbiNo")
    public String getSbiNo() {
        return sbiNo;
    }

    @JsonProperty("sbiNo")
    public void setSbiNo(String sbiNo) {
        this.sbiNo = sbiNo;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("suffix")
    public String getSuffix() {
        return suffix;
    }

    @JsonProperty("suffix")
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @JsonProperty("sex")
    public String getSex() {
        return sex;
    }

    @JsonProperty("sex")
    public void setSex(String sex) {
        this.sex = sex;
    }

    @JsonProperty("docWLSeqNum")
    public Integer getDocWLSeqNum() {
        return docWLSeqNum;
    }

    @JsonProperty("docWLSeqNum")
    public void setDocWLSeqNum(Integer docWLSeqNum) {
        this.docWLSeqNum = docWLSeqNum;
    }

    @JsonProperty("datePlacedOnwait")
    public Timestamp getDatePlacedOnwait() {
        return datePlacedOnwait;
    }

    @JsonProperty("datePlacedOnwait")
    public void setDatePlacedOnwait(Timestamp datePlacedOnwait) {
        this.datePlacedOnwait = datePlacedOnwait;
    }

    @JsonProperty("timeAddedToWait")
    public String getTimeAddedToWait() {
        return timeAddedToWait;
    }

    @JsonProperty("timeAddedToWait")
    public void setTimeAddedToWait(String timeAddedToWait) {
        this.timeAddedToWait = timeAddedToWait;
    }

    @JsonProperty("movementScheduledFlag")
    public String getMovementScheduledFlag() {
        return movementScheduledFlag;
    }

    @JsonProperty("movementScheduledFlag")
    public void setMovementScheduledFlag(String movementScheduledFlag) {
        this.movementScheduledFlag = movementScheduledFlag;
    }

    @JsonProperty("institution")
    public String getInstitution() {
        return institution;
    }

    @JsonProperty("institution")
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @JsonProperty("building")
    public String getBuilding() {
        return building;
    }

    @JsonProperty("building")
    public void setBuilding(String building) {
        this.building = building;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("floor")
    public String getFloor() {
        return floor;
    }

    @JsonProperty("floor")
    public void setFloor(String floor) {
        this.floor = floor;
    }

    @JsonProperty("tier")
    public String getTier() {
        return tier;
    }

    @JsonProperty("tier")
    public void setTier(String tier) {
        this.tier = tier;
    }

    @JsonProperty("cell")
    public String getCell() {
        return cell;
    }

    @JsonProperty("cell")
    public void setCell(String cell) {
        this.cell = cell;
    }

    @JsonProperty("bed")
    public String getBed() {
        return bed;
    }

    @JsonProperty("bed")
    public void setBed(String bed) {
        this.bed = bed;
    }

    @JsonProperty("instituionNum")
    public String getInstituionNum() {
        return instituionNum;
    }

    @JsonProperty("instituionNum")
    public void setInstituionNum(String instituionNum) {
        this.instituionNum = instituionNum;
    }

    @JsonProperty("buildingNum")
    public String getBuildingNum() {
        return buildingNum;
    }

    @JsonProperty("buildingNum")
    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    @JsonProperty("unitNum")
    public String getUnitNum() {
        return unitNum;
    }

    @JsonProperty("unitNum")
    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    @JsonProperty("floorNum")
    public String getFloorNum() {
        return floorNum;
    }

    @JsonProperty("floorNum")
    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    @JsonProperty("tierNum")
    public String getTierNum() {
        return tierNum;
    }

    @JsonProperty("tierNum")
    public void setTierNum(String tierNum) {
        this.tierNum = tierNum;
    }

    @JsonProperty("cellNum")
    public String getCellNum() {
        return cellNum;
    }

    @JsonProperty("cellNum")
    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }

    @JsonProperty("bedNum")
    public String getBedNum() {
        return bedNum;
    }

    @JsonProperty("bedNum")
    public void setBedNum(String bedNum) {
        this.bedNum = bedNum;
    }
    
    @JsonInclude(JsonInclude.Include.ALWAYS)
	public static HousingEntityWaitlist housingEntityWaitList(Map<String, Object> row) {
	    HousingEntityWaitlist housingEntityWaitlistData = new HousingEntityWaitlist();
	    housingEntityWaitlistData.setCommitNo((String) row.get("COMMIT_NO"));
	    housingEntityWaitlistData.setSbiNo((String) row.get("SBI_MST_SBI_NO"));
	    housingEntityWaitlistData.setLastName((String) row.get("COMMIT_LNAME"));
	    housingEntityWaitlistData.setFirstName((String) row.get("COMMIT_FNAME"));
	    housingEntityWaitlistData.setMiddleName((String) row.get("COMMIT_MNAME"));
	    housingEntityWaitlistData.setSuffix((String) row.get("COMMIT_SNAME"));
	    housingEntityWaitlistData.setSex((String) row.get("SEX"));
	    
	    BigDecimal docWLSeqNum = (BigDecimal) row.get("DOC_WL_SEQ_NUM");
	    housingEntityWaitlistData.setDocWLSeqNum(docWLSeqNum != null ? docWLSeqNum.intValue() : null);
	    
	    housingEntityWaitlistData.setDatePlacedOnwait((Timestamp) row.get("DATE_PLACEDONWAIT"));
	    housingEntityWaitlistData.setTimeAddedToWait((String) row.get("TIME_ADDED_TO_WAIT_LIST"));
	    housingEntityWaitlistData.setMovementScheduledFlag((String) row.get("MOVEMENT_SCHEDULED_FLAG"));
	    
	    return housingEntityWaitlistData;
	}
	
    @JsonInclude(JsonInclude.Include.ALWAYS)
	public static HousingEntityWaitlist receivingOffenderQuery(Map<String, Object> row) {
	    HousingEntityWaitlist receivingOffenderQueryData = new HousingEntityWaitlist();
	    receivingOffenderQueryData.setCommitNo((String) row.get("COMMIT_NO"));
	    receivingOffenderQueryData.setInstitution((String) row.get("INST_NAME"));
	    receivingOffenderQueryData.setBuilding((String) row.get("BLD_NAME"));
	    receivingOffenderQueryData.setUnit((String) row.get("UNIT_DESC"));
	    receivingOffenderQueryData.setFloor((String) row.get("FLOOR_DESC"));
	    receivingOffenderQueryData.setTier((String) row.get("TIER_DESC"));
	    receivingOffenderQueryData.setCell((String) row.get("CELL_DESC"));
	    receivingOffenderQueryData.setBed((String) row.get("BED_DESC"));
	    receivingOffenderQueryData.setInstituionNum((String) row.get("WAITLISTED_INST_NUM"));	    	    
	    receivingOffenderQueryData.setBuildingNum((String) row.get("BLD_NUM"));
	    receivingOffenderQueryData.setUnitNum((String) row.get("UNIT_ID"));
	    receivingOffenderQueryData.setFloorNum((String) row.get("FLOOR_NUM"));
	    receivingOffenderQueryData.setTierNum((String) row.get("TIER_NUM"));
	    receivingOffenderQueryData.setCellNum((String) row.get("CELL_NO"));
	    receivingOffenderQueryData.setBedNum((String) row.get("BED_NO"));
	    
	    return receivingOffenderQueryData;
	}
    
    @JsonInclude(JsonInclude.Include.ALWAYS)
	public static HousingEntityWaitlist currentOffenderQuery(Map<String, Object> row) {
	    HousingEntityWaitlist currentOffenderQueryData = new HousingEntityWaitlist();
	    currentOffenderQueryData.setCommitNo((String) row.get("COMMIT_NO"));
	    currentOffenderQueryData.setInstitution((String) row.get("INST_NAME"));
	    currentOffenderQueryData.setBuilding((String) row.get("BLD_NAME"));
	    currentOffenderQueryData.setUnit((String) row.get("UNIT_DESC"));
	    currentOffenderQueryData.setFloor((String) row.get("FLOOR_DESC"));
	    currentOffenderQueryData.setFloor((String) row.get("TIER_DESC"));
	    currentOffenderQueryData.setCell((String) row.get("CELL_DESC"));
	    currentOffenderQueryData.setBed((String) row.get("BED_DESC"));
	    
	    return currentOffenderQueryData;
	}
}
