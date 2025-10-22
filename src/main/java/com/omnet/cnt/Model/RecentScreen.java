package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "doc_audit_dtl")
public class RecentScreen {

	
	@Id
	private String screen_code;
	private String screen_name;
	private String user_id;
	private String access_date_time;
	private String screen_url;
	private String user_name;
	
	
	
	public RecentScreen(String screen_code, String screen_name, String user_id, String access_date_time,
			String screen_url, String user_name) {
		super();
		this.screen_code = screen_code;
		this.screen_name = screen_name;
		this.user_id = user_id;
		this.access_date_time = access_date_time;
		this.screen_url = screen_url;
		this.user_name = user_name;
	}
	
	


	public String getUser_name() {
		return user_name;
	}




	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}




	public String getScreen_url() {
		return screen_url;
	}


	public void setScreen_url(String screen_url) {
		this.screen_url = screen_url;
	}


	public RecentScreen() {
		
	}


	public String getScreen_code() {
		return screen_code;
	}


	public void setScreen_code(String screen_code) {
		this.screen_code = screen_code;
	}


	public String getScreen_name() {
		return screen_name;
	}


	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getAccess_date_time() {
		return access_date_time;
	}


	public void setAccess_date_time(String access_date_time) {
		this.access_date_time = access_date_time;
	}
	
	
}
