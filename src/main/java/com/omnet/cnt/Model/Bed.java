package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bed {
	
	@Id
	private String Bed_NO;
	private String BED_DESC;
	
	public String getBed_NO() {
		return Bed_NO;
	}
	public void setBed_NO(String bed_NO) {
		Bed_NO = bed_NO;
	}
	public String getBED_DESC() {
		return BED_DESC;
	}
	public void setBED_DESC(String bED_DESC) {
		BED_DESC = bED_DESC;
	}
	
	

}
