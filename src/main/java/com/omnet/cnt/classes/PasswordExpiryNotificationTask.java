package com.omnet.cnt.classes;



import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.omnet.cnt.Model.Users;
import com.omnet.cnt.Service.EmailService;
import com.omnet.cnt.Service.UserService;


@Component
public class PasswordExpiryNotificationTask {

	 @Autowired
	    private EmailService emailService;
	  @Autowired
	    private UserService userservice;
	  
	  @Scheduled(cron = "0 40 11 * * ?") // This cron expression schedules the task to run daily at 9 AM
	    public void scheduleEmailNotifications() {
	        // Check for users with passwords expiring in 2 days
	        sendEmailNotifications(2, "Password Expiry Notification - 2 Days Left", "Your password will expire in 2 days. Please update your password.");

	        // Check for users with passwords expiring in 1 day
	        sendEmailNotifications(1, "Password Expiry Notification - 1 Day Left", "Your password will expire in 1 day. Please update your password.");
	    }

	  private void sendEmailNotifications(int days, String subject, String messageTemplate) {
	        List<Users> users = userservice.getUsersWithExpiryDateInDays(days);
			/* DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); */

	        for (Users user : users) {
	            Date expiryDate = user.getPasswordExpiredDate();
	            if (expiryDate != null) {
	                String message = String.format("Dear %s,\n\n%s\n\nBest regards,\n OMNET",
	                        user.getUserFirstName(), messageTemplate);
	                emailService.sendEmailpassword(user.getUserEmailAddress(), subject, message);
	            }
	        }
	    }
}
	  
	   
