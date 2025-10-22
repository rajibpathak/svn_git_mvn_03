/*
Document   : User Credentials POJO
Author     : Jamal Abraar
last update: 01/04/2024
*/

package com.omnet.cnt.Model;

public class UserData {
	 private String userId;
	 private String currentPassword;
	 
		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	    public String getCurrentPassword() {
	        return currentPassword;
	    }

	    public void setCurrentPassword(String currentPassword) {
	        this.currentPassword = currentPassword;
	    }

	

		
}
