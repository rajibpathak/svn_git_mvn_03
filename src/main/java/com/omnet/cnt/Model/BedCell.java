package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BedCell {

    @Id
    @Column(name = "sbi_no")
    private String sbiNo;

    @Column(name = "commit_no")
    private String commitNo;

    @Column(name = "cell_desc")
    private String cellDesc;

    @Column(name = "bed_desc")
    private String bedDesc;

    @Column(name = "unit_desc")
    private String unitDesc;

    @Column(name = "bed_no")
    private String bedNo;

    @Column(name = "unit_id")
    private String unitId;

    @Column(name = "cell_no")
    private String cellNo;

    @Column(name = "bed_status")
    private String bedStatus;

    @Column(name = "covid_flag")
    private String covidFlag;

    // Not stored in DB, but computed after fetching bedNo
    private String bedSex;

    // --- Getters and Setters ---
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

    public String getCellDesc() {
        return cellDesc;
    }

    public void setCellDesc(String cellDesc) {
        this.cellDesc = cellDesc;
    }

    public String getBedDesc() {
        return bedDesc;
    }

    public void setBedDesc(String bedDesc) {
        this.bedDesc = bedDesc;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public String getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

    public String getCovidFlag() {
        return covidFlag;
    }

    public void setCovidFlag(String covidFlag) {
        this.covidFlag = covidFlag;
    }

    public String getBedSex() {
        return bedSex;
    }

    public void setBedSex(String bedSex) {
        this.bedSex = bedSex;
    }

    // --- Constructors ---
    public BedCell() {
    }

    public BedCell(String commitNo, String cellDesc, String bedDesc, String unitDesc,
                   String bedNo, String unitId, String cellNo, String bedStatus, String covidFlag) {
        this.commitNo = commitNo;
        this.cellDesc = cellDesc;
        this.bedDesc = bedDesc;
        this.unitDesc = unitDesc;
        this.bedNo = bedNo;
        this.unitId = unitId;
        this.cellNo = cellNo;
        this.bedStatus = bedStatus;
        this.covidFlag = covidFlag;
    }

    @Override
    public String toString() {
        return "BedCell{" +
                "sbiNo='" + sbiNo + '\'' +
                ", commitNo='" + commitNo + '\'' +
                ", cellDesc='" + cellDesc + '\'' +
                ", bedDesc='" + bedDesc + '\'' +
                ", unitDesc='" + unitDesc + '\'' +
                ", bedNo='" + bedNo + '\'' +
                ", unitId='" + unitId + '\'' +
                ", cellNo='" + cellNo + '\'' +
                ", bedStatus='" + bedStatus + '\'' +
                ", covidFlag='" + covidFlag + '\'' +
                ", bedSex='" + bedSex + '\'' +
                '}';
    }
}
