package com.omnet.cnt.Model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserNotificationDetails {

	@Id
	private long user_mess_seq_num;
	private String short_message;
	private Date date_time_sent;
	private String created_user_id;
	private String created_user_name;
	private long no_of_days;
	private String screen_code;
	private String message_text;
	private String remove;
	private String sbi_no;
	private String commit_no;
	private String offender_name;
	
	public UserNotificationDetails() {
    }
	
	
	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}
	public long getUser_mess_seq_num() {
		return user_mess_seq_num;
	}
	public void setUser_mess_seq_num(long user_mess_seq_num) {
		this.user_mess_seq_num = user_mess_seq_num;
	}
	public String getShort_message() {
		return short_message;
	}
	public void setShort_message(String short_message) {
		this.short_message = short_message;
	}
	public Date getDate_time_sent() {
		return date_time_sent;
	}
	public void setDate_time_sent(Date date_time_sent) {
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
	public long getNo_of_days() {
		return no_of_days;
	}
	public void setNo_of_days(long no_of_days) {
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
