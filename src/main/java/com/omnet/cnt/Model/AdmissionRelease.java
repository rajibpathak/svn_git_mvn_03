package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OUTER_CONTROL_LOG")
public class AdmissionRelease {

	@Id
    @Column(name = "commit_no")
    private String commitNo;
	
    @Column(name = "ADMSS_RELEASE_CODE")
    private String admitCode;

    @Column(name = "immt_tp_cd")
    private String offenderType;

    @Column(name = "entry_exit_date")
    private Date admissionReleaseDate;

    @Column(name = "entry_exit_time")
    private String admissionReleaseTime;

	@Column(name= "INST_NUM")
	private String instNum;

    @ManyToOne
    @JoinColumn(name = "inst_num", referencedColumnName = "inst_num", insertable = false, updatable = false)
    private InstLocation instLocation;

    @ManyToOne
    @JoinColumn(name = "commit_no", referencedColumnName = "commitNo", insertable = false, updatable = false)
    private InmateRegistration inmate;

  

    public String getAdmitCode() {
        return admitCode;
    }

    public void setAdmitCode(String admitCode) {
        this.admitCode = admitCode;
    }

    public String getOffenderType() {
        return offenderType;
    }

    public void setOffenderType(String offenderType) {
        this.offenderType = offenderType;
    }

    public Date getAdmissionReleaseDate() {
        return admissionReleaseDate;
    }

    public void setAdmissionReleaseDate(Date admissionReleaseDate) {
        this.admissionReleaseDate = admissionReleaseDate;
    }

    public String getAdmissionReleaseTime() {
        return admissionReleaseTime;
    }

    public void setAdmissionReleaseTime(String admissionReleaseTime) {
        this.admissionReleaseTime = admissionReleaseTime;
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

	public InstLocation getInstLocation() {
        return instLocation;
    }

    public void setInstLocation(InstLocation instLocation) {
        this.instLocation = instLocation;
    }

    public InmateRegistration getInmate() {
        return inmate;
    }

    public void setInmate(InmateRegistration inmate) {
        this.inmate = inmate;
    }
}