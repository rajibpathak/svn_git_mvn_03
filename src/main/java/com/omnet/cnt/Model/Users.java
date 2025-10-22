package com.omnet.cnt.Model;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "omnet_users")
public class Users {
	
	@Id

	@Column(name="user_id", insertable=true, updatable=false)
	private String userId;
	private String userFirstName;
	private String userLastName;
	private String userMidName;
	private String userSuffixName;
	
	@Column(name="user_phone_1")
	private String userPhone1;
	
	@Column(name="user_phone_2")
	private String userPhone2;
	
	//private String userEmail;
	@Column(name="user_status")
	private String userStatus;
	
	private String instNum;
	private String primaryInstNum;
	
	@Column(name = "TITLE")
	private String jobTitle;
	
	@Column(name = "par_ofcr_num", insertable=false, updatable=false)
	private String parOfcrNum;
	
	private String supUserId;
	
	@Column(name = "max_num_sessions", insertable=false, updatable=false)
	private Integer maxNumSessions;
	
	private String initialPassword;
	private String currentPassword;
	
	
	private String encryptedPassword;
	
	private String lastLoggedDateTime;
	
	@Column(name="inserted_date_time", insertable=false, updatable=false)
	private String insertedDateTime;
	
	@Column(name = "INSERTED_USERID")
	private String insertedUserId;
	
	@Column(name="updated_date_time", insertable=false, updatable=false)
	private String updatedDateTime;
	
	@Column(name = "UPDATED_USERID")
	private String updatedUserId;
	
	private String userEmailAddress;

	@Temporal(TemporalType.DATE)
	private Date passwordExpiredDate;
	
	@Transient
	public String insFlag;


	public Users() {

	}
	
	public Users(String userId, String userFirstName, String userLastName, String userMidName,
			String userSuffixName) {
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userMidName = userMidName;
		this.userSuffixName = userSuffixName;
	}

	protected Users(String userId, String userFirstName, String userLastName, String userMidName,
			String userSuffixName, String userPhone1, String userPhone2, String userStatus, String instNum,
			String primaryInstNum, String jobTitle, String parOfcrNum, String supUserId, Integer maxNumSessions,
			String initialPassword, String currentPassword, String encryptedPassword, String lastLoggedDateTime,
			String insertedDateTime, String insertedUserId, String updatedDateTime, String updatedUserId,
			String userEmailAddress, Date passwordExpiredDate) {
		super();
		this.userId = userId;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userMidName = userMidName;
		this.userSuffixName = userSuffixName;
		this.userPhone1 = userPhone1;
		this.userPhone2 = userPhone2;
		this.userStatus = userStatus;
		this.instNum = instNum;
		this.primaryInstNum = primaryInstNum;
		this.jobTitle = jobTitle;
		this.parOfcrNum = parOfcrNum;
		this.supUserId = supUserId;
		this.maxNumSessions = maxNumSessions;
		this.initialPassword = initialPassword;
		this.currentPassword = currentPassword;
		this.encryptedPassword = encryptedPassword;
		this.lastLoggedDateTime = lastLoggedDateTime;
		this.insertedDateTime = insertedDateTime;
		this.insertedUserId = insertedUserId;
		this.updatedDateTime = updatedDateTime;
		this.updatedUserId = updatedUserId;
		this.userEmailAddress = userEmailAddress;
		this.passwordExpiredDate = passwordExpiredDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
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

	public String getUserPhone1() {
		return userPhone1;
	}

	public void setUserPhone1(String userPhone1) {
		this.userPhone1 = userPhone1;
	}

	public String getUserPhone2() {
		return userPhone2;
	}

	public void setUserPhone2(String userPhone2) {
		this.userPhone2 = userPhone2;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getInstNum() {
		return instNum;
	}

	public void setInstNum(String instNum) {
		this.instNum = instNum;
	}

	public String getPrimaryInstNum() {
		return primaryInstNum;
	}

	public void setPrimaryInstNum(String primaryInstNum) {
		this.primaryInstNum = primaryInstNum;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getParOfcrNum() {
		return parOfcrNum;
	}

	public void setParOfcrNum(String parOfcrNum) {
		this.parOfcrNum = parOfcrNum;
	}

	public String getSupUserId() {
		return supUserId;
	}

	public void setSupUserId(String supUserId) {
		this.supUserId = supUserId;
	}

	public Integer getMaxNumSessions() {
		return maxNumSessions;
	}

	public void setMaxNumSessions(Integer maxNumSessions) {
		this.maxNumSessions = maxNumSessions;
	}

	public String getInitialPassword() {
		return initialPassword;
	}

	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getLastLoggedDateTime() {
		return lastLoggedDateTime;
	}

	public void setLastLoggedDateTime(String lastLoggedDateTime) {
		this.lastLoggedDateTime = lastLoggedDateTime;
	}

	public String getInsertedDateTime() {
		return insertedDateTime;
	}

	public void setInsertedDateTime(String insertedDateTime) {
		this.insertedDateTime = insertedDateTime;
	}

	public String getInsertedUserId() {
		return insertedUserId;
	}

	public void setInsertedUserId(String insertedUserId) {
		this.insertedUserId = insertedUserId;
	}

	public String getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public String getUserEmailAddress() {
		return userEmailAddress;
	}

	public void setUserEmailAddress(String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}

	public Date getPasswordExpiredDate() {
		return passwordExpiredDate;
	}

	public void setPasswordExpiredDate(Date passwordExpiredDate) {
		this.passwordExpiredDate = passwordExpiredDate;
	}
	
	public String getInsFlag() {
		return insFlag;
	}

	public void setInsFlag(String insFlag) {
		this.insFlag = insFlag;
	}
	
}
