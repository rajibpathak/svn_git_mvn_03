package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity

public class OmnetDesktop {

	
	private String screen_name;
	@Id
	private String id;

	private String sbi_no;
	private String INMATE_FULL_NAME;
	private String access_date_time;
	@Lob
	private byte[] mug_shot;
	private String  NAME_PREFIX;
	private String  ACCESS_TIME;
	private String  SCREEN_URL;
	
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSCREEN_URL() {
		return SCREEN_URL;
	}
	public void setSCREEN_URL(String sCREEN_URL) {
		SCREEN_URL = sCREEN_URL;
	}
	public String getACCESS_TIME() {
		return ACCESS_TIME;
	}
	public void setACCESS_TIME(String aCCESS_TIME) {
		ACCESS_TIME = aCCESS_TIME;
	}
	public String getNAME_PREFIX() {
		return NAME_PREFIX;
	}
	public void setNAME_PREFIX(String nAME_PREFIX) {
		NAME_PREFIX = nAME_PREFIX;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public byte[] getMug_shot() {
		return mug_shot;
	}
	public void setMug_shot(byte[] mug_shot) {
		this.mug_shot = mug_shot;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getSbi_no() {
		return sbi_no;
	}
	public void setSbi_no(String sbi_no) {
		this.sbi_no = sbi_no;
	}


	public String getINMATE_FULL_NAME() {
		return INMATE_FULL_NAME;
	}
	public void setINMATE_FULL_NAME(String iNMATE_FULL_NAME) {
		INMATE_FULL_NAME = iNMATE_FULL_NAME;
	}
	public String getAccess_date_time() {
		return access_date_time;
	}
	public void setAccess_date_time(String access_date_time) {
		this.access_date_time = access_date_time;
	}



	
	
}
