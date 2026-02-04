package com.pattasu.service.impl;

import org.springframework.stereotype.Service;

import com.pattasu.entity.ContactInfo;
import com.pattasu.repository.ContactRepository;

@Service
public class MailService {
  private final MailjetHttpMailService mailjet;
  private final ContactRepository contactRepository;

  public MailService(MailjetHttpMailService mailjet, ContactRepository contactRepository) {
    this.mailjet = mailjet;
    this.contactRepository = contactRepository;
  }
  
  public void sendOtpEmail(String toEmail, String otp) throws Exception {
    String name = contactRepository.findAll().stream().findFirst()
        .map(ContactInfo::getShopName).filter(s -> s != null && !s.isBlank())
        .orElse("Pattasu");

    String html = """
      <p>Dear Valued Customer,</p>
      <p>Welcome to %s!</p>
      <p>To complete your login, please use this One-Time Password (OTP): <b>%s</b></p>
      <p><i>This code will expire in 3 minutes.</i></p>
      <p>If you didn’t request this, you can ignore this message.</p>
      <p>Best regards,<br/>The %s Team</p>
      """.formatted(name, otp, name);

    mailjet.send(toEmail, "Your Login Verification Code", html);
  }
}
