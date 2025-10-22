package com.omnet.cnt.Model;

import org.springframework.stereotype.Component;

@Component
public class UserStatusResponse {

	private String username;
	private String userstatus;
	
	public UserStatusResponse() {}
	protected UserStatusResponse(String username, String userstatus) {
		super();
		this.username = username;
		this.userstatus = userstatus;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}
	
	
}