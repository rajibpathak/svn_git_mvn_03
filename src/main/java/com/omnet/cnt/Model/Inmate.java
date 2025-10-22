package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INMATE")
public class Inmate {

	@Id
    
	@Column(name = "sbi_mst_sbi_no")
    private String sbiNo;
	
    private String commitNo;
	
    private String inmateStatusCode;
    private String location1;
    private String securityLevel1;
    private String assignedOfficer1;
    private String location2;
    private String securityLevel2;
    private String assignedOfficer2;
    private String commitLname;
    private String commitFname;
    private String commitMname;
    private String commitSname;
    private Date dob; 
    private String sex;
    private String race;
    
    @Column(name= "current_inst_num")
    private String currentInstNum;  
    
	public String getSbiNo() {
		return sbiNo;
	}
	public void setSbiNo(String sbiNo) {
		this.sbiNo = sbiNo;
	}
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public String getInmateStatusCode() {
		return inmateStatusCode;
	}
	public void setInmateStatusCode(String inmateStatusCode) {
		this.inmateStatusCode = inmateStatusCode;
	}
	public String getLocation1() {
		return location1;
	}
	public void setLocation1(String location1) {
		this.location1 = location1;
	}
	public String getSecurityLevel1() {
		return securityLevel1;
	}
	public void setSecurityLevel1(String securityLevel1) {
		this.securityLevel1 = securityLevel1;
	}
	public String getAssignedOfficer1() {
		return assignedOfficer1;
	}
	public void setAssignedOfficer1(String assignedOfficer1) {
		this.assignedOfficer1 = assignedOfficer1;
	}
	public String getLocation2() {
		return location2;
	}
	public void setLocation2(String location2) {
		this.location2 = location2;
	}
	public String getSecurityLevel2() {
		return securityLevel2;
	}
	public void setSecurityLevel2(String securityLevel2) {
		this.securityLevel2 = securityLevel2;
	}
	public String getAssignedOfficer2() {
		return assignedOfficer2;
	}
	public void setAssignedOfficer2(String assignedOfficer2) {
		this.assignedOfficer2 = assignedOfficer2;
	}
	public String getCommitLname() {
		return commitLname;
	}
	public void setCommitLname(String commitLname) {
		this.commitLname = commitLname;
	}
	public String getCommitFname() {
		return commitFname;
	}
	public void setCommitFname(String commitFname) {
		this.commitFname = commitFname;
	}
	public String getCommitMname() {
		return commitMname;
	}
	public void setCommitMname(String commitMname) {
		this.commitMname = commitMname;
	}
	public String getCommitSname() {
		return commitSname;
	}
	public void setCommitSname(String commitSname) {
		this.commitSname = commitSname;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getCurrentInstNum() {
		return currentInstNum;
	}
	public void setCurrentInstNum(String currentInstNum) {
		this.currentInstNum = currentInstNum;
	}
	public Inmate(String sbiNo, String commitNo, String inmateStatusCode, String location1, String securityLevel1,
			String assignedOfficer1, String location2, String securityLevel2, String assignedOfficer2,
			String commitLname, String commitFname, String commitMname, String commitSname, Date dob, String sex,
			String race, String currentInstNum) {
		super();
		this.sbiNo = sbiNo;
		this.commitNo = commitNo;
		this.inmateStatusCode = inmateStatusCode;
		this.location1 = location1;
		this.securityLevel1 = securityLevel1;
		this.assignedOfficer1 = assignedOfficer1;
		this.location2 = location2;
		this.securityLevel2 = securityLevel2;
		this.assignedOfficer2 = assignedOfficer2;
		this.commitLname = commitLname;
		this.commitFname = commitFname;
		this.commitMname = commitMname;
		this.commitSname = commitSname;
		this.dob = dob;
		this.sex = sex;
		this.race = race;
		this.currentInstNum = currentInstNum;
	}
	public Inmate() {
		super();
	}
	
	
}
