	/**
	 * Document: IndividualOffMov.java
	 * Author: Jamal Abraar
	 * Date Created: 27-Aug-2024
	 * Last Updated: 
	 */
package com.omnet.cnt.classes;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndividualOffMov {
	
	private String sbiNumber;
	private String commitNo;
	private String instNum;
	private Timestamp  dateTimeOfDep;
	private Timestamp  dateTimeOfArr;
	private String countLocCodeFrom;
	private String countLocCodeTo;
	private String comments;
	private String activityTypeCode;
	private String rgHousRelationship;
	private String arrivedCheck;
	
	public static class ScheduledActivity{
		
		private Timestamp activityStartDate;
		private String activityStartTime;
		private Timestamp activityEndDate;
		private String activityEndTime;
		private String activityDesc;
		private String externalFlag;
		private String destination;
		private String activityTypeDesc;
		private String transportType;
		private String transportDetail;
		private String transportBy;
		
		public Timestamp getActivityStartDate() {
			return activityStartDate;
		}
		
		public void setActivityStartDate(Timestamp activityStartDate) {
			this.activityStartDate = activityStartDate;
		}
		
		public String getActivityStartTime() {
			return activityStartTime;
		}
		
		public void setActivityStartTime(String activityStartTime) {
			this.activityStartTime = activityStartTime;
		}
		
		public Timestamp getActivityEndDate() {
			return activityEndDate;
		}
		
		public void setActivityEndDate(Timestamp activityEndDate) {
			this.activityEndDate = activityEndDate;
		}
		
		public String getActivityEndTime() {
			return activityEndTime;
		}
		
		public void setActivityEndTime(String activityEndTime) {
			this.activityEndTime = activityEndTime;
		}
		
		public String getActivityDesc() {
			return activityDesc;
		}
		
		public void setActivityDesc(String activityDesc) {
			this.activityDesc = activityDesc;
		}
		
		public String getExternalFlag() {
			return externalFlag;
		}
		
		public void setExternalFlag(String externalFlag) {
			this.externalFlag = externalFlag;
		}
		
		public String getDestination() {
			return destination;
		}
		
		public void setDestination(String destination) {
			this.destination = destination;
		}
		
		public String getActivityTypeDesc() {
			return activityTypeDesc;
		}
		
		public void setActivityTypeDesc(String activityTypeDesc) {
			this.activityTypeDesc = activityTypeDesc;
		}
		
		public String getTransportType() {
			return transportType;
		}
		
		public void setTransportType(String transportType) {
			this.transportType = transportType;
		}
		
		public String getTransportDetail() {
			return transportDetail;
		}
		
		public void setTransportDetail(String transportDetail) {
			this.transportDetail = transportDetail;
		}
		
		public String getTransportBy() {
			return transportBy;
		}
		
		public void setTransportBy(String transportBy) {
			this.transportBy = transportBy;
		}
		
		@JsonInclude(JsonInclude.Include.ALWAYS)
		public static ScheduledActivity mapScheduledActivity(Map<String, Object> row) {
		    ScheduledActivity scheduledActivity = new ScheduledActivity();

		    scheduledActivity.setActivityStartDate((Timestamp) row.get("ACTIVITY_START_DATE"));
		    scheduledActivity.setActivityStartTime((String) row.get("ACTIVITY_START_TIME"));
		    scheduledActivity.setActivityEndDate((Timestamp) row.get("ACTIVITY_END_DATE"));
		    scheduledActivity.setActivityEndTime((String) row.get("ACTIVITY_END_TIME"));
		    scheduledActivity.setActivityTypeDesc((String) row.get("ACTIVITY_TYPE_DESC"));
		    scheduledActivity.setExternalFlag((String) row.get("INTERNAL_EXTERNAL_FLAG"));
		    scheduledActivity.setActivityDesc((String) row.get("ACTIVITY_DESC"));
		    scheduledActivity.setDestination((String) row.get("DESTINATION"));
		    scheduledActivity.setTransportType((String) row.get("TRANSPORT_TYPE"));
		    scheduledActivity.setTransportDetail((String) row.get("TRANSPORT_DETAIL"));
		    scheduledActivity.setTransportBy((String) row.get("TRANSPORT_BY"));

		    return scheduledActivity;
		}
		
	}
	
	
	
	@JsonProperty("activityTypeCode")
	public String getActivityTypeCode() {
		return activityTypeCode;
	}
	
	@JsonProperty("activityTypeCode")
	public void setActivityTypeCode(String activityTypeCode) {
		this.activityTypeCode = activityTypeCode;
	}
	
	@JsonProperty("commitNo")
	public String getCommitNo() {
		return commitNo;
	}
	
	@JsonProperty("commitNo")
	public void setCommitNo(String commitNo) {
		this.commitNo = commitNo;
	}

	@JsonProperty("instNum")
	public String getInstNum() {
		return instNum;
	}

	@JsonProperty("instNum")
	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	@JsonProperty("dateTimeOfDep")
	public Timestamp getDateTimeOfDep() {
		return dateTimeOfDep;
	}

	@JsonProperty("dateTimeOfDep")
	public void setDateTimeOfDep(Timestamp dateTimeOfDep) {
		this.dateTimeOfDep = dateTimeOfDep;
	}

	@JsonProperty("dateTimeOfArr")
	public Timestamp getDateTimeOfArr() {
		return dateTimeOfArr;
	}

	@JsonProperty("dateTimeOfArr")
	public void setDateTimeOfArr(Timestamp dateTimeOfArr) {
		this.dateTimeOfArr = dateTimeOfArr;
	}

	@JsonProperty("countLocCodeFrom")
	public String getCountLocCodeFrom() {
		return countLocCodeFrom;
	}

	@JsonProperty("countLocCodeFrom")
	public void setCountLocCodeFrom(String countLocCodeFrom) {
		this.countLocCodeFrom = countLocCodeFrom;
	}

	@JsonProperty("countLocCodeTo")
	public String getCountLocCodeTo() {
		return countLocCodeTo;
	}

	@JsonProperty("countLocCodeTo")
	public void setCountLocCodeTo(String countLocCodeTo) {
		this.countLocCodeTo = countLocCodeTo;
	}

	@JsonProperty("comments")
	public String getComments() {
		return comments;
	}

	@JsonProperty("comments")
	public void setComments(String comments) {
		this.comments = comments;
	}

	@JsonProperty("rgHousRelationship")
	public String getRgHousRelationship() {
		return rgHousRelationship;
	}

	@JsonProperty("rgHousRelationship")
	public void setRgHousRelationship(String rgHousRelationship) {
		this.rgHousRelationship = rgHousRelationship;
	}
	
    public String getArrivedCheck() {
		return arrivedCheck;
	}

	public void setArrivedCheck(String arrivedCheck) {
		this.arrivedCheck = arrivedCheck;
	}
	
	public String getSbiNumber() {
		return sbiNumber;
	}

	public void setSbiNumber(String sbiNumber) {
		this.sbiNumber = sbiNumber;
	}

	@JsonInclude(JsonInclude.Include.ALWAYS)
    public static IndividualOffMov movDetailQuery(Map<String, Object> row) {
        IndividualOffMov movDetailQueryData = new IndividualOffMov();

        movDetailQueryData.setCommitNo((String) row.get("COMMIT_NO"));
        movDetailQueryData.setInstNum((String) row.get("INST_NUM"));
        movDetailQueryData.setDateTimeOfDep((Timestamp) row.get("DATE_TIME_OF_DEP"));
        movDetailQueryData.setDateTimeOfArr((Timestamp) row.get("DATE_TIME_OF_ARR"));
        movDetailQueryData.setCountLocCodeFrom((String) row.get("COUNT_LOC_CODE_FROM"));
        movDetailQueryData.setCountLocCodeTo((String) row.get("COUNT_LOC_CODE_TO"));
        movDetailQueryData.setComments((String) row.get("COMMENTS"));
        movDetailQueryData.setActivityTypeCode((String) row.get("ACTIVITY_TYPE_CODE"));
        movDetailQueryData.setRgHousRelationship((String) row.get("RG_HOU_REL"));

        return movDetailQueryData;
    }

	@Override
	public String toString() {
		return "IndividualOffMov [commitNo=" + commitNo + ", instNum=" + instNum + ", dateTimeOfDep=" + dateTimeOfDep
				+ ", dateTimeOfArr=" + dateTimeOfArr + ", countLocCodeFrom=" + countLocCodeFrom + ", countLocCodeTo="
				+ countLocCodeTo + ", comments=" + comments + ", activityTypeCode=" + activityTypeCode
				+ ", rgHousRelationship=" + rgHousRelationship + "]";
	}
    
    
}

