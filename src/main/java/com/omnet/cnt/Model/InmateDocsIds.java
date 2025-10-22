package com.omnet.cnt.Model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INMATE_DOC_IDS")
public class InmateDocsIds {
    @Id	
    
    private String instNum;
    @Column(name = "COMMIT_NO") 
    private String commitNo;
    private String docId;
   
	private Date  instAdmissDate;
    private String instAdmissTime;
    private Date instReleaseDate;
    private String instReleaseTime;
    private String admissMethod;
    private String releaseMethod;
    private String inmtTpCd;
	
    
  
	public InmateDocsIds() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InmateDocsIds(String instNum, String commitNo, String docId, Date instAdmissDate, String instAdmissTime,
			Date instReleaseDate, String instReleaseTime, String admissMethod, String releaseMethod, String inmtTpCd) {
		super();
		this.instNum = instNum;
		this.commitNo = commitNo;
		this.docId = docId;
		this.instAdmissDate = instAdmissDate;
		this.instAdmissTime = instAdmissTime;
		this.instReleaseDate = instReleaseDate;
		this.instReleaseTime = instReleaseTime;
		this.admissMethod = admissMethod;
		this.releaseMethod = releaseMethod;
		this.inmtTpCd = inmtTpCd;
	}
	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public String getInstNum() {
		return instNum;
	}
	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}
	public Date getInstAdmissDate() {
		return instAdmissDate;
	}
	public void setInstAdmissDate(Date instAdmissDate) {
		this.instAdmissDate = instAdmissDate;
	}
	public String getInstAdmissTime() {
		return instAdmissTime;
	}
	public void setInstAdmissTime(String instAdmissTime) {
		this.instAdmissTime = instAdmissTime;
	}
	public Date getInstReleaseDate() {
		return instReleaseDate;
	}
	public void setInstReleaseDate(Date instReleaseDate) {
		this.instReleaseDate = instReleaseDate;
	}
	 public String getDocId() {
			return docId;
		}
		public void setDocId(String docId) {
			this.docId = docId;
		}
	public String getInstReleaseTime() {
		return instReleaseTime;
	}
	public void setInstReleaseTime(String instReleaseTime) {
		this.instReleaseTime = instReleaseTime;
	}
	public String getAdmissMethod() {
		return admissMethod;
	}
	public void setAdmissMethod(String admissMethod) {
		this.admissMethod = admissMethod;
	}
	public String getReleaseMethod() {
		return releaseMethod;
	}
	public void setReleaseMethod(String releaseMethod) {
		this.releaseMethod = releaseMethod;
	}
	public String getInmtTpCd() {
		return inmtTpCd;
	}
	public void setInmtTpCd(String inmtTpCd) {
		this.inmtTpCd = inmtTpCd;
	}
    

}
