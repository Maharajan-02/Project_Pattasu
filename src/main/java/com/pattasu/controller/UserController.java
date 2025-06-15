package com.pattasu.controller;

import com.pattasu.dto.LoginRequest;
import com.pattasu.dto.LoginResponse;
import com.pattasu.dto.OtpVerificationRequest;
import com.pattasu.dto.UserDTO;
import com.pattasu.dto.UserRegistrationRequest;
import com.pattasu.entity.User;
import com.pattasu.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.initiateRegistration(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        return ResponseEntity.ok(userService.verifyOtpAndRegister(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    
    @GetMapping("/user")
    public UserDTO getUser(@AuthenticationPrincipal User user) {
    	return new UserDTO(user);
    }
}
