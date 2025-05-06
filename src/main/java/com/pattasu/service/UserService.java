package com.pattasu.service;

import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;

public interface UserService {
	
    String initiateRegistration(UserRegistrationRequest request);
    String verifyOtpAndRegister(OtpVerificationRequest request);	
}

