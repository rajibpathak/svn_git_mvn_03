package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GRIEVANCE_LSME_BCME_DTL")
public class GrievanceLsmeBcmeDetail {

    @Id
    @Column(name = "LSME_BCME_DTL_SEQ_NUM", nullable = false)
    private Long lsmeBcmeDetailSeqNum;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private OmnetUser user;

    @Column(name = "LSME_FLAG", length = 1)
    private String lsmeFlag;

    @Column(name = "BCME_FLAG", length = 1)
    private String bcmeFlag;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "INST_NUM", length = 10)
    private String facility;

    @Column(name = "GRIEVANCE_TYPE", length = 10)
    private String grievanceType;

    @Column(name = "STATUS", length = 1)
    private String status;

    @Column(name = "INSERTED_USERID", nullable = false, length = 8)
    private String insertedUserId;

    @Column(name = "INSERTED_DATE_TIME", nullable = false)
    private Date insertedDateTime;

    @Column(name = "UPDATED_USERID", nullable = false, length = 8)
    private String updatedUserId;

    @Column(name = "UPDATED_DATE_TIME", nullable = false)
    private Date updatedDateTime;

    @Column(name = "SESSION_ID", nullable = false)
    private Long sessionId;

    @Column(name = "TERMINAL", nullable = false, length = 30)
    private String terminal;

    // Relationships with other entities
    @ManyToOne
    @JoinColumn(name = "GRIEVANCE_SEQ_NUM", referencedColumnName = "GRIEVANCE_SEQ_NUM")
    private InmateGrievance inmateGrievance;

    @ManyToOne
    @JoinColumn(name = "GRVN_INVST_SEQ_NO", referencedColumnName = "GRVN_INVST_SEQ_NO")
    private GrievanceInvestigationDetail grievanceInvestigationDetail;

    // Getters and Setters
    public Long getLsmeBcmeDetailSeqNum() {
        return lsmeBcmeDetailSeqNum;
    }

    public void setLsmeBcmeDetailSeqNum(Long lsmeBcmeDetailSeqNum) {
        this.lsmeBcmeDetailSeqNum = lsmeBcmeDetailSeqNum;
    }

    public OmnetUser getUser() {
        return user;
    }

    public void setUser(OmnetUser user) {
        this.user = user;
    }

    public String getLsmeFlag() {
        return lsmeFlag;
    }

    public void setLsmeFlag(String lsmeFlag) {
        this.lsmeFlag = lsmeFlag;
    }

    public String getBcmeFlag() {
        return bcmeFlag;
    }

    public void setBcmeFlag(String bcmeFlag) {
        this.bcmeFlag = bcmeFlag;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getGrievanceType() {
        return grievanceType;
    }

    public void setGrievanceType(String grievanceType) {
        this.grievanceType = grievanceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInsertedUserId() {
        return insertedUserId;
    }

    public void setInsertedUserId(String insertedUserId) {
        this.insertedUserId = insertedUserId;
    }

    public Date getInsertedDateTime() {
        return insertedDateTime;
    }

    public void setInsertedDateTime(Date insertedDateTime) {
        this.insertedDateTime = insertedDateTime;
    }

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public Date getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Date updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public InmateGrievance getInmateGrievance() {
        return inmateGrievance;
    }

    public void setInmateGrievance(InmateGrievance inmateGrievance) {
        this.inmateGrievance = inmateGrievance;
    }

    public GrievanceInvestigationDetail getGrievanceInvestigationDetail() {
        return grievanceInvestigationDetail;
    }

    public void setGrievanceInvestigationDetail(GrievanceInvestigationDetail grievanceInvestigationDetail) {
        this.grievanceInvestigationDetail = grievanceInvestigationDetail;
    }
}