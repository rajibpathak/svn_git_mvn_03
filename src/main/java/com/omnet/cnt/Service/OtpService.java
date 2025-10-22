/*
Document   : Otp service
Author     : Sunandhaa ,Jamal Abraar
last update: 04/04/2024
*/

package com.omnet.cnt.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.SimpleMailMessage;
import com.omnet.cnt.Model.Otp;
import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Repository.OtpRepository;
import com.omnet.cnt.Repository.UserRepository;

@Service
public class OtpService {
	private final JavaMailSender javaMailSender;
	private final UserRepository userRepository;
	private final OtpRepository otpRepository;

	@Autowired
	public OtpService(JavaMailSender javaMailSender, UserRepository userRepository, OtpRepository otpRepository) {
		this.javaMailSender = javaMailSender;
		this.userRepository = userRepository;
		this.otpRepository = otpRepository;
	}

	public String generateOTP() {
		int otpLength = 6;
		String otp = new Random().ints(0, 10).limit(otpLength).boxed().map(String::valueOf)
				.collect(Collectors.joining());
		return otp;
	}

	public void sendOtpByEmail(String email, String otp,String userId) {
		Optional<String> optionalName = userRepository.findConcatenatedNameByUserId(userId);
		String fullName = optionalName.orElse("");
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("authentication code");
		 message.setText("Dear " + fullName + ",\r\n"
		            + "\r\n"
		            + "As per your request, One Time Password (OTP) has been generated as below:\r\n"
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

	public void generateAndSendOtp(String userId) {
	    String userEmail = userRepository.findEmailByUserId(userId);
	    if (userEmail != null && !userEmail.trim().isEmpty()) {
	        String otp = generateOTP();
	        saveOtpToDatabase(userEmail, userId, otp);
	        sendOtpByEmail(userEmail, otp,userId);
	    } else {
	        System.out.println("User not found or email address not available.");
	    }
	}

	private void saveOtpToDatabase(String userEmail, String userId, String otp) {
		Otp otpEntity = new Otp();
		otpEntity.setOtp(otp);
		Date currentDate = new Date();
		otpEntity.setInsertedDateTime(currentDate);
		otpEntity.setInsertedUserId(userId);
		otpEntity.setUpdatedUserId(userId);
		otpEntity.setUpdatedDateTime(currentDate);
		otpEntity.setUserId(userId);
		otpEntity.setStatus("A");
		Date ExpiryDate = new Date(currentDate.getTime() + 1800000);
		System.out.println("ExpiryDate"+ExpiryDate);
		otpEntity.setOtpExpiryDate(ExpiryDate);
		otpRepository.save(otpEntity);
	}

	public void updateExpiredOtps() {
		List<Otp> otpList = otpRepository.findOtpsByStatus("A");
		Instant currentInstant = Instant.now();

		for (Otp otp : otpList) {
			Instant expiryInstant = otp.getOtpExpiryDate().toInstant();
			Duration timeElapsed = Duration.between(currentInstant, expiryInstant);
			long minutesElapsed = timeElapsed.toMinutes();
			int minutesElapsedAsInteger = (int) minutesElapsed;
			if (minutesElapsedAsInteger <= 0) {
				Date currentDate = new Date();
				otp.setInsertedDateTime(currentDate);
				otp.setStatus("I");
				otpRepository.save(otp);
			}
		}
	}

	public void saveOrUpdate(Otp otpEntity) {
		Instant expirationInstant = Instant.now().plus(Duration.ofMinutes(30));
		Date ExpiryDate = Date.from(expirationInstant);
		otpEntity.setOtpExpiryDate(ExpiryDate);
		otpRepository.save(otpEntity);
	}

	public List<Otp> findOtpByUserIdAndOtp(String userId, String userOTP) {
		return otpRepository.findOtpByUserIdAndOtp(userId, userOTP);
	}

	public String findStatusByUserIdsAndOtps(String userId, String userOTP) {
		return otpRepository.findStatusByUserIdAndOtp(userId, userOTP);
	}

	public List<Otp> findOtpsByStatus(String status) {
		return otpRepository.findOtpsByStatus(status);
	}

	private final Map<String, TokenInfo> tokenMap = new ConcurrentHashMap<>();

  
    public void storeToken(String userId, String token) {
        tokenMap.put(userId, new TokenInfo(token, System.currentTimeMillis()));
    }
    
    public String getuserIdForToken(String token) {
        for (Map.Entry<String, TokenInfo> entry : tokenMap.entrySet()) {
            if (entry.getValue().getToken().equals(token)) {
                return entry.getKey(); 
            }
        }
        return null; 
    }

    public boolean isValidToken(String token) {
        for (Map.Entry<String, TokenInfo> entry : tokenMap.entrySet()) {
            TokenInfo tokenInfo = entry.getValue();
            if (tokenInfo.getToken().equals(token)) {
                if (System.currentTimeMillis() - tokenInfo.getCreationTime() > 300000) {
                    tokenMap.remove(entry.getKey());
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    public void invalidateToken(String token) {
        for (Iterator<Map.Entry<String, TokenInfo>> iterator = tokenMap.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, TokenInfo> entry = iterator.next();
            if (entry.getValue().getToken().equals(token)) {
                iterator.remove();
                break;
            }
        }
    }
    
    private static class TokenInfo {
        private String token;
        private long creationTime;

        public TokenInfo(String token, long creationTime) {
            this.token = token;
            this.creationTime = creationTime;
        }

        public String getToken() {
            return token;
        }

        public long getCreationTime() {
            return creationTime;
        }
    }
  
    @Transactional
    public int updateOtpStatusAndUserInfo(String userId, String otp) {
        return otpRepository.updateOtpStatusAndUserInfo(userId, otp);
    }
    
    public void invalidateOtpsByUserId(String userId,String status) {
    	System.out.println("otpListexpirycheck0"+userId);
    	System.out.println("otpListexpirycheck1"+status);
        List<Otp> otpList = otpRepository.findOtpsByStatusanduserid(userId,status);
       System.out.println("otpListexpirycheck5"+otpList);
        for (Otp otp : otpList) {
            otp.setStatus("I"); 
            otp.setUpdatedDateTime(new Date()); 
            otpRepository.save(otp);
        }
    }
    
    @Scheduled(fixedRate = 1800000) // every 1 minute (for testing)
    @Transactional
    public void cleanExpiredOtps() {
        int updatedCount = otpRepository.invalidateExpiredOtps();
        System.out.println("Expired OTPs cleaned up at: " + LocalDateTime.now() +
                           " | Count: " + updatedCount);
    }
   
}
