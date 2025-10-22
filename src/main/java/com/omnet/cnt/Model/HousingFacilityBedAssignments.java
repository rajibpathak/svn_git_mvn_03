package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HousingFacilityBedAssignments {
	
	@Id
	private String INST_NUM;
	private String INST_NAME;
	
	public String getINST_NUM() {
		return INST_NUM;
	}
	public void setINST_NUM(String iNST_NUM) {
		INST_NUM = iNST_NUM;
	}
	public String getINST_NAME() {
		return INST_NAME;
	}
	public void setINST_NAME(String iNST_NAME) {
		INST_NAME = iNST_NAME;
	}
	
	
	

}
