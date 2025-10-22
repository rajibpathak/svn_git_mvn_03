/**
 * Document: ReasonMaster.java
 * Author: Jamal Abraar
 * Date Created: 19-Jun-2024
 * Last Updated: 
 */

package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name= "REASON_MST")
public class ReasonMaster {
	
	@Id
	@Column(name= "RESN_CD")
	private String reasonCode;
	
	@Column(name= "RESN_DESC")
	private String reasonDescription;
	
	@Column(name="SUB_TP_CD_RESN")
	private String subTpCdResn;
	
	@Column(name= "STATUS")
	private String status;

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDescription() {
		return reasonDescription;
	}

	public void setReasonDescription(String reasonDescription) {
		this.reasonDescription = reasonDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	public String getSubTpCdResn() {
		return subTpCdResn;
	}

	public void setSubTpCdResn(String subTpCdResn) {
		this.subTpCdResn = subTpCdResn;
	}

	public ReasonMaster(String reasonCode, String reasonDescription, String subTpCdResn, String status) {
		super();
		this.reasonCode = reasonCode;
		this.reasonDescription = reasonDescription;
		this.subTpCdResn = subTpCdResn;
		this.status = status;
	}

	public ReasonMaster() {
		super();
	}		

}
