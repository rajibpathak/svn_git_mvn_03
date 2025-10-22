package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Omnet_Users")
public class Login {
    @Id
	
	 @Column(name = "USER_ID") 
    private String userId;
    private String currentPassword;
    
    private String encryptedPassword;
    
    @Column(name = "user_status")
    private String userstatus;
    
    
    
	private Date passwordExpiredDate;


	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getPasswordExpiredDate() {
		return passwordExpiredDate;
	}

	public void setPasswordExpiredDate(Date passwordExpiredDate) {
		this.passwordExpiredDate = passwordExpiredDate;
	}
	
	public Login(String userId,String currentPassword, String encryptedPassword, String userstatus,
			Date passwordExpiredDate) {
		super();
		this.userId = userId;
		this.currentPassword = currentPassword;
		this.encryptedPassword = encryptedPassword;
		this.userstatus = userstatus;		
		this.passwordExpiredDate = passwordExpiredDate;
	}
	
	public Login() {
		super();
	}

	@Override
	public String toString() {
		return "Login [ userId=" + userId +  ",currentPassword=" + currentPassword + ", userstatus=" + userstatus
				+ ",]";
	}
}


