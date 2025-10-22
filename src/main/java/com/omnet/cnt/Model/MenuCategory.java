package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OMNET_MENU_CATEGORIES")
public class MenuCategory {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private String CATEGORY_CODE;
	private String MENU_CODE;
	private String CATEGORY_NAME;
	
	public MenuCategory(String cATEGORY_CODE, String mENU_CODE, String cATEGORY_NAME) {
		super();
		CATEGORY_CODE = cATEGORY_CODE;
		MENU_CODE = mENU_CODE;
		CATEGORY_NAME = cATEGORY_NAME;
	}
	
	public MenuCategory() {
		
	}

	public String getCATEGORY_CODE() {
		return CATEGORY_CODE;
	}

	public void setCATEGORY_CODE(String cATEGORY_CODE) {
		CATEGORY_CODE = cATEGORY_CODE;
	}

	public String getCategory_MENUCODE() {
		return MENU_CODE;
	}

	public void setMENU_CODE(String mENU_CODE) {
		MENU_CODE = mENU_CODE;
	}

	public String getCATEGORY_NAME() {
		return CATEGORY_NAME;
	}

	public void setCATEGORY_NAME(String cATEGORY_NAME) {
		CATEGORY_NAME = cATEGORY_NAME;
	}
	
	
	
}
