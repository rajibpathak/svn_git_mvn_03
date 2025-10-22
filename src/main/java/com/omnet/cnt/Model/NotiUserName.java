package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NotiUserName {
    @Id
    private String user_id;
    private String userLastName;
    private String userFirstName;
    private String userMidName;
    private String userSuffixName;
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserMidName() {
		return userMidName;
	}
	public void setUserMidName(String userMidName) {
		this.userMidName = userMidName;
	}
	public String getUserSuffixName() {
		return userSuffixName;
	}
	public void setUserSuffixName(String userSuffixName) {
		this.userSuffixName = userSuffixName;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
    
    
	
}
