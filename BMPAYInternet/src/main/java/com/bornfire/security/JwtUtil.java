package com.bornfire.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // ✅ Use a cryptographically secure 256-bit key (32 bytes minimum for HS256)
    private static final String SECRET = "mySecretKeyForJWT2024VeryLongAndSecure1234567890abcdef";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    
    // ✅ 1 hour expiration
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; 

    public static String generateToken(String userId, String merchantId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("merchantId", merchantId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new Exception("Invalid JWT signature");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new Exception("JWT token expired");
        } catch (Exception e) {
            throw new Exception("Invalid JWT token: " + e.getMessage());
        }
    }

    public static String getUserId(String token) throws Exception {
        return validateToken(token).getSubject();
    }
    
    public static String getMerchantId(String token) throws Exception {
        Claims claims = validateToken(token);
        String merchantId = claims.get("merchantId", String.class);
        if (merchantId == null || merchantId.trim().isEmpty()) {
            throw new Exception("merchantId not found in token");
        }
        return merchantId;
    }
    
}