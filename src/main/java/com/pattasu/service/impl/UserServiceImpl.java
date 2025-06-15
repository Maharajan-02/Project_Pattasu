package com.pattasu.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pattasu.dto.LoginRequest;
import com.pattasu.dto.LoginResponse;
import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserRegistrationRequest;
import com.pattasu.entity.PendingUser;
import com.pattasu.entity.User;
import com.pattasu.exception.InvalidCredentialsException;
import com.pattasu.exception.OtpExpiredException;
import com.pattasu.exception.OtpMismatchException;
import com.pattasu.exception.UserNotFoundException;
import com.pattasu.repository.PendingUserRepository;
import com.pattasu.repository.UserRepository;
import com.pattasu.service.JwtService;
import com.pattasu.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final PendingUserRepository pendingUserRepository;
    private final Random random = new Random();
    private final JwtService jwtService;
    
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailService mailService
    		, PendingUserRepository pendingUserRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.pendingUserRepository = pendingUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public ResponseEntity<String> initiateRegistration(UserRegistrationRequest request) {
       
    	try {
    		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    			return ResponseEntity
    				    .status(HttpStatus.BAD_REQUEST)
    				    .body("User already exists");
            }

        	String encodedPassword = passwordEncoder.encode(request.getPassword());
        	
        	PendingUser pending = pendingUserRepository.findByEmail(request.getEmail())
        		    .orElse(new PendingUser());

            // Generate OTP
        	int otpValue = random.nextInt(1_000_000); // generates 0 to 999999
        	String otp = String.format("%06d", otpValue);
        	
            // Store in DB
            pending.setName(request.getName());
            pending.setEmail(request.getEmail());
            pending.setPhoneNumber(request.getPhoneNumber());
            pending.setPassword(encodedPassword);
            pending.setOtp(otp);
            pending.setOtpExpiry(LocalDateTime.now().plusMinutes(3));

            pendingUserRepository.save(pending);

            // Send OTP to user's email
            mailService.sendOtpEmail(request.getEmail(), otp);

            return ResponseEntity
            		.status(HttpStatus.OK)
            		.body("OTP sent to your email. Please verify to complete registration.");
    	}catch(Exception e) {
    		log.info("error during inititate registration {} ", e.getMessage());
    		return ResponseEntity
            		.status(HttpStatus.BAD_REQUEST)
            		.body(e.getMessage());
    	}
    }

    @Override
    @Transactional
    public ResponseEntity<String> verifyOtpAndRegister(OtpVerificationRequest request) {

        try {
        	PendingUser pending = pendingUserRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("No registration found for this email."));

                if (pending.getOtpExpiry().isBefore(LocalDateTime.now())) {
                    throw new OtpExpiredException("OTP expired.");
                }

                if (!pending.getOtp().equals(request.getOtp())) {
                    throw new OtpMismatchException("Invalid OTP.");
                }

                User user = new User();
                user.setName(pending.getName());
                user.setEmail(pending.getEmail());
                user.setPhoneNumber(pending.getPhoneNumber());
                user.setPassword(pending.getPassword()); // 🔒 secure
                user.setRole("user");

                userRepository.save(user);
                pendingUserRepository.deleteByEmail(request.getEmail());

                return ResponseEntity
                		.status(HttpStatus.OK)
                		.body("User registered successfully!");
		} catch (Exception e) {
			
			log.info("error while otp verification {} ", e.getMessage());
			return ResponseEntity
            		.status(HttpStatus.BAD_REQUEST)
            		.body(e.getMessage());
		}
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
        	throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity
        		.status(HttpStatus.OK)
        		.body(new LoginResponse(token, user.getRole()));
    }

}
