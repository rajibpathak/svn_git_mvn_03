package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NotiCategory {
	
	@Id
	private String short_message;
	private String message_description;
	public String getShort_message() {
		return short_message;
	}
	public void setShort_message(String short_message) {
		this.short_message = short_message;
	}
	public String getMessage_description() {
		return message_description;
	}
	public void setMessage_description(String message_description) {
		this.message_description = message_description;
	}
	
	

}
