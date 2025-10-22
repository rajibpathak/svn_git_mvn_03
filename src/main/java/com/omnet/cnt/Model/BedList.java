package com.omnet.cnt.Model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BedList {
	
	@Id
	private String CommitNo;
	private String BedNo;
	private String BedDescription;
	private String BedType;
	private String BedStatus;
	private String BedStat;
	private String SBINo;
	private String CommitLastName;
	private String CommitFirstName;
	private String CommitMiddleName;
	private String CommitSuffixName;
	private Timestamp AvailableDate;
	private String AvailableTime;
	
	
	public String getCommitNo() {
		return CommitNo;
	}
	public void setCommitNo(String commitNo) {
		CommitNo = commitNo;
	}
	public String getBedNo() {
		return BedNo;
	}
	public void setBedNo(String bedNo) {
		BedNo = bedNo;
	}
	public String getBedDescription() {
		return BedDescription;
	}
	public void setBedDescription(String bedDescription) {
		BedDescription = bedDescription;
	}
	public String getBedType() {
		return BedType;
	}
	public void setBedType(String bedType) {
		BedType = bedType;
	}
	public String getBedStatus() {
		return BedStatus;
	}
	public void setBedStatus(String bedStatus) {
		BedStatus = bedStatus;
	}
	public String getBedStat() {
		return BedStat;
	}
	public void setBedStat(String bedStat) {
		BedStat = bedStat;
	}
	public String getSBINo() {
		return SBINo;
	}
	public void setSBINo(String sBINo) {
		SBINo = sBINo;
	}
	public String getCommitLastName() {
		return CommitLastName;
	}
	public void setCommitLastName(String commitLastName) {
		CommitLastName = commitLastName;
	}
	public String getCommitFirstName() {
		return CommitFirstName;
	}
	public void setCommitFirstName(String commitFirstName) {
		CommitFirstName = commitFirstName;
	}
	public String getCommitMiddleName() {
		return CommitMiddleName;
	}
	public void setCommitMiddleName(String commitMiddleName) {
		CommitMiddleName = commitMiddleName;
	}
	public String getCommitSuffixName() {
		return CommitSuffixName;
	}
	public void setCommitSuffixName(String commitSuffixName) {
		CommitSuffixName = commitSuffixName;
	}
	public Timestamp getAvailableDate() {
		return AvailableDate;
	}
	public void setAvailableDate(Timestamp availableDate) {
		AvailableDate = availableDate;
	}
	public String getAvailableTime() {
		return AvailableTime;
	}
	public void setAvailableTime(String availableTime) {
		AvailableTime = availableTime;
	}

	

}
