package com.omnet.cnt.Model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "current_inst_movements")
public class Currentinstmovement {
	@Id
	
	  @Column(name = "INST_NUM")
    private Long instNum;

    @Column(name = "COMMIT_NO", nullable = false)
    private String commitNo;

    @Column(name = "DATE_TIME_OF_DEP")
    private LocalDateTime dateTimeOfDep;

    @Column(name = "DATE_TIME_OF_ARR")
    private LocalDateTime dateTimeOfArr;

    @Column(name = "COUNT_LOC_CODE_FROM")
    private String countLocCodeFrom;

    @Column(name = "COUNT_LOC_CODE_TO")
    private String countLocCodeTo;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "UNSCHEDULED_FLAG")
    private String unscheduledFlag;

    @Column(name = "ACTIVITY_TYPE_CODE")
    private String activityTypeCode;

    @Column(name = "MOVEMENT_TYPE")
    private String movementType;

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

    @Column(name = "INSERTED_USER_ID")
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME")
    private LocalDateTime insertedDateTime;

    @Column(name = "UPDATED_USER_ID")
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME")
    private LocalDateTime updatedDateTime;

    @Column(name = "TERMINAL")
    private String terminal;

    @Column(name = "SESSIONID")
    private String sessionId;

	public Long getInstNum() {
		return instNum;
	}

	public void setInstNum(Long instNum) {
		this.instNum = instNum;
	}

	public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public LocalDateTime getDateTimeOfDep() {
		return dateTimeOfDep;
	}

	public void setDateTimeOfDep(LocalDateTime dateTimeOfDep) {
		this.dateTimeOfDep = dateTimeOfDep;
	}

	public LocalDateTime getDateTimeOfArr() {
		return dateTimeOfArr;
	}

	public void setDateTimeOfArr(LocalDateTime dateTimeOfArr) {
		this.dateTimeOfArr = dateTimeOfArr;
	}

	public String getCountLocCodeFrom() {
		return countLocCodeFrom;
	}

	public void setCountLocCodeFrom(String countLocCodeFrom) {
		this.countLocCodeFrom = countLocCodeFrom;
	}

	public String getCountLocCodeTo() {
		return countLocCodeTo;
	}

	public void setCountLocCodeTo(String countLocCodeTo) {
		this.countLocCodeTo = countLocCodeTo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUnscheduledFlag() {
		return unscheduledFlag;
	}

	public void setUnscheduledFlag(String unscheduledFlag) {
		this.unscheduledFlag = unscheduledFlag;
	}

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
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

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Currentinstmovement(Long instNum, String commitNo, LocalDateTime dateTimeOfDep, LocalDateTime dateTimeOfArr,
			String countLocCodeFrom, String countLocCodeTo, String comments, String unscheduledFlag,
			String activityTypeCode, String movementType, String bldNum, String unitId, String floorNum, String tierNum,
			String cellNo, String bedNo, String insertedUserId, LocalDateTime insertedDateTime, String updatedUserId,
			LocalDateTime updatedDateTime, String terminal, String sessionId) {
		super();
		this.instNum = instNum;
		this.commitNo = commitNo;
		this.dateTimeOfDep = dateTimeOfDep;
		this.dateTimeOfArr = dateTimeOfArr;
		this.countLocCodeFrom = countLocCodeFrom;
		this.countLocCodeTo = countLocCodeTo;
		this.comments = comments;
		this.unscheduledFlag = unscheduledFlag;
		this.activityTypeCode = activityTypeCode;
		this.movementType = movementType;
		this.bldNum = bldNum;
		this.unitId = unitId;
		this.floorNum = floorNum;
		this.tierNum = tierNum;
		this.cellNo = cellNo;
		this.bedNo = bedNo;
		this.insertedUserId = insertedUserId;
		this.insertedDateTime = insertedDateTime;
		this.updatedUserId = updatedUserId;
		this.updatedDateTime = updatedDateTime;
		this.terminal = terminal;
		this.sessionId = sessionId;
	}

	public Currentinstmovement() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    

}
