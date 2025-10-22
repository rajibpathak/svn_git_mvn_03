package com.omnet.cnt.Model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "INMATE_GRIEVANCE")
public class InmateGrievance {

	@Id
    @Column(name = "GRIEVANCE_SEQ_NUM")
    private Long grievanceSeqNum;

    @Column(name = "GRV_GROUP_NUM")
    private Long groupNo;
    
    @Column(name = "GRIEVANCE_STATUS")
    private String status;

    @Column(name = "DATE_OF_GRIEVANCE")
    private Date grievanceWrittenDate;

    private Date dateEntered;

    @Column(name = "GRVN_LVL_RESL")
    private String levelOfResolution;

    @Column(name = "INST_NUM")
    private String facility;

    @Column(name = "EMERGENCY_GRV_FLAG")
    private String emergency;

    @Column(name = "CONF_GRV_FLAG")
    private String confidential;

    @Column(name = "COUNT_LOC_CODE")
    private String location;

    @Column(name = "GRVN_CATG")
    private String category;

    @Column(name = "GRIEVANCE_CODE")
    private String grievanceType;

    @Column(name = "DATE_RECEIVED")
    private Date igcReceivedDate;

    @Column(name = "RESOLUTION_DATE")
    private Date resolutionDate;

    private String currentHousing;

    @Column(name = "DATE_OF_INCIDENT")
    private Date incidentDate;

    @Column(name = "GRVN_TM")
    private String incidentTime;

    @Lob
    @Column(name = "DESC_OF_COMPLAINT")
    private String descriptionOfComplaint;

    @Lob
    @Column(name = "REMEDY_REQUESTED")
    private String remedyRequested;

    private String typeOfPerson;

    @Column(name = "GRVN_MED_REL_FLG")
    private String medicalGrievance;
    
    @Column(name = "GRVN_MED_RCPT_DT")
    private Date dateReceivedByMedicalUnit;

    @Column(name = "GRVN_AMOUNT")
    private BigDecimal grievanceAmount;
    
    @Column(name = "inFormal_resl_flag")
    private String informalResolutionAccepted;
    
    @Column(name = "FRWD_TO_ICG_FLAG")
    private String forwardToIGC;
    
    @Column(name = "FRWD_TO_IGC_DATE")
    private Date dateForwardedToIGC;

	@ManyToOne
    @JoinColumn(name = "user_id_investigated_by", referencedColumnName = "USER_ID")
    private OmnetUser igcStaff;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMIT_NO", referencedColumnName = "commitNo", insertable = false, updatable = false)
    private InmateRegistration inmate;

    public InmateRegistration getInmate() {
        return inmate;
    }


	public void setInmate(InmateRegistration inmate) {
        this.inmate = inmate;
    }

    public Long getGrievanceSeqNum() {
        return grievanceSeqNum;
    }

    public void setGrievanceSeqNum(Long grievanceSeqNum) {
        this.grievanceSeqNum = grievanceSeqNum;
    }

    public Long getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Long groupNo) {
        this.groupNo = groupNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getGrievanceWrittenDate() {
        return grievanceWrittenDate;
    }

    public void setGrievanceWrittenDate(Date grievanceWrittenDate) {
        this.grievanceWrittenDate = grievanceWrittenDate;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public String getLevelOfResolution() {
        return levelOfResolution;
    }

    public void setLevelOfResolution(String levelOfResolution) {
        this.levelOfResolution = levelOfResolution;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getConfidential() {
        return confidential;
    }

    public void setConfidential(String confidential) {
        this.confidential = confidential;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGrievanceType() {
        return grievanceType;
    }

    public void setGrievanceType(String grievanceType) {
        this.grievanceType = grievanceType;
    }

    public Date getIgcReceivedDate() {
        return igcReceivedDate;
    }

    public void setIgcReceivedDate(Date igcReceivedDate) {
        this.igcReceivedDate = igcReceivedDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getCurrentHousing() {
        return currentHousing;
    }

    public void setCurrentHousing(String currentHousing) {
        this.currentHousing = currentHousing;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(String incidentTime) {
        this.incidentTime = incidentTime;
    }

    public OmnetUser getIgcStaff() {
        return igcStaff;
    }

    public void setIgcStaff(OmnetUser igcStaff) {
        this.igcStaff = igcStaff;
    }
    
    public String getDescriptionOfComplaint() {
		return descriptionOfComplaint;
	}

	public void setDescriptionOfComplaint(String descriptionOfComplaint) {
		this.descriptionOfComplaint = descriptionOfComplaint;
	}

	public String getRemedyRequested() {
		return remedyRequested;
	}

	public void setRemedyRequested(String remedyRequested) {
		this.remedyRequested = remedyRequested;
	}

	public String getTypeOfPerson() {
		return typeOfPerson;
	}

	public void setTypeOfPerson(String typeOfPerson) {
		this.typeOfPerson = typeOfPerson;
	}

	public String getMedicalGrievance() {
		return medicalGrievance;
	}

	public void setMedicalGrievance(String medicalGrievance) {
		this.medicalGrievance = medicalGrievance;
	}

	public Date getDateReceivedByMedicalUnit() {
		return dateReceivedByMedicalUnit;
	}

	public void setDateReceivedByMedicalUnit(Date dateReceivedByMedicalUnit) {
		this.dateReceivedByMedicalUnit = dateReceivedByMedicalUnit;
	}

	public BigDecimal getGrievanceAmount() {
		return grievanceAmount;
	}

	public void setGrievanceAmount(BigDecimal grievanceAmount) {
		this.grievanceAmount = grievanceAmount;
	}
	
	   public String getInformalResolutionAccepted() {
			return informalResolutionAccepted;
		}


		public void setInformalResolutionAccepted(String informalResolutionAccepted) {
			this.informalResolutionAccepted = informalResolutionAccepted;
		}


		public String getForwardToIGC() {
			return forwardToIGC;
		}


		public void setForwardToIGC(String forwardToIGC) {
			this.forwardToIGC = forwardToIGC;
		}


		public Date getDateForwardedToIGC() {
			return dateForwardedToIGC;
		}


		public void setDateForwardedToIGC(Date dateForwardedToIGC) {
			this.dateForwardedToIGC = dateForwardedToIGC;
		}

}

