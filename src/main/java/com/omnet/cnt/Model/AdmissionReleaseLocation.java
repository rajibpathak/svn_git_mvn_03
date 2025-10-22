package com.omnet.cnt.Model;

import javax.persistence.*;

@Entity
@Table(name = "INST_LOCATION")
public class AdmissionReleaseLocation {

	@Id
	@Column(name= "INST_NUM")
	private String instNum;

    @Column(name = "COUNT_LOC_DESC")
    private String locationDescription;


    public String getInstNum() {
		return instNum;
	}

	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
