package com.omnet.cnt.Model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="OMNET_USER_PASSWORDRESETOTP")
public class ForgetPassword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer OupSeqNum;
    private String Otp;
	private String UserId; 
	private String Status;
    private Date OtpExpiryDate;
	 private String InsertedUserId; 
    private Date InsertedDateTime;
	 private String UpdatedUserId; 
    private  Date  UpdatedDateTime;
    private  String Terminal;
    private Integer Sessionid;
	protected ForgetPassword(Integer oupSeqNum, String otp, String userId, String status, Date otpExpiryDate,
			String insertedUserId, Date insertedDateTime, String updatedUserId, Date updatedDateTime, String terminal,
			Integer sessionid) {
		super();
		OupSeqNum = oupSeqNum;
		Otp = otp;
		UserId = userId;
		Status = status;
		OtpExpiryDate = otpExpiryDate;
		InsertedUserId = insertedUserId;
		InsertedDateTime = insertedDateTime;
		UpdatedUserId = updatedUserId;
		UpdatedDateTime = updatedDateTime;
		Terminal = terminal;
		Sessionid = sessionid;
	}
		public ForgetPassword() {}
	public Integer getOuoSeqNum() {
		return OupSeqNum;
	}
	public void setOuoSeqNum(Integer oupSeqNum) {
		OupSeqNum = oupSeqNum;
	}
	public String getOtp() {
		return Otp;
	}
	public void setOtp(String otp) {
		Otp = otp;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Date getOtpExpiryDate() {
		return OtpExpiryDate;
	}
	public void setOtpExpiryDate(Date otpExpiryDate) {
		OtpExpiryDate = otpExpiryDate;
	}
	public String getInsertedUserId() {
		return InsertedUserId;
	}
	public void setInsertedUserId(String insertedUserId) {
		InsertedUserId = insertedUserId;
	}
	public Date getInsertedDateTime() {
		return InsertedDateTime;
	}
	public void setInsertedDateTime(Date insertedDateTime) {
		InsertedDateTime = insertedDateTime;
	}
	public String getUpdatedUserId() {
		return UpdatedUserId;
	}
	public void setUpdatedUserId(String updatedUserId) {
		UpdatedUserId = updatedUserId;
	}
	public Date getUpdatedDateTime() {
		return UpdatedDateTime;
	}
	public void setUpdatedDateTime(Date updatedDateTime) {
		UpdatedDateTime = updatedDateTime;
	}
	public String getTerminal() {
		return Terminal;
	}
	public void setTerminal(String terminal) {
		Terminal = terminal;
	}
	public Integer getSessionid() {
		return Sessionid;
	}
	public void setSessionid(Integer sessionid) {
		Sessionid = sessionid;
	}
	
}
