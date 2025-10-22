package com.omnet.cnt.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.omnet.cnt.Model.SubMenu;

@Entity
public  class User_Roles  {


//	private String access;
//	
//	public String getAccess() {
//		return access;
//	}
//	public void setAccess(String access) {
//		access = access;
//	}
	
	@Id
	private String Sub_menu_code;
	private String insert_priv_flag;
	private String update_priv_flag;
	private String view_priv_flag;
	private String screen_url;
	private String Screen_Name;
	private String CATEGORY_CODE;
	
	public User_Roles() {
		
	}

	
	public String getScreen_Name() {
		return Screen_Name;
	}




	public void setScreen_Name(String screen_Name) {
		Screen_Name = screen_Name;
	}




	public String getCATEGORY_CODE() {
		return CATEGORY_CODE;
	}




	public void setCATEGORY_CODE(String cATEGORY_CODE) {
		CATEGORY_CODE = cATEGORY_CODE;
	}




	public String getSub_menu_code() {
		return Sub_menu_code;
	}


	public void setSub_menu_code(String sub_menu_code) {
		Sub_menu_code = sub_menu_code;
	}


	public String getInsert_priv_flag() {
		return insert_priv_flag;
	}


	public void setInsert_priv_flag(String insert_priv_flag) {
		this.insert_priv_flag = insert_priv_flag;
	}


	public String getUpdate_priv_flag() {
		return update_priv_flag;
	}


	public void setUpdate_priv_flag(String update_priv_flag) {
		this.update_priv_flag = update_priv_flag;
	}


	public String getView_priv_flag() {
		return view_priv_flag;
	}


	public void setView_priv_flag(String view_priv_flag) {
		this.view_priv_flag = view_priv_flag;
	}


	public String getScreen_url() {
		return screen_url;
	}


	public void setScreen_url(String screen_url) {
		this.screen_url = screen_url;
	}


	
	
	
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private String User_ID;
//	private int ROLE_SEQ_NUM;
//	private Date ROLE_EFFECTIVE_DATE;
//	private Date ROLE_EXPIRY_DATE;
//	private String INSERTED_USER_ID;
//	private Date INSERTED_DATE_TIME;
//	private String UPDATED_USER_ID;
//	private Date UPDATED_DATE_TIME;
//	private String TERMINAL;
//	private int SESSIONID;
//	
//	
//	
//	
//	
//	public User_Roles(String user_ID, int rOLE_SEQ_NUM, Date rOLE_EFFECTIVE_DATE, Date rOLE_EXPIRY_DATE,
//			String iNSERTED_USER_ID, Date iNSERTED_DATE_TIME, String uPDATED_USER_ID, Date uPDATED_DATE_TIME,
//			String tERMINAL, int sESSIONID) {
//		super();
//		User_ID = user_ID;
//		ROLE_SEQ_NUM = rOLE_SEQ_NUM;
//		ROLE_EFFECTIVE_DATE = rOLE_EFFECTIVE_DATE;
//		ROLE_EXPIRY_DATE = rOLE_EXPIRY_DATE;
//		INSERTED_USER_ID = iNSERTED_USER_ID;
//		INSERTED_DATE_TIME = iNSERTED_DATE_TIME;
//		UPDATED_USER_ID = uPDATED_USER_ID;
//		UPDATED_DATE_TIME = uPDATED_DATE_TIME;
//		TERMINAL = tERMINAL;
//		SESSIONID = sESSIONID;
//	}
//
//
//
//
//	public String getUser_ID() {
//		return User_ID;
//	}
//
//
//
//
//	public void setUser_ID(String user_ID) {
//		User_ID = user_ID;
//	}
//
//
//
//
//	public int getROLE_SEQ_NUM() {
//		return ROLE_SEQ_NUM;
//	}
//
//
//
//
//	public void setROLE_SEQ_NUM(int rOLE_SEQ_NUM) {
//		ROLE_SEQ_NUM = rOLE_SEQ_NUM;
//	}
//
//
//
//
//	public Date getROLE_EFFECTIVE_DATE() {
//		return ROLE_EFFECTIVE_DATE;
//	}
//
//
//
//
//	public void setROLE_EFFECTIVE_DATE(Date rOLE_EFFECTIVE_DATE) {
//		ROLE_EFFECTIVE_DATE = rOLE_EFFECTIVE_DATE;
//	}
//
//
//
//
//	public Date getROLE_EXPIRY_DATE() {
//		return ROLE_EXPIRY_DATE;
//	}
//
//
//
//
//	public void setROLE_EXPIRY_DATE(Date rOLE_EXPIRY_DATE) {
//		ROLE_EXPIRY_DATE = rOLE_EXPIRY_DATE;
//	}
//
//
//
//
//	public String getINSERTED_USER_ID() {
//		return INSERTED_USER_ID;
//	}
//
//
//
//
//	public void setINSERTED_USER_ID(String iNSERTED_USER_ID) {
//		INSERTED_USER_ID = iNSERTED_USER_ID;
//	}
//
//
//
//
//	public Date getINSERTED_DATE_TIME() {
//		return INSERTED_DATE_TIME;
//	}
//
//
//
//
//	public void setINSERTED_DATE_TIME(Date iNSERTED_DATE_TIME) {
//		INSERTED_DATE_TIME = iNSERTED_DATE_TIME;
//	}
//
//
//
//
//	public String getUPDATED_USER_ID() {
//		return UPDATED_USER_ID;
//	}
//
//
//
//
//	public void setUPDATED_USER_ID(String uPDATED_USER_ID) {
//		UPDATED_USER_ID = uPDATED_USER_ID;
//	}
//
//
//
//
//	public Date getUPDATED_DATE_TIME() {
//		return UPDATED_DATE_TIME;
//	}
//
//
//
//
//	public void setUPDATED_DATE_TIME(Date uPDATED_DATE_TIME) {
//		UPDATED_DATE_TIME = uPDATED_DATE_TIME;
//	}
//
//
//
//
//	public String getTERMINAL() {
//		return TERMINAL;
//	}
//
//
//
//
//	public void setTERMINAL(String tERMINAL) {
//		TERMINAL = tERMINAL;
//	}
//
//
//
//
//	public int getSESSIONID() {
//		return SESSIONID;
//	}
//
//
//
//
//	public void setSESSIONID(int sESSIONID) {
//		SESSIONID = sESSIONID;
//	}
//

	
}
