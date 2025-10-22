package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MoveMentLogActivity_Type {

	@Id
	private String ACTIVITY_TYPE_CODE;
	private String ACTIVITY_TYPE_DESC;
	public String getACTIVITY_TYPE_CODE() {
		return ACTIVITY_TYPE_CODE;
	}
	public void setACTIVITY_TYPE_CODE(String aCTIVITY_TYPE_CODE) {
		ACTIVITY_TYPE_CODE = aCTIVITY_TYPE_CODE;
	}
	public String getACTIVITY_TYPE_DESC() {
		return ACTIVITY_TYPE_DESC;
	}
	public void setACTIVITY_TYPE_DESC(String aCTIVITY_TYPE_DESC) {
		ACTIVITY_TYPE_DESC = aCTIVITY_TYPE_DESC;
	}
	
}
