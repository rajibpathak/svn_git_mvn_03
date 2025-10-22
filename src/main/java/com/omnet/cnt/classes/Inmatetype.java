package com.omnet.cnt.classes;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "inmate_tp_mst")
public class Inmatetype {
	
	@Id
	private String getSbiMstSbiNo;
	private  String getInmateFullName;
	private  String getCommitNo;
	private   String getCurrentLocation;
	private   String getOffenderType;
	
	
	
	public Inmatetype() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getGetSbiMstSbiNo() {
		return getSbiMstSbiNo;
	}
	public void setGetSbiMstSbiNo(String getSbiMstSbiNo) {
		this.getSbiMstSbiNo = getSbiMstSbiNo;
	}
	public String getGetInmateFullName() {
		return getInmateFullName;
	}
	public void setGetInmateFullName(String getInmateFullName) {
		this.getInmateFullName = getInmateFullName;
	}
	public String getGetCommitNo() {
		return getCommitNo;
	}
	public void setGetCommitNo(String getCommitNo) {
		this.getCommitNo = getCommitNo;
	}
	public String getGetCurrentLocation() {
		return getCurrentLocation;
	}
	public void setGetCurrentLocation(String getCurrentLocation) {
		this.getCurrentLocation = getCurrentLocation;
	}
	public String getGetOffenderType() {
		return getOffenderType;
	}
	public void setGetOffenderType(String getOffenderType) {
		this.getOffenderType = getOffenderType;
	}
	@Override
	public String toString() {
		return "Inmatetype [getSbiMstSbiNo=" + getSbiMstSbiNo + ", getInmateFullName=" + getInmateFullName
				+ ", getCommitNo=" + getCommitNo + ", getCurrentLocation=" + getCurrentLocation + ", getOffenderType="
				+ getOffenderType + "]";
	}
	
	

}
