package com.pattasu.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET_KEY_STR = "3Nf43HgNblDqXQ7sy5uEt2YJ3CZ+lQ0BBu+0MyDq4S4="; // 🔐 keep this safe in prod
    private static final long EXPIRATION_TIME = (long)1000 * 60 * 60; // 1 hour
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STR.getBytes());

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

}
