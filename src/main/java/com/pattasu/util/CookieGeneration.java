package com.pattasu.util;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieGeneration {
	
	private CookieGeneration() {}
	
	public static ResponseCookie getCookie(String token) {
		return ResponseCookie.from("authToken", token)
	            .httpOnly(true)
	            .secure(true) // only over HTTPS
	            .sameSite("None")
	            .path("/")    // available to all API endpoints
	            .sameSite("Strict")
	            .maxAge(Duration.ofHours(2))
	            .build();
	}

}
