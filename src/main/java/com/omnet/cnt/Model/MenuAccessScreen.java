package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MenuAccessScreen {

	@Id
	private String S_NO;
	private String SCREEN_NAME;
	private String SUB_MENU_CODE;
	private String MENU_CODE;
	private String INSERT_PRIV_FLAG;
	private String UPDATE_PRIV_FLAG;
	private String VIEW_PRIV_FLAG;
	private String SCREEN_URL;
	public String getS_NO() {
		return S_NO;
	}
	public void setS_NO(String s_NO) {
		S_NO = s_NO;
	}
	public String getSCREEN_NAME() {
		return SCREEN_NAME;
	}
	public void setSCREEN_NAME(String sCREEN_NAME) {
		SCREEN_NAME = sCREEN_NAME;
	}
	public String getSUB_MENU_CODE() {
		return SUB_MENU_CODE;
	}
	public void setSUB_MENU_CODE(String sUB_MENU_CODE) {
		SUB_MENU_CODE = sUB_MENU_CODE;
	}
	public String getMENU_CODE() {
		return MENU_CODE;
	}
	public void setMENU_CODE(String mENU_CODE) {
		MENU_CODE = mENU_CODE;
	}
	public String getINSERT_PRIV_FLAG() {
		return INSERT_PRIV_FLAG;
	}
	public void setINSERT_PRIV_FLAG(String iNSERT_PRIV_FLAG) {
		INSERT_PRIV_FLAG = iNSERT_PRIV_FLAG;
	}
	public String getUPDATE_PRIV_FLAG() {
		return UPDATE_PRIV_FLAG;
	}
	public void setUPDATE_PRIV_FLAG(String uPDATE_PRIV_FLAG) {
		UPDATE_PRIV_FLAG = uPDATE_PRIV_FLAG;
	}
	public String getVIEW_PRIV_FLAG() {
		return VIEW_PRIV_FLAG;
	}
	public void setVIEW_PRIV_FLAG(String vIEW_PRIV_FLAG) {
		VIEW_PRIV_FLAG = vIEW_PRIV_FLAG;
	}
	public String getSCREEN_URL() {
		return SCREEN_URL;
	}
	public void setSCREEN_URL(String sCREEN_URL) {
		SCREEN_URL = sCREEN_URL;
	}
	
	
	
	
}
