package com.pattasu.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.pattasu.dto.LoginRequest;
import com.pattasu.dto.LoginResponse;
import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserDTO;
import com.pattasu.dto.UserRegistrationRequest;
import com.pattasu.entity.User;

public interface UserService extends UserDetailsService{
	
	ResponseEntity<String> initiateRegistration(UserRegistrationRequest request);
	ResponseEntity<String> verifyOtpAndRegister(OtpVerificationRequest request);
    public LoginResponse login(LoginRequest request);
    UserDTO getUser(User user);
}

