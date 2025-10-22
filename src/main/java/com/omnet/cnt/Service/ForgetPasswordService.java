package com.omnet.cnt.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.omnet.cnt.Repository.UserRepository;
import com.omnet.cnt.Model.ForgetPassword;
import com.omnet.cnt.Model.Otp;
import com.omnet.cnt.Repository.ForgetPasswordRepository;

@Service
public class ForgetPasswordService {

	private JavaMailSender javaMailSender;
	private UserRepository userRepository;
	private ForgetPasswordRepository ForgetPasswordRepository;

	@Autowired
	public ForgetPasswordService(JavaMailSender javaMailSender, UserRepository userRepository,
			ForgetPasswordRepository forgetpasswordRepository) {
		this.javaMailSender = javaMailSender;
		this.userRepository = userRepository;
		this.ForgetPasswordRepository = forgetpasswordRepository;
	}

	@Autowired
	private OtpService otpService;

	public void generateAndSendOtp(String userId) {
		System.out.println("befotre");  
	    String userEmail = userRepository.findEmailByUserId(userId);
		System.out.println("after");
	    System.out.println("User not found or email address not available."+userEmail);
	    if (userEmail != null && !userEmail.trim().isEmpty()) {
	        String otp =otpService. generateOTP();
	        saveOtpToDatabase(userEmail, userId, otp);
	        sendOtpByEmail(userEmail, otp,userId);
	    } else {
	        System.out.println("User not found or email address not available.");
	    }
	}

	private void saveOtpToDatabase(String userEmail, String userId, String otp) {
		ForgetPassword entity = new ForgetPassword();
		entity.setSessionid(107821196);
		entity.setOtp(otp);
		Date currentDate = new Date();
		entity.setInsertedDateTime(currentDate);
		entity.setInsertedUserId(userId);
		entity.setUpdatedUserId(userId); 
		entity.setUpdatedDateTime(currentDate);
		entity.setUserId(userId);
		entity.setStatus("A");
		Date ExpiryDate = new Date(currentDate.getTime() + 1800000);
		entity.setOtpExpiryDate(ExpiryDate);
		ForgetPasswordRepository.save(entity);
	}

	public void saveOrUpdate(ForgetPassword entity) {
		ForgetPasswordRepository.save(entity);
	}

	private void sendOtpByEmail(String userEmail, String otp,String userId) {
		Optional<String> optionalName = userRepository.findConcatenatedNameByUserId(userId);
		String fullName = optionalName.orElse("");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(userEmail);
		message.setSubject("Password Reset Authentication Code");
		message.setText("Dear " + fullName + ",\r\n"
				+ "\r\n"
				+ " \r\n"
				+ "\r\n"
				+ "Please find the below authentication code to reset your password.\r\n"
				+ "\r\n"
		        + "Your OTP: " + otp + "\r\n"
				+ "\r\n"
			    + "Note: OTP will expire after every single successful attempt or for every 30 minutes.\r\n"
	            + "\r\n"
	            + "***This is an automated email, please do not reply to this message. This message is for the designated recipient only and may contain privileged, proprietary, or otherwise private information. If you have received it in error, please delete. Any other use of the email by you is prohibited. ***\r\n"
	            + "\r\n"
	            + "For queries and concerns, please contact our services.\r\n"
	            + "\r\n"
	            + "Contact: Helpdesk@cntit.com");
		javaMailSender.send(message);
	}

	public void ExpiredOtps() {
		List<ForgetPassword> ForgetPasswordList = ForgetPasswordRepository.findOtpsByStatus("A");
		Instant currentInstant = Instant.now();
		for (ForgetPassword ForgetPassword : ForgetPasswordList) {
			Instant expiryInstant = ForgetPassword.getOtpExpiryDate().toInstant();
			Duration timeElapsed = Duration.between(currentInstant, expiryInstant);
			long minutesElapsed = timeElapsed.toMinutes();
			int minutesElapsedAsInteger = (int) minutesElapsed;
			if (minutesElapsedAsInteger <= 0) {
				ForgetPassword.setStatus("I");
				ForgetPasswordRepository.save(ForgetPassword);
			}
		}
	}

	public List<ForgetPassword> findOtpByUserIdsAndOtps(String userId, String userOTP) {
		return ForgetPasswordRepository.findOtpByUserIdsAndOtps(userId, userOTP);
	}

	public String findStatusByUserIdsAndOtps(String userId, String userOTP) {
		return ForgetPasswordRepository.findStatusByUserIdsAndOtps(userId, userOTP);
	}

	public List<ForgetPassword> findOtpsByStatus(String status) {
		return ForgetPasswordRepository.findOtpsByStatus(status);
	}
	
	 public void invalidateOtpsByUserIdforforgetpassword(String userId,String status) {
	        List<ForgetPassword> forgetotpList = ForgetPasswordRepository.findOtpsByStatusanduseridforget(userId,status);

	        for (ForgetPassword ForgetPassword : forgetotpList) {
	        	ForgetPassword.setStatus("I"); 
	        	ForgetPassword.setUpdatedDateTime(new Date()); 
	            ForgetPasswordRepository.save(ForgetPassword);
	        }
	    }
}
