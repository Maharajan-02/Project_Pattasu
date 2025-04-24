package com.pattasu.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;
import com.pattasu.exception.OtpExpiredException;
import com.pattasu.exception.OtpMismatchException;
import com.pattasu.exception.UserNotFoundException;
import com.pattasu.model.PendingUser;
import com.pattasu.model.User;
import com.pattasu.repository.PendingUserRepository;
import com.pattasu.repository.UserRepository;
import com.pattasu.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final PendingUserRepository pendingUserRepository;
    private final Random random = new Random();
    
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailService mailService
    		, PendingUserRepository pendingUserRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.pendingUserRepository = pendingUserRepository;
    }

    @Override
    @Transactional
    public String initiateRegistration(UserRegistrationRequest request) {
       
    	if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "User already exists with this email.";
        }

    	PendingUser pending = pendingUserRepository.findByEmail(request.getEmail())
    		    .orElse(new PendingUser());

        // Generate OTP
    	
    	int otpValue = random.nextInt(1_000_000); // generates 0 to 999999
    	String otp = String.format("%06d", otpValue);

        // Store in DB
        pending.setName(request.getName());
        pending.setEmail(request.getEmail());
        pending.setPhoneNumber(request.getPhoneNumber());
        pending.setPassword(request.getPassword()); // Will be encrypted after OTP verification
        pending.setOtp(otp);
        pending.setOtpExpiry(LocalDateTime.now().plusMinutes(3));

        pendingUserRepository.save(pending);

        // Send OTP to user's email
        mailService.sendOtpEmail(request.getEmail(), otp);

        return "OTP sent to your email. Please verify to complete registration.";
    }

    @Override
    @Transactional
    public String verifyOtpAndRegister(OtpVerificationRequest request) {

        PendingUser pending = pendingUserRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("No registration found for this email."));

        if (pending.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP expired.");
        }

        if (!pending.getOtp().equals(request.getOtp())) {
            throw new OtpMismatchException("Invalid OTP.");
        }

        // ✅ Encrypt the password before saving the final user
        String encodedPassword = passwordEncoder.encode(pending.getPassword());

        User user = new User();
        user.setName(pending.getName());
        user.setEmail(pending.getEmail());
        user.setPhoneNumber(pending.getPhoneNumber());
        user.setPassword(encodedPassword); // 🔒 secure
        user.setRole("user");

        userRepository.save(user);
        pendingUserRepository.deleteByEmail(request.getEmail());

        return "User registered successfully!";
    }


}
