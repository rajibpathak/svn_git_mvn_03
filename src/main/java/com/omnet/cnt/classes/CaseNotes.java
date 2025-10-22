
/*
Document   : Case Notes
Author     : Jamal Abraar
last update: 03/06/2024
*/

package com.omnet.cnt.classes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;



public class CaseNotes {
    
    private String fullName;
    private String commitNo;
    private String contTpCd;
    private String contTpDesc;
    private String correctionFlag;
    private String csmnContComnt;
    private Timestamp csmnContDt;
    private String csmnContInitDdocFlg;
    private Integer csmnContSeqNum;
    private String csmnContSprvNegFlg;
    private String csmnContTm;
    private String enteredByUserId;
    private Timestamp insertedDateTime;
    private String interventionTp;
    private String iscApprovedFlag;
    private Timestamp iscRequestedDateBox;
    private String reportNum;
    private String resnCdNegCont;
    private String resnDesc;
    private String insertedTime;
    private String userFirstName;
    private String userIdInit;
    private String userLastName;
    private String userMidName;
    private String userSuffixName;

    private List<ContactedPerson> contactedPersons;
    private List<ISCData> iscData;
    
    public static class ContactedPerson {
    	private String contactCommitNo;
    	private Integer contSeqNum;
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

		public String getContactCommitNo() {
			return contactCommitNo;
		}

		public void setContactCommitNo(String contactCommitNo) {
			this.contactCommitNo = contactCommitNo;
		}

		public Integer getContSeqNum() {
			return contSeqNum;
		}

		public void setContSeqNum(Integer contSeqNum) {
			this.contSeqNum = contSeqNum;
		}      
    }
    
    public static class ISCData {
    	private String iscCommitNo;
        private String iscApprovedFlag;
        private Timestamp iscRequestedDateBox;
		
        public String getIscCommitNo() {
			return iscCommitNo;
		}
        
		public void setIscCommitNo(String iscCommitNo) {
			this.iscCommitNo = iscCommitNo;
		}
		
		public String getIscApprovedFlag() {
			return iscApprovedFlag;
		}
		
		public void setIscApprovedFlag(String iscApprovedFlag) {
			this.iscApprovedFlag = iscApprovedFlag;
		}
		
		public Timestamp getIscRequestedDateBox() {
			return iscRequestedDateBox;
		}
		
		public void setIscRequestedDateBox(Timestamp iscRequestedDateBox) {
			this.iscRequestedDateBox = iscRequestedDateBox;
		}
    }
    
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
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

	public String getCorrectionFlag() {
		return correctionFlag;
	}

	public void setCorrectionFlag(String correctionFlag) {
		this.correctionFlag = correctionFlag;
	}

	public String getCsmnContComnt() {
		return csmnContComnt;
	}

	public void setCsmnContComnt(String csmnContComnt) {
		this.csmnContComnt = csmnContComnt;
	}

	public Timestamp getCsmnContDt() {
		return csmnContDt;
	}

	public void setCsmnContDt(Timestamp csmnContDt) {
		this.csmnContDt = csmnContDt;
	}

	public String getCsmnContInitDdocFlg() {
		return csmnContInitDdocFlg;
	}

	public void setCsmnContInitDdocFlg(String csmnContInitDdocFlg) {
		this.csmnContInitDdocFlg = csmnContInitDdocFlg;
	}

	public Integer getCsmnContSeqNum() {
		return csmnContSeqNum;
	}

	public void setCsmnContSeqNum(Integer csmnContSeqNum) {
		this.csmnContSeqNum = csmnContSeqNum;
	}

	public String getCsmnContSprvNegFlg() {
		return csmnContSprvNegFlg;
	}

	public void setCsmnContSprvNegFlg(String csmnContSprvNegFlg) {
		this.csmnContSprvNegFlg = csmnContSprvNegFlg;
	}

	public String getCsmnContTm() {
		return csmnContTm;
	}

	public void setCsmnContTm(String csmnContTm) {
		this.csmnContTm = csmnContTm;
	}

	public String getEnteredByUserId() {
		return enteredByUserId;
	}

	public void setEnteredByUserId(String enteredByUserId) {
		this.enteredByUserId = enteredByUserId;
	}

	public Timestamp getInsertedDateTime() {
		return insertedDateTime;
	}

