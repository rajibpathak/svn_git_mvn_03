/*
Document   : Contact Master
Author     : Sunandhaa,Jamal Abraar
last update: 01/04/2024
*/

package com.omnet.cnt.Repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM OMNET_USER_OTP u WHERE u.user_id = :userId  AND u.OTP = :userOTP")
	 List<Otp> findOtpByUserIdAndOtp(String userId, String userOTP);
	@Query(nativeQuery = true, value = "SELECT u.Status FROM OMNET_USER_OTP u WHERE u.user_id = :userId AND u.OTP = :userOTP")
	String findStatusByUserIdAndOtp(String userId, String userOTP);
	  @Query(value = "SELECT * FROM OMNET_USER_OTP u WHERE u.status = :status", nativeQuery = true)
	    List<Otp> findOtpsByStatus(@Param("status") String status);
	
	  @Query(value = "SELECT * FROM OMNET_USER_OTP  WHERE UPPER(user_id) = UPPER(:userId) and  status = :status", nativeQuery =true)
	  List<Otp> findOtpsByStatusanduserid( @Param("userId") String userId,@Param("status") String status);

	  
	  @Modifying
	  @Query("UPDATE Otp o SET o.status = CASE WHEN o.status = 'A' THEN 'I' ELSE o.status END, " +
	          "o.updatedUserId = CASE WHEN o.status = 'A' THEN :userId ELSE o.updatedUserId END, " +
	          "o.updatedDateTime = CASE WHEN o.status = 'A' THEN CURRENT_TIMESTAMP ELSE o.updatedDateTime END " +
	          "WHERE o.userId = :userId AND o.otp = :otp AND o.status = 'A'")
	  int updateOtpStatusAndUserInfo(@Param("userId") String userId, @Param("otp") String otp);
	  
	  
	  @Modifying
	  @Transactional
	  @Query("UPDATE Otp o SET o.status = 'I', o.updatedDateTime = CURRENT_TIMESTAMP " +
	         "WHERE o.status = 'A' AND o.otpExpiryDate < CURRENT_TIMESTAMP")
	  int invalidateExpiredOtps();

	}
