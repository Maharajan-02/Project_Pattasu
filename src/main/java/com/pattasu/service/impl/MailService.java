package com.pattasu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pattasu.entity.ContactInfo;
import com.pattasu.repository.ContactRepository;

@Service
public class MailService{

    private final JavaMailSender mailSender;
    private final ContactRepository contactRepository;

    @Autowired
    public MailService(JavaMailSender mailSender, ContactRepository contactRepository) {
        this.mailSender = mailSender;
        this.contactRepository = contactRepository;
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Login Verification Code");
        message.setText(getMailBody(otp));
        mailSender.send(message);
    }
    
    private String getMailBody(String otp) {
    	ContactInfo contact = contactRepository.findAll().get(0);
    	StringBuilder sb = new StringBuilder();
    	String name = contact.getShopName();
    	sb.append("Dear Valued Customer,\r\n");
		sb.append("\nWelcome to ").append(name).append("!\n");
		sb.append("\nTo complete your login, please use the following One-Time Password (OTP) : ").append(otp);
    	sb.append("\n⏰ Important: This code will expire in 3 minutes for your security.\n");
    	sb.append("\nIf you didn't request this login, please ignore this email or contact our support team immediately.\n");
    	sb.append("\nThank you for choosing ").append(name).append("!\n"); 
    	sb.append("\nBest regards,\r\nThe ").append(name).append(" Team");
    	return sb.toString();
    }
}
