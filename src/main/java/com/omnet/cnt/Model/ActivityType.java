	/**
	 * Document: ActivityType.java
	 * Author: Jamal Abraar
	 * Date Created: 20-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "ACTIVITY_TYPE_RT")
public class ActivityType {
	
	@Id
	@Column(name = "ACTIVITY_TYPE_CODE")
	private String activityTypeCode;
	
	@Column(name = "ACTIVITY_TYPE_DESC")
	private String activityTypeDesc;
	
	@Column(name = "STATUS")
	private String status;

	public String getActivityTypeCode() {
		return activityTypeCode;
	}

	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}

	public String getActivityTypeDesc() {
		return activityTypeDesc;
	}

	public void setActivityTypeDesc(String activityTypeDesc) {
		this.activityTypeDesc = activityTypeDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ActivityType(String activityTypeCode, String activityTypeDesc, String status) {
		super();
		this.activityTypeCode = activityTypeCode;
		this.activityTypeDesc = activityTypeDesc;
		this.status = status;
	}

	public ActivityType() {
		super();
	}
		
}
