package com.pattasu.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

//	private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final String SECRET_KEY = "3Nf43HgNblDqXQ7sy5uEt2YJ3CZ+lQ0BBu+0MyDq4S4="; // 🔐 keep this safe in prod
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Add validation methods later as needed
}
