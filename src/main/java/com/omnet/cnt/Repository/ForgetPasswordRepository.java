package com.omnet.cnt.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.omnet.cnt.Model.ForgetPassword;


public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Integer> {
	@Query(nativeQuery = true, value = "SELECT * FROM OMNET_USER_PASSWORDRESETOTP u WHERE UPPER(u.USER_ID) = UPPER(:userId) AND u.OTP = :userOTP")
	List<ForgetPassword> findOtpByUserIdsAndOtps(String userId, String userOTP);

	@Query(nativeQuery = true, value = "SELECT u.Status FROM OMNET_USER_PASSWORDRESETOTP u WHERE UPPER(u.USER_ID) = UPPER(:userId) AND u.OTP = :userOTP")
	String findStatusByUserIdsAndOtps(String userId, String userOTP);

	  @Query(value = "SELECT * FROM OMNET_USER_PASSWORDRESETOTP u WHERE u.status = :status", nativeQuery = true)
		 List<ForgetPassword> findOtpsByStatus(@Param("status") String status);
	  
	  @Query(value = "SELECT * FROM OMNET_USER_PASSWORDRESETOTP u WHERE u.user_id = :userId and  u.status = :status", nativeQuery = true)
	    List<ForgetPassword> findOtpsByStatusanduseridforget(@Param("userId") String userId,@Param("status") String status);
	  
}
