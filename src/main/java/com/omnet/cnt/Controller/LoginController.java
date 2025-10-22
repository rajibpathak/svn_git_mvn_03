/*
Document   : Login Rest Controller
Author     : Sunandhaa,Jamal Abraar
last update: 23/04/2024
*/

package com.omnet.cnt.Controller;

import java.text.ParseException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.omnet.cnt.Model.ForgetPassword;
import com.omnet.cnt.Model.UserData;
import com.omnet.cnt.Service.ForgetPasswordService;
import com.omnet.cnt.Service.LoginService;
import com.omnet.cnt.Service.LoginUserDetailsService;
import com.omnet.cnt.Service.OtpService;
import com.omnet.cnt.classes.TokenManager;

@RestController
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginUserDetailsService userDetailsService;
	@Autowired
	private OtpService otpService;
	@Autowired
	private ForgetPasswordService forgetpasswordservice;
	@Autowired
	private TokenManager tokenManager;

	@PostMapping("/LoginApi/Authenticate")
	public ResponseEntity<String> authenticateUser(@RequestBody UserData userData) {
		try {
			String userId = userData.getUserId().toUpperCase();
			String status="A";			
			UserDetails userDetails = userDetailsService.loadUserByUsername(userId);			
			String encryptedPassword = loginService.validatePassword(userId, userData.getCurrentPassword());		
			if (encryptedPassword != null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
			}
			if (!userDetails.isAccountNonExpired()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Inactive user");
			}
			if (!userDetails.isCredentialsNonExpired()) {
				String token = UUID.randomUUID().toString();				
				tokenManager.storePasswordToken(userId, token);
				String passwordChangeURL = "/OmnetTest/PasswordChange-" + token;
				return ResponseEntity.ok(passwordChangeURL);
			}
			String token = UUID.randomUUID().toString();
			otpService.storeToken(userId, token);
			String loginValidationURL = "/OmnetTest/LoginValidation-" + token;		
			 otpService.invalidateOtpsByUserId(userId,status); 
			otpService.generateAndSendOtp(userId);
			
			return ResponseEntity.ok(loginValidationURL);
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("Invalid UserId entered, Please verify and re-enter");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}

	@PostMapping("/LoginApi/validateOTP")
	public ResponseEntity<String> validateOTPForget(@RequestParam("userOTP") String userOTP,
			@RequestParam("token") String token) {
		String userId = otpService.getuserIdForToken(token);
		if (userId != null && userOTP != null) {
			int updateResult = otpService.updateOtpStatusAndUserInfo(userId, userOTP);
			if (updateResult > 0) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				Map<String, String> additionalDetails = new HashMap<>();
				additionalDetails.put("userId", userId);
				((UsernamePasswordAuthenticationToken) authentication).setDetails(additionalDetails);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				otpService.invalidateToken(token);				
				return ResponseEntity.ok("OTP is valid");
			} else if (updateResult == 0) {
				return ResponseEntity.ok("OTP is Invalid");
			} else {
				return ResponseEntity.badRequest().body("Error validating OTP");
			}
		} else {
			return ResponseEntity.badRequest().body("User not found");
		}
	}

	@PostMapping("/LoginApi/ForgotPassword")
	public ResponseEntity<String> initiateForgetPassword(@RequestParam("userId") String userId) {
		if (userId == null) {
			return ResponseEntity.badRequest().body("Please enter a username");
		} else {
			String retrievedUserId = loginService.getUserIdByUsername(userId);
			String status ="A";
			if (retrievedUserId != null) {
				forgetpasswordservice.invalidateOtpsByUserIdforforgetpassword(retrievedUserId,status);
				forgetpasswordservice.generateAndSendOtp(retrievedUserId);
				return ResponseEntity.ok(retrievedUserId);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
		}
	}

	
	  @RequestMapping(value = "/LoginApi/regenerateOTP", method =RequestMethod.GET) 
	  public String regenerateOTP(@RequestParam String userId) {
		  String userIdre = userId.toUpperCase();
			String status="A";	
	 otpService.invalidateOtpsByUserId(userIdre,status);
	  otpService.generateAndSendOtp(userId);
	      return "OTP regenerated";
	  }
	

	@RequestMapping(value = "LoginApi/regenerate", method = RequestMethod.GET)
	public String regenerate(@RequestParam String userId) {
		 String userIdre = userId.toUpperCase();
			String status="A";	
		forgetpasswordservice.invalidateOtpsByUserIdforforgetpassword(userIdre,status);
		forgetpasswordservice.generateAndSendOtp(userId);

		return "OTP regenerated";
	}

	@GetMapping("LoginApi/checkvalidateotp")
	public ResponseEntity<String> validateOTPForgetpassword(@RequestParam("userOTP") String userOTP,

			@RequestParam("userId") String userId) {
		forgetpasswordservice.ExpiredOtps();

		if (userId != null && userOTP != null) {
			List<ForgetPassword> forgetList = forgetpasswordservice.findOtpByUserIdsAndOtps(userId, userOTP);
			String status = forgetpasswordservice.findStatusByUserIdsAndOtps(userId, userOTP);
			if ("I".equals(status)) {
				return ResponseEntity.ok("OTP is Expired");
			} else if (forgetList.isEmpty()) {
				return ResponseEntity.ok("Invalid OTP");
			} else {
				forgetList.get(0).setStatus("I");
				return ResponseEntity.ok("OTP is Valid");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
	}

	@PostMapping("/LoginApi/savePassword")
	public String savePassword(@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword, @RequestParam("userId") String userId)
			throws ParseException {	
		if (newPassword.equals(confirmPassword)) {
			String encryptedPassword = loginService.getHashedPassword(newPassword);			
			loginService.updateEncryptedPassword(userId, encryptedPassword);
			/* loginService.updateCurrentPassword(userId, newPassword); */
			loginService.updatePasswordExpiryDate(userId);
			return "Password saved successfully";
		} else {
			return "Passwords do not match";
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok("Logged out successfully");
	}

	public String getUsernamesecurity() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return authentication.getName();
		}
		return null;
	}

}