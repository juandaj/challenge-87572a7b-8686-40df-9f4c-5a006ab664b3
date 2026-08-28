package com.pragma.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public String getSecretKey() {
        return SECRET_KEY;
    }

    public String generateToken(String username) {
        return Jwts.builder()
               .setSubject(username)
               .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
               .compact();
    }
}