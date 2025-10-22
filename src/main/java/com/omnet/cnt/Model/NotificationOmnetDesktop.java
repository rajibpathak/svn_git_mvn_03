package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class NotificationOmnetDesktop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String user_mess_seq_num;
	private String short_message;
	private String date_time_sent;
	private String created_user_id;
	private String created_user_name;
	private String no_of_days;
	private String screen_code;
	private String message_text;
	private String remove;
	private String sbi_no;
	private String commit_no;
	private String offender_name;
	private String screen_url;
	private String USER_ID_VIEWED_BY;
	private String Message_Read;
	private String access_time;
	private String name_prefix;
	
 
	


	public NotificationOmnetDesktop() {
		
	}


	
	
	public String getAccess_time() {
		return access_time;
	}


	public void setAccess_time(String access_time) {
		this.access_time = access_time;
	}


	public String getName_prefix() {
		return name_prefix;
	}

	public void setName_prefix(String name_prefix) {
		this.name_prefix = name_prefix;
	}

	public String getMessage_Read() {
		return Message_Read;
	}


	public void setMessage_Read(String message_Read) {
		Message_Read = message_Read;
	}


	public String getUSER_ID_VIEWED_BY() {
		return USER_ID_VIEWED_BY;
	}


	public void setUSER_ID_VIEWED_BY(String uSER_ID_VIEWED_BY) {
		USER_ID_VIEWED_BY = uSER_ID_VIEWED_BY;
	}


	public String getScreen_url() {
		return screen_url;
	}


	public void setScreen_url(String screen_url) {
		this.screen_url = screen_url;
	}


	public String getUser_mess_seq_num() {
		return user_mess_seq_num;
	}


	public void setUser_mess_seq_num(String user_mess_seq_num) {
		this.user_mess_seq_num = user_mess_seq_num;
	}


	public String getShort_message() {
		return short_message;
	}


	public void setShort_message(String short_message) {
		this.short_message = short_message;
	}


	public String getDate_time_sent() {
		return date_time_sent;
	}


	public void setDate_time_sent(String date_time_sent) {
		this.date_time_sent = date_time_sent;
	}


	public String getCreated_user_id() {
		return created_user_id;
	}


	public void setCreated_user_id(String created_user_id) {
		this.created_user_id = created_user_id;
	}


	public String getCreated_user_name() {
		return created_user_name;
	}


	public void setCreated_user_name(String created_user_name) {
		this.created_user_name = created_user_name;
	}


	public String getNo_of_days() {
		return no_of_days;
	}


	public void setNo_of_days(String no_of_days) {
		this.no_of_days = no_of_days;
	}


	public String getScreen_code() {
		return screen_code;
	}


	public void setScreen_code(String screen_code) {
		this.screen_code = screen_code;
	}


	public String getMessage_text() {
		return message_text;
	}


	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}


	public String getRemove() {
		return remove;
	}


	public void setRemove(String remove) {
		this.remove = remove;
	}


	public String getSbi_no() {
		return sbi_no;
	}


	public void setSbi_no(String sbi_no) {
		this.sbi_no = sbi_no;
	}


	public String getCommit_no() {
		return commit_no;
	}


	public void setCommit_no(String commit_no) {
		this.commit_no = commit_no;
	}


	public String getOffender_name() {
		return offender_name;
	}


	public void setOffender_name(String offender_name) {
		this.offender_name = offender_name;
	}
	
	
	
}
