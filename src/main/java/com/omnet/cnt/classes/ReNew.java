/*
Document   : Case Notes
Author     : Jamal Abraar
last update: 03/06/2024
*/

package com.omnet.cnt.classes;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class ReNew {
    
	private String sbiNo;
	private String commitNo;   
    private Integer contSeqNum;    
    private Date csmnContDt;
    private String contTpCd;
    private String contTpDesc;
    private String csmnContTm;    
    private Date insertedDate;    
    private String insertedTime;    
    private String csmnContSprvNegFlg;    
    private String resnCdNegCont; 
    private String resnDesc;
    private String csmnContComnt;    
    private String correctionFlag;    
    private String enteredByUserId;    
    private String enteredByUserName;    
    private String csmnContInitDdocFlg;    
    private String userIdInit;
    private String userInitLname;
    private String userInitFname;
    private String userInitMname;
    private String userInitSname;
    private Date iscRequestedDateBox;
    private String iscApprovedFlag;
    private String reportNum;    
    private String interventionTp;

    
    private List<ContactedPerson> contactedPersons;

    public static class ContactedPerson {
    	private Integer prsnSeqNum;
    	private Integer relationshipSeqNum;
        private String relationshipCode;
        private String contLname;
        private String contFname;
        private String contMname;
        private String contSname;
        
        
        public Integer getRelationshipSeqNum() {
			return relationshipSeqNum;
		}

		public void setRelationshipSeqNum(Integer relationshipSeqNum) {
			this.relationshipSeqNum = relationshipSeqNum;
		}

		public Integer getPrsnSeqNum() {
			return prsnSeqNum;
		}

		public void setPrsnSeqNum(Integer prsnSeqNum) {
			this.prsnSeqNum = prsnSeqNum;
		}

		public String getRelationshipCode() {
            return relationshipCode;
        }

        public void setRelationshipCode(String relationshipCode) {
            this.relationshipCode = relationshipCode;
        }

        public String getContLname() {
            return contLname;
        }

        public void setContLname(String contLname) {
            this.contLname = contLname;
        }

        public String getContFname() {
            return contFname;
        }

        public void setContFname(String contFname) {
            this.contFname = contFname;
        }

        public String getContMname() {
            return contMname;
        }

        public void setContMname(String contMname) {
            this.contMname = contMname;
        }

        public String getContSname() {
            return contSname;
        }

        public void setContSname(String contSname) {
            this.contSname = contSname;
        }
    }
	
    public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	public Integer getContSeqNum() {
		return contSeqNum;
	}

	public void setContSeqNum(Integer contSeqNum) {
		this.contSeqNum = contSeqNum;
	}

	public Date getCsmnContDt() {
		return csmnContDt;
	}

	public void setCsmnContDt(Date csmnContDt) {
		this.csmnContDt = csmnContDt;
	}

	public String getContTpCd() {
		return contTpCd;
	}

	public void setContTpCd(String contTpCd) {
		this.contTpCd = contTpCd;
	}

	public String getContTpDesc() {
		return contTpDesc;
	}

	public void setContTpDesc(String contTpDesc) {
		this.contTpDesc = contTpDesc;
	}

	public String getCsmnContTm() {
		return csmnContTm;
	}

	public void setCsmnContTm(String csmnContTm) {
		this.csmnContTm = csmnContTm;
	}

	public Date getInsertedDate() {
		return insertedDate;
	}

	public void setInsertedDate(Date insertedDate) {
		this.insertedDate = insertedDate;
	}

	public String getInsertedTime() {
		return insertedTime;
	}

	public void setInsertedTime(String insertedTime) {
		this.insertedTime = insertedTime;
	}

	public String getCsmnContSprvNegFlg() {
		return csmnContSprvNegFlg;
	}

	public void setCsmnContSprvNegFlg(String csmnContSprvNegFlg) {
		this.csmnContSprvNegFlg = csmnContSprvNegFlg;
	}

	public String getResnCdNegCont() {
		return resnCdNegCont;
	}

	public void setResnCdNegCont(String resnCdNegCont) {
		this.resnCdNegCont = resnCdNegCont;
	}

	public String getResnDesc() {
		return resnDesc;
	}

	public void setResnDesc(String resnDesc) {
		this.resnDesc = resnDesc;
	}

	public String getCsmnContComnt() {
		return csmnContComnt;
	}

	public void setCsmnContComnt(String csmnContComnt) {
		this.csmnContComnt = csmnContComnt;
	}

	public String getCorrectionFlag() {
		return correctionFlag;
	}

	public void setCorrectionFlag(String correctionFlag) {
		this.correctionFlag = correctionFlag;
	}

	public String getEnteredByUserId() {
		return enteredByUserId;
	}

	public void setEnteredByUserId(String enteredByUserId) {
		this.enteredByUserId = enteredByUserId;
	}

	public String getEnteredByUserName() {
		return enteredByUserName;
	}

	public void setEnteredByUserName(String enteredByUserName) {
		this.enteredByUserName = enteredByUserName;
	}

	public String getCsmnContInitDdocFlg() {
		return csmnContInitDdocFlg;
	}

	public void setCsmnContInitDdocFlg(String csmnContInitDdocFlg) {
		this.csmnContInitDdocFlg = csmnContInitDdocFlg;
	}

	public String getUserIdInit() {
		return userIdInit;
	}

	public void setUserIdInit(String userIdInit) {
		this.userIdInit = userIdInit;
	}

	public String getUserInitLname() {
		return userInitLname;
	}

	public void setUserInitLname(String userInitLname) {
		this.userInitLname = userInitLname;
	}

	public String getUserInitFname() {
		return userInitFname;
	}

	public void setUserInitFname(String userInitFname) {
		this.userInitFname = userInitFname;
	}

	public String getUserInitMname() {
		return userInitMname;
	}

	public void setUserInitMname(String userInitMname) {
		this.userInitMname = userInitMname;
	}

	public String getUserInitSname() {
		return userInitSname;
	}

	public void setUserInitSname(String userInitSname) {
		this.userInitSname = userInitSname;
	}

	public Date getIscRequestedDateBox() {
		return iscRequestedDateBox;
	}

	public void setIscRequestedDateBox(Date iscRequestedDateBox) {
		this.iscRequestedDateBox = iscRequestedDateBox;
	}

	public String getIscApprovedFlag() {
		return iscApprovedFlag;
	}

	public void setIscApprovedFlag(String iscApprovedFlag) {
		this.iscApprovedFlag = iscApprovedFlag;
	}

	public String getReportNum() {
		return reportNum;
	}

	public void setReportNum(String reportNum) {
		this.reportNum = reportNum;
	}

	public String getInterventionTp() {
		return interventionTp;
	}

	public void setInterventionTp(String interventionTp) {
		this.interventionTp = interventionTp;
	}

	public String getSbiNo() {
		return sbiNo;
	}

	public void setSbiNo(String sbiNo) {
		this.sbiNo = sbiNo;
	}

	public List<ContactedPerson> getContactedPersons() {
		return contactedPersons;
	}

	public void setContactedPersons(List<ContactedPerson> contactedPersons) {
		this.contactedPersons = contactedPersons;
	}

	@Override
	public String toString() {
		return "ReNew [commitNo=" + commitNo + ", contSeqNum=" + contSeqNum + ", csmnContDt=" + csmnContDt
				+ ", contTpCd=" + contTpCd + ", contTpDesc=" + contTpDesc + ", csmnContTm=" + csmnContTm
				+ ", insertedDate=" + insertedDate + ", insertedTime=" + insertedTime + ", csmnContSprvNegFlg="
				+ csmnContSprvNegFlg + ", resnCdNegCont=" + resnCdNegCont + ", resnDesc=" + resnDesc
				+ ", csmnContComnt=" + csmnContComnt + ", correctionFlag=" + correctionFlag + ", enteredByUserId="
				+ enteredByUserId + ", enteredByUserName=" + enteredByUserName + ", csmnContInitDdocFlg="
				+ csmnContInitDdocFlg + ", userIdInit=" + userIdInit + ", userInitLname=" + userInitLname
				+ ", userInitFname=" + userInitFname + ", userInitMname=" + userInitMname + ", userInitSname="
				+ userInitSname + ", iscRequestedDateBox=" + iscRequestedDateBox + ", iscApprovedFlag="
				+ iscApprovedFlag + ", reportNum=" + reportNum + ", interventionTp=" + interventionTp
				+ ", contactedPersons=" + contactedPersons + "]";
	}

}