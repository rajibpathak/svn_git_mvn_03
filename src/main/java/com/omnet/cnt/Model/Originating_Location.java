package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Originating_Location {

	@Id
    private String COUNT_LOC_CODE;
	private String COUNT_LOC_DESC;
	
	public String getCOUNT_LOC_CODE() {
		return COUNT_LOC_CODE;
	}
	public void setCOUNT_LOC_CODE(String cOUNT_LOC_CODE) {
		COUNT_LOC_CODE = cOUNT_LOC_CODE;
	}
	public String getCOUNT_LOC_DESC() {
		return COUNT_LOC_DESC;
	}
	public void setCOUNT_LOC_DESC(String cOUNT_LOC_DESC) {
		COUNT_LOC_DESC = cOUNT_LOC_DESC;
	}
	
	
	
}
