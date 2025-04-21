package com.pattasu.service;

import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;

public interface UserService {
	
    String registerUser(UserRegistrationRequest request);
    String initiateRegistration(UserRegistrationRequest request);
    String verifyOtpAndRegister(OtpVerificationRequest request);	
}

