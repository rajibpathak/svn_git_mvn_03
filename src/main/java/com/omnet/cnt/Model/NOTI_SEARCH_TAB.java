package com.omnet.cnt.Model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class NOTI_SEARCH_TAB {
	
	@Id
	private BigDecimal user_mess_seq_num;
	private String user_id;
	private String inst_num;
	private String screen_code;
	private Timestamp date_time_sent;
	private String message_text;
	private String sent_by_user_id;
	private String sent_by_user_name;
	private BigDecimal no_of_days;
	private String removed_flag;
	private String commit_no;
	private String sbi_no;
	private String offender_last_name;
	private String offender_full_name;
	private String short_message;
	private String message_description;
	
	
	public BigDecimal getUser_mess_seq_num() {
		return user_mess_seq_num;
	}
	public void setUser_mess_seq_num(BigDecimal user_mess_seq_num) {
		this.user_mess_seq_num = user_mess_seq_num;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getInst_num() {
		return inst_num;
	}
	public void setInst_num(String inst_num) {
		this.inst_num = inst_num;
	}
	public String getScreen_code() {
		return screen_code;
	}
	public void setScreen_code(String screen_code) {
		this.screen_code = screen_code;
	}
	public Timestamp getDate_time_sent() {
		return date_time_sent;
	}
	public void setDate_time_sent(Timestamp date_time_sent) {
		this.date_time_sent = date_time_sent;
	}
	public String getMessage_text() {
		return message_text;
	}
	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}
	public String getSent_by_user_id() {
		return sent_by_user_id;
	}
	public void setSent_by_user_id(String sent_by_user_id) {
		this.sent_by_user_id = sent_by_user_id;
	}
	public String getSent_by_user_name() {
		return sent_by_user_name;
	}
	public void setSent_by_user_name(String sent_by_user_name) {
		this.sent_by_user_name = sent_by_user_name;
	}
	public BigDecimal getNo_of_days() {
		return no_of_days;
	}
	public void setNo_of_days(BigDecimal no_of_days) {
		this.no_of_days = no_of_days;
	}
	public String getRemoved_flag() {
		return removed_flag;
	}
	public void setRemoved_flag(String removed_flag) {
		this.removed_flag = removed_flag;
	}
	public String getCommit_no() {
		return commit_no;
	}
	public void setCommit_no(String commit_no) {
		this.commit_no = commit_no;
	}
	public String getSbi_no() {
		return sbi_no;
	}
	public void setSbi_no(String sbi_no) {
		this.sbi_no = sbi_no;
	}
	public String getOffender_last_name() {
		return offender_last_name;
	}
	public void setOffender_last_name(String offender_last_name) {
		this.offender_last_name = offender_last_name;
	}
	public String getOffender_full_name() {
		return offender_full_name;
	}
	public void setOffender_full_name(String offender_full_name) {
		this.offender_full_name = offender_full_name;
	}
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
