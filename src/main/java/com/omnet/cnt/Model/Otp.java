
package com.omnet.cnt.Model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "OMNET_USER_OTP")
@SequenceGenerator(name = "ouo_seq_generator", sequenceName = "OUO_SEQ", allocationSize = 1)
public class Otp {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ouo_seq_generator")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer ouoSeqNum;
	
	private String otp;
	
	private String userId;
	
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OTP_EXPIRY_DATE")
	private Date otpExpiryDate;
	
	private String insertedUserId;
	
	private Date insertedDateTime;
	
	private String updatedUserId;
	
	private Date updatedDateTime;

	public Integer getOuoSeqNum() {
		return ouoSeqNum;
	}

	public void setOuoSeqNum(Integer ouoSeqNum) {
		this.ouoSeqNum = ouoSeqNum;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOtpExpiryDate() {
		return otpExpiryDate;
	}

	public void setOtpExpiryDate(Date otpExpiryDate) {
		this.otpExpiryDate = otpExpiryDate;
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

	public Otp(Integer ouoSeqNum, String otp, String userId, String status, Date otpExpiryDate, String insertedUserId,
			Date insertedDateTime, String updatedUserId, Date updatedDateTime) {
		super();
		this.ouoSeqNum = ouoSeqNum;
		this.otp = otp;
		this.userId = userId;
		this.status = status;
		this.otpExpiryDate = otpExpiryDate;
		this.insertedUserId = insertedUserId;
		this.insertedDateTime = insertedDateTime;
		this.updatedUserId = updatedUserId;
		this.updatedDateTime = updatedDateTime;
	}

	public Otp() {
		super();
	}
	
}  
    
	   