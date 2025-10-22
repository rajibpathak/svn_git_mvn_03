package com.omnet.cnt.Model;

import javax.persistence.*;

@Entity
@Table(name = "COUNTY_RT")
public class County {

	@Id
    @Column(name = "COUNTY_CODE", length = 6)
    private String countyCd;
    
    @Column(name = "COUNTY_NAME")
    private String countyName;
    
    @Column(name = "STATE_CODE")
    private String stateCode;
    
    @Column(name = "ZIP_5")
    private String zip5;
    
    
    public String getCountyCd() {
        return countyCd;
    }

    public void setCountyCd(String countyCd) {
        this.countyCd = countyCd;
    }
    
	public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip5() {
        return zip5;
    }

    public void setZip5(String zip5) {
        this.zip5 = zip5;
    }
}