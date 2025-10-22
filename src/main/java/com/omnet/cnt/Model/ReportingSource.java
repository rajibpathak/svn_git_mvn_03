package com.omnet.cnt.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "reporting_source_rt")

public class ReportingSource {
	
	@Id
	private String reportingSourceCode;
	private String reportingDdesc;
	private String status;
	public String getReportingSourceCode() {
		return reportingSourceCode;
	}
	public void setReportingSourceCode(String reportingSourceCode) {
		this.reportingSourceCode = reportingSourceCode;
	}
	public String getReportingDdesc() {
		return reportingDdesc;
	}
	public void setReportingDdesc(String reportingDdesc) {
		this.reportingDdesc = reportingDdesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	


}
