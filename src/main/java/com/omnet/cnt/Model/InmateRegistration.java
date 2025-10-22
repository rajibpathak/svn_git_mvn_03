package com.omnet.cnt.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "INMATE")
public class InmateRegistration {

    @Id
    @Column(name = "sbi_mst_sbi_no")
    private String sbiNo;
    
    private String commitNo;
    
    private String commitLname;
    private String commitFname;
    private String commitMname;
    
    private String commitSname;

    @Column(name = "primary_dob")
    private Date dob;

    @Column(name = "Sex")
    private String sex;

    @Column(name = "race_code")
    private String race;

    @OneToOne(mappedBy = "inmate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private InmateAddress inmateAddress;
    
    @OneToMany(mappedBy = "inmate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdmissionRelease> admissionReleases;  


    public List<AdmissionRelease> getAdmissionReleases() {
        return admissionReleases;
    }

    public void setAdmissionReleases(List<AdmissionRelease> admissionReleases) {
        this.admissionReleases = admissionReleases;
    }
    
    @OneToMany(mappedBy = "inmate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InmateGrievance> grievances;

 
    public List<InmateGrievance> getGrievances() {
        return grievances;
    }

    public void setGrievances(List<InmateGrievance> grievances) {
        this.grievances = grievances;
    }

	public String getSbiNo() {
		return sbiNo;
	}

	public void setSbiNo(String sbiNo) {
		this.sbiNo = sbiNo;
	}

	public String getCommitLname() {
		return commitLname;
	}

	public void setCommitLname(String commitLname) {
		this.commitLname = commitLname;
	}

	public String getCommitFname() {
		return commitFname;
	}

	public void setCommitFname(String commitFname) {
		this.commitFname = commitFname;
	}

	public String getCommitMname() {
		return commitMname;
	}

	public void setCommitMname(String commitMname) {
		this.commitMname = commitMname;
	}

	public String getCommitSname() {
		return commitSname;
	}

	public void setCommitSname(String commitSname) {
		this.commitSname = commitSname;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public InmateAddress getInmateAddress() {
		return inmateAddress;
	}

	public void setInmateAddress(InmateAddress inmateAddress) {
		this.inmateAddress = inmateAddress;
	}

	public String getCommitNo() {
		return commitNo;
	}

	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	
    
}
