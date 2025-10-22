package com.omnet.cnt.Model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Inmate")
public class StayHistory {
	@Id
	  private String commitNo;
	    private String inmateName;
	    private String stay;
	    private String instNum;
	    private String instName;
	    private java.util.Date instAdmissDate;
	    private String instAdmissTime;
	    private java.util.Date instReleaseDate;
	    private String instReleaseTime;
	    private String sentTypeDesc;
	    private String status;
	    private String inmtTpCd;
	    
	    
		public StayHistory() {
			super();
			// TODO Auto-generated constructor stub
		}
		public StayHistory(String commitNo, String inmateName, String stay, String instNum, String instName,
				Date instAdmissDate, String instAdmissTime, Date instReleaseDate, String instReleaseTime,
				String sentTypeDesc, String status, String inmtTpCd) {
			super();
			this.commitNo = commitNo;
			this.inmateName = inmateName;
			this.stay = stay;
			this.instNum = instNum;
			this.instName = instName;
			this.instAdmissDate = instAdmissDate;
			this.instAdmissTime = instAdmissTime;
			this.instReleaseDate = instReleaseDate;
			this.instReleaseTime = instReleaseTime;
			this.sentTypeDesc = sentTypeDesc;
			this.status = status;
			this.inmtTpCd = inmtTpCd;
		}
		public String getCommitNo() {
			return commitNo;
		}
		public void setCommitNo(String commitNo) {
			this.commitNo = commitNo;
		}
		public String getInmateName() {
			return inmateName;
		}
		public void setInmateName(String inmateName) {
			this.inmateName = inmateName;
		}
		public String getStay() {
			return stay;
		}
		public void setStay(String stay) {
			this.stay = stay;
		}
		public String getInstNum() {
			return instNum;
		}
		public void setInstNum(String instNum) {
			this.instNum = instNum;
		}
		public String getInstName() {
			return instName;
		}
		public void setInstName(String instName) {
			this.instName = instName;
		}
		public java.util.Date getInstAdmissDate() {
			return instAdmissDate;
		}
		public void setInstAdmissDate(java.util.Date instAdmissDate) {
			this.instAdmissDate = instAdmissDate;
		}
		public String getInstAdmissTime() {
			return instAdmissTime;
		}
		public void setInstAdmissTime(String instAdmissTime) {
			this.instAdmissTime = instAdmissTime;
		}
		public java.util.Date getInstReleaseDate() {
			return instReleaseDate;
		}
		public void setInstReleaseDate(java.util.Date instReleaseDate) {
			this.instReleaseDate = instReleaseDate;
		}
		public String getInstReleaseTime() {
			return instReleaseTime;
		}
		public void setInstReleaseTime(String instReleaseTime) {
			this.instReleaseTime = instReleaseTime;
		}
		public String getSentTypeDesc() {
			return sentTypeDesc;
		}
		public void setSentTypeDesc(String sentTypeDesc) {
			this.sentTypeDesc = sentTypeDesc;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getInmtTpCd() {
			return inmtTpCd;
		}
		public void setInmtTpCd(String inmtTpCd) {
			this.inmtTpCd = inmtTpCd;
		}
	    

}
