package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContactInformationInsert {

	@Id
	private String commit_no;
	private long csmn_cont_seq_no;
	private String cont_tp_cd;
	private Date csmn_cont_dt;
	private String csmn_cont_tm;
	private Date inserted_date;
	private String inserted_time;  
	private String csmn_cont_sprv_neg_flg;
	private String correction_flag;
	private String resn_cd_neg_cont;
	private String entered_by_user_id;
	private String entered_by_user_name;
	private String csmn_cont_comnt;
	private String csmn_cont_init_ddoc_flg;
	private String user_id_init;
	private String REPORT_NUM;
	private String intervention_tp;
	private String verify_addr_flag;
	
	

	public String getCommit_no() {
		return commit_no;
	}
	public void setCommit_no(String commit_no) {
		this.commit_no = commit_no;
	}
	public long getCsmn_cont_seq_no() {
		return csmn_cont_seq_no;
	}
	public void setCsmn_cont_seq_no(long csmn_cont_seq_no) {
		this.csmn_cont_seq_no = csmn_cont_seq_no;
	}
	public String getCont_tp_cd() {
		return cont_tp_cd;
	}
	public void setCont_tp_cd(String cont_tp_cd) {
		this.cont_tp_cd = cont_tp_cd;
	}
	public Date getCsmn_cont_dt() {
		return csmn_cont_dt;
	}
	public void setCsmn_cont_dt(Date csmn_cont_dt) {
		this.csmn_cont_dt = csmn_cont_dt;
	}
	public String getCsmn_cont_tm() {
		return csmn_cont_tm;
	}
	public void setCsmn_cont_tm(String csmn_cont_tm) {
		this.csmn_cont_tm = csmn_cont_tm;
	}
	public Date getInserted_date() {
		return inserted_date;
	}
	public void setInserted_date(Date inserted_date) {
		this.inserted_date = inserted_date;
	}
	public String getInserted_time() {
		return inserted_time;
	}
	public void setInserted_time(String inserted_time) {
		this.inserted_time = inserted_time;
	}
	public String getCsmn_cont_sprv_neg_flg() {
		return csmn_cont_sprv_neg_flg;
	}
	public void setCsmn_cont_sprv_neg_flg(String csmn_cont_sprv_neg_flg) {
		this.csmn_cont_sprv_neg_flg = csmn_cont_sprv_neg_flg;
	}
	public String getCorrection_flag() {
		return correction_flag;
	}
	public void setCorrection_flag(String correction_flag) {
		this.correction_flag = correction_flag;
	}
	public String getResn_cd_neg_cont() {
		return resn_cd_neg_cont;
	}
	public void setResn_cd_neg_cont(String resn_cd_neg_cont) {
		this.resn_cd_neg_cont = resn_cd_neg_cont;
	}
	public String getEntered_by_user_id() {
		return entered_by_user_id;
	}
	public void setEntered_by_user_id(String entered_by_user_id) {
		this.entered_by_user_id = entered_by_user_id;
	}
	public String getEntered_by_user_name() {
		return entered_by_user_name;
	}
	public void setEntered_by_user_name(String entered_by_user_name) {
		this.entered_by_user_name = entered_by_user_name;
	}
	public String getCsmn_cont_comnt() {
		return csmn_cont_comnt;
	}
	public void setCsmn_cont_comnt(String csmn_cont_comnt) {
		this.csmn_cont_comnt = csmn_cont_comnt;
	}
	public String getCsmn_cont_init_ddoc_flg() {
		return csmn_cont_init_ddoc_flg;
	}
	public void setCsmn_cont_init_ddoc_flg(String csmn_cont_init_ddoc_flg) {
		this.csmn_cont_init_ddoc_flg = csmn_cont_init_ddoc_flg;
	}
	public String getUser_id_init() {
		return user_id_init;
	}
	public void setUser_id_init(String user_id_init) {
		this.user_id_init = user_id_init;
	}
	public String getREPORT_NUM() {
		return REPORT_NUM;
	}
	public void setREPORT_NUM(String rEPORT_NUM) {
		REPORT_NUM = rEPORT_NUM;
	}
	public String getIntervention_tp() {
		return intervention_tp;
	}
	public void setIntervention_tp(String intervention_tp) {
		this.intervention_tp = intervention_tp;
	}
	public String getVerify_addr_flag() {
		return verify_addr_flag;
	}
	public void setVerify_addr_flag(String verify_addr_flag) {
		this.verify_addr_flag = verify_addr_flag;
	}
	
	
}
