package com.pattasu.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;

public interface UserService extends UserDetailsService{
	
    String initiateRegistration(UserRegistrationRequest request);
    String verifyOtpAndRegister(OtpVerificationRequest request);
}

