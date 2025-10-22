package com.omnet.cnt.Model;

import java.sql.Date;

public class CSMViewLocation {
	private String commitNo;
    private String location;
    private Date arrivalDate;
    private String arrivalType;
    private Date departDate;
    private String departType;
    
	public CSMViewLocation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CSMViewLocation(String commitNo, String location, Date arrivalDate, String arrivalType, Date departDate,
			String departType) {
		super();
		this.commitNo = commitNo;
		this.location = location;
		this.arrivalDate = arrivalDate;
		this.arrivalType = arrivalType;
		this.departDate = departDate;
		this.departType = departType;
	}

	public String getCommitNo() {
		return commitNo;
	}
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getArrivalType() {
		return arrivalType;
	}
	public void setArrivalType(String arrivalType) {
		this.arrivalType = arrivalType;
	}
	public Date getDepartDate() {
		return departDate;
	}
	public void setDepartDate(Date departDate) {
		this.departDate = departDate;
	}
	public String getDepartType() {
		return departType;
	}
	public void setDepartType(String departType) {
		this.departType = departType;
	}
}
