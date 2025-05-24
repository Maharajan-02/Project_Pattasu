package com.pattasu.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.pattasu.dto.LoginRequest;
import com.pattasu.dto.LoginResponse;
import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;

public interface UserService extends UserDetailsService{
	
    String initiateRegistration(UserRegistrationRequest request);
    String verifyOtpAndRegister(OtpVerificationRequest request);
    public LoginResponse login(LoginRequest request);
}