	public void setInsertedDateTime(Timestamp insertedDateTime) {
		this.insertedDateTime = insertedDateTime;
	}

	public String getInterventionTp() {
		return interventionTp;
	}

	public void setInterventionTp(String interventionTp) {
		this.interventionTp = interventionTp;
	}

	public String getIscApprovedFlag() {
		return iscApprovedFlag;
	}

	public void setIscApprovedFlag(String iscApprovedFlag) {
		this.iscApprovedFlag = iscApprovedFlag;
	}

	public Timestamp getIscRequestedDateBox() {
		return iscRequestedDateBox;
	}

	public void setIscRequestedDateBox(Timestamp iscRequestedDateBox) {
		this.iscRequestedDateBox = iscRequestedDateBox;
	}

	public String getReportNum() {
		return reportNum;
	}

	public void setReportNum(String reportNum) {
		this.reportNum = reportNum;
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

	public String getInsertedTime() {
		return insertedTime;
	}

	public void setInsertedTime(String insertedTime) {
		this.insertedTime = insertedTime;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserIdInit() {
		return userIdInit;
	}

	public void setUserIdInit(String userIdInit) {
		this.userIdInit = userIdInit;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserMidName() {
		return userMidName;
	}

	public void setUserMidName(String userMidName) {
		this.userMidName = userMidName;
	}

	public String getUserSuffixName() {
		return userSuffixName;
	}

	public void setUserSuffixName(String userSuffixName) {
		this.userSuffixName = userSuffixName;
	}

	public List<ContactedPerson> getContactedPersons() {
		return contactedPersons;
	}

	public void setContactedPersons(List<ContactedPerson> contactedPersons) {
		this.contactedPersons = contactedPersons;
	}

	public List<ISCData> getIscData() {
		return iscData;
	}

	public void setIscData(List<ISCData> iscData) {
		this.iscData = iscData;
	}

	public CaseNotes() {
		super();
	}
    
    public static CaseNotes CaseHistory(Map<String, Object> row) {
    	CaseNotes caseHistory = new CaseNotes();
        caseHistory.setFullName((String) row.get("C.USER_LAST_NAME||''||C.USER_FIRST_NAME||''||C.USER_MID_NAME||''||C.USER_SUFFIX_NAME"));
        caseHistory.setCommitNo((String) row.get("COMMIT_NO"));
        caseHistory.setContTpCd((String) row.get("CONT_TP_CD"));
        caseHistory.setContTpDesc((String) row.get("CONT_TP_DESC"));
        caseHistory.setCorrectionFlag((String) row.get("CORRECTION_FLAG"));
        caseHistory.setCsmnContComnt((String) row.get("CSMN_CONT_COMNT"));
        caseHistory.setCsmnContDt((Timestamp ) row.get("CSMN_CONT_DT"));
        caseHistory.setCsmnContInitDdocFlg((String) row.get("CSMN_CONT_INIT_DDOC_FLG"));
        caseHistory.setCsmnContSeqNum(((BigDecimal) row.get("CSMN_CONT_SEQ_NUM")).intValue());
        caseHistory.setCsmnContSprvNegFlg((String) row.get("CSMN_CONT_SPRV_NEG_FLG"));
        caseHistory.setCsmnContTm((String) row.get("CSMN_CONT_TM"));
        caseHistory.setEnteredByUserId((String) row.get("ENTERED_BY_USER_ID"));
        caseHistory.setInsertedDateTime((Timestamp ) row.get("INSERTED_DATE_TIME"));
        caseHistory.setInterventionTp((String) row.get("INTERVENTION_TP"));
        caseHistory.setIscApprovedFlag((String) row.get("ISC_APPROVED_FLAG"));
        caseHistory.setIscRequestedDateBox((Timestamp ) row.get("ISC_REQUESTED_DATE_BOX"));
        caseHistory.setReportNum((String) row.get("REPORT_NUM"));
        caseHistory.setResnCdNegCont((String) row.get("RESN_CD_NEG_CONT"));
        caseHistory.setResnDesc((String) row.get("RESN_DESC"));
        caseHistory.setInsertedTime((String) row.get("TO_CHAR(B.INSERTED_DATE_TIME,'HH24:MI')"));
        caseHistory.setUserFirstName((String) row.get("USER_FIRST_NAME"));
        caseHistory.setUserIdInit((String) row.get("USER_ID_INIT"));
        caseHistory.setUserLastName((String) row.get("USER_LAST_NAME"));
        caseHistory.setUserMidName((String) row.get("USER_MID_NAME"));
        caseHistory.setUserSuffixName((String) row.get("USER_SUFFIX_NAME"));
        return caseHistory;
    }
    
    public static CaseNotes caseNotesData(Map<String, Object> data) {
        CaseNotes caseNotes = new CaseNotes();
        caseNotes.setFullName((String) data.get("USER_NAME"));
        caseNotes.setCommitNo((String) data.get("COMMIT_NO"));
        caseNotes.setContTpCd((String) data.get("CONT_TP_CD"));
        caseNotes.setContTpDesc((String) data.get("CONT_TP_DESC"));
        caseNotes.setCorrectionFlag((String) data.get("CORRECTION_FLAG"));
        caseNotes.setCsmnContComnt((String) data.get("CSMN_CONT_COMNT"));
        caseNotes.setCsmnContDt((Timestamp ) data.get("CSMN_CONT_DT"));
        caseNotes.setCsmnContInitDdocFlg((String) data.get("CSMN_CONT_INIT_DDOC_FLG"));
        caseNotes.setCsmnContSeqNum(((BigDecimal) data.get("CSMN_CONT_SEQ_NUM")).intValue());
        caseNotes.setCsmnContSprvNegFlg((String) data.get("CSMN_CONT_SPRV_NEG_FLG"));
        caseNotes.setCsmnContTm((String) data.get("CSMN_CONT_TM"));
        caseNotes.setEnteredByUserId((String) data.get("ENTERED_BY_USER_ID"));
        caseNotes.setInsertedDateTime((Timestamp ) data.get("CSMN_CONT_DT"));;
        caseNotes.setInterventionTp((String) data.get("INTERVENTION_TP"));
        caseNotes.setReportNum((String) data.get("REPORT_NUM"));
        caseNotes.setResnCdNegCont((String) data.get("RESN_CD_NEG_CONT"));
        caseNotes.setResnDesc((String) data.get("RESN_DESC"));
        caseNotes.setInsertedTime((String) data.get("INSERTED_TIME"));
        caseNotes.setUserFirstName((String) data.get("USER_FIRST_NAME"));
        caseNotes.setUserIdInit((String) data.get("USER_ID_INIT"));
        caseNotes.setUserLastName((String) data.get("USER_LAST_NAME"));
        caseNotes.setUserMidName((String) data.get("USER_MID_NAME"));
        caseNotes.setUserSuffixName((String) data.get("USER_SUFFIX_NAME"));
        return caseNotes;
    }
    
    public static CaseNotes.ContactedPerson contactedPersonsData(Map<String, Object> data) {
    	CaseNotes.ContactedPerson contactedPersons = new CaseNotes.ContactedPerson();
    	contactedPersons.setContactCommitNo((String) data.get("COMMIT_NO"));
    	contactedPersons.setContSeqNum(((BigDecimal) data.get("CSMN_CONT_SEQ_NUM")).intValue());
    	if(data.get("RELATIONSHIP_SEQ_NUM") != null)
    		contactedPersons.setRelationshipSeqNum(((BigDecimal) data.get("RELATIONSHIP_SEQ_NUM")).intValue());
    	contactedPersons.setPrsnSeqNum(((BigDecimal) data.get("CSMN_PRSN_SEQ_NUM")).intValue());
    	contactedPersons.setContFname((String) data.get("cont_fname"));
    	contactedPersons.setContLname((String) data.get("cont_lname"));
    	contactedPersons.setContMname((String) data.get("cont_mname"));
    	contactedPersons.setContSname((String) data.get("cont_sname"));
    	contactedPersons.setRelationshipCode((String) data.get("RELATIONSHIP_CODE"));
		return contactedPersons;
    }
    
    public static CaseNotes.ISCData iscRequestedFlagAndDate(Map<String, Object> data) {
    	CaseNotes.ISCData iscData = new CaseNotes.ISCData();
    	iscData.setIscApprovedFlag((String) data.get("isc_approved_flag"));
    	iscData.setIscRequestedDateBox((Timestamp) data.get("isc_requested_date_box"));
    	return iscData;    	
    }
}
