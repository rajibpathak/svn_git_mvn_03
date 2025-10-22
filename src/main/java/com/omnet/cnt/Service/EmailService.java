package com.omnet.cnt.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String toEmail, String fName, String lName, String user, String pswd) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		
		String htmlMsg = "<h1>Hi " + lName + " " + fName + ",</h1><br>";
		htmlMsg += "You are Successfully registered.<br><br>";
		htmlMsg += "username : <b>" + user + "</b><br>";
		htmlMsg += "password : <b>" + pswd + "</b><br>";
		//mimeMessage.setContent(htmlMsg, "text/html"); /** Use this or below line **/
		try {
			helper.setText(htmlMsg, true);
			helper.setTo(toEmail);
			helper.setSubject("Credentials from OMNET");
			helper.setFrom("smtpcontrol.2024@gmail.com");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Use this or above line.
		
		mailSender.send(mimeMessage);

		System.out.println("Mail Sent successfully...");
	}
	
	public void sendEmailpassword(String to, String subject, String body) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(to);
	    message.setSubject(subject);
	    message.setText(body);
	    mailSender.send(message);
	}


}
