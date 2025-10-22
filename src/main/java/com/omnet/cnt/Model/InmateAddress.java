package com.omnet.cnt.Model;

import javax.persistence.*;

@Entity
@Table(name = "INMATE_ADDRESS")
public class InmateAddress {

    @Id
    @Column(name = "commit_no")
    private String commitNo;

    @OneToOne
    @JoinColumn(name = "commit_no", referencedColumnName = "commit_no") 
    private InmateRegistration inmate;

    @Column(name = "adr_street_1")
    private String adrStreet1;

    private String adrStreet2;

    @ManyToOne
    @JoinColumn(name = "COUNTY_CODE", referencedColumnName = "COUNTY_CODE", insertable = false, updatable = false)
    private County county;

    @Column(name = "COUNTY_CODE", length = 6)
    private String countyCd;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "adr_zip_5", length = 5)
    private String adrZip5;
    
    @Column(name = "address_seq_num")
    private String addseqnum;

    // Add this getter to easily access county name
    public String getCountyName() {
        return county != null ? county.getCountyName() : null;
    }

   
    public String getAddseqnum() {
        return addseqnum;
    }

    public void setAddseqnum(String addseqnum) {
        this.addseqnum = addseqnum;
    }

    public String getCommitNo() {
        return commitNo;
    }

    public void setCommitNo(String commitNo) {
        this.commitNo = commitNo;
    }

    public InmateRegistration getInmate() {
        return inmate;
    }

    public void setInmate(InmateRegistration inmate) {
        this.inmate = inmate;
    }

    public String getAdrStreet1() {
        return adrStreet1;
    }

    public void setAdrStreet1(String adrStreet1) {
        this.adrStreet1 = adrStreet1;
    }

    public String getAdrStreet2() {
        return adrStreet2;
    }

    public void setAdrStreet2(String adrStreet2) {
        this.adrStreet2 = adrStreet2;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }

    public String getCountyCd() {
        return countyCd;
    }

    public void setCountyCd(String countyCd) {
        this.countyCd = countyCd;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getAdrZip5() {
        return adrZip5;
    }

    public void setAdrZip5(String adrZip5) {
        this.adrZip5 = adrZip5;
    }
}