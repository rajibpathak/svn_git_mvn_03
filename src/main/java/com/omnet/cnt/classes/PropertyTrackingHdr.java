package com.omnet.cnt.classes;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
public class PropertyTrackingHdr {
	
	//@Id
	private String inmateCommitNo;
    private String institutionInstNum;
    private String usersUserId;
    private String userFirstName;
    private String userLastName;
    private String userMidName;
    private String userSuffixName;
    private String usersUserIdReturnBy;
    private Integer propTrkSeqNo;
    private double propTrkAmt;
    private String propRetnDt;
    private String propTrkDt;
    private String propTrkTp;
    private String propBoxNum;
    
    public String getInmateCommitNo() {
        return inmateCommitNo;
    }

    public void setInmateCommitNo(String inmateCommitNo) {
        this.inmateCommitNo = inmateCommitNo;
    }

    public String getInstitutionInstNum() {
        return institutionInstNum;
    }

    public void setInstitutionInstNum(String institutionInstNum) {
        this.institutionInstNum = institutionInstNum;
    }

    public String getUsersUserId() {
        return usersUserId;
    }

    public void setUsersUserId(String usersUserId) {
        this.usersUserId = usersUserId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMidName() {
        return userMidName;
    }

    public void setUserMidName(String userMidName) {
        this.userMidName = userMidName;
    }

    public String getUserSuffixName() {
        return userSuffixName;
    }

    public void setUserSuffixName(String userSuffixName) {
        this.userSuffixName = userSuffixName;
    }

    public String getUsersUserIdReturnBy() {
        return usersUserIdReturnBy;
    }

    public void setUsersUserIdReturnBy(String usersUserIdReturnBy) {
        this.usersUserIdReturnBy = usersUserIdReturnBy;
    }

    public Integer getPropTrkSeqNo() {
        return propTrkSeqNo;
    }

    public void setPropTrkSeqNo(Integer propTrkSeqNo) {
        this.propTrkSeqNo = propTrkSeqNo;
    }

    public double getPropTrkAmt() {
        return propTrkAmt;
    }

    public void setPropTrkAmt(double propTrkAmt) {
        this.propTrkAmt = propTrkAmt;
    }

    public String getPropRetnDt() {
        return propRetnDt;
    }

    public void setPropRetnDt(String propRetnDt) {
        this.propRetnDt = propRetnDt;
    }

    public String getPropTrkDt() {
        return propTrkDt;
    }

    public void setPropTrkDt(String propTrkDt) {
        this.propTrkDt = propTrkDt;
    }

    public String getPropTrkTp() {
        return propTrkTp;
    }

    public void setPropTrkTp(String propTrkTp) {
        this.propTrkTp = propTrkTp;
    }

    public String getPropBoxNum() {
        return propBoxNum;
    }

    public void setPropBoxNum(String propBoxNum) {
        this.propBoxNum = propBoxNum;
    }
}
