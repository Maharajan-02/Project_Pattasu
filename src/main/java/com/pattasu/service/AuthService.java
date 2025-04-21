package com.pattasu.service;

import com.pattasu.dto.LoginRequest;
import com.pattasu.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
