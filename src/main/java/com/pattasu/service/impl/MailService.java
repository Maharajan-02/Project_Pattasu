package com.pattasu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService{

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Pattasu OTP Code");
        message.setText(getMailBody(otp));
        mailSender.send(message);
    }
    
    private String getMailBody(String otp) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Welcome to Surya Pyro Park\n");
    	sb.append("Your otp to login is " + otp); 
    	sb.append("\n Your Otp will expire in 3 minutes");
    	return sb.toString();
    }
}
