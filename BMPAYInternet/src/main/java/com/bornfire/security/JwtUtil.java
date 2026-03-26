package com.bornfire.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "MerchantQRCode2024!SecretKeyVeryLong1234567890";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // ✅ Still generate with deviceId (backward compatible)
    public static String generateToken(String userId, String merchantId, String deviceId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("merchantId", merchantId)
                .claim("deviceId", deviceId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }


    public static Claims validateToken(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUserId(String token) throws Exception {
        return validateToken(token).getSubject();
    }
    public static String getMerchantId(String token) throws Exception {
        return (String) validateToken(token).get("merchantId");
    }
 // ✅ Add this to JwtUtil for debugging
    public static String getDeviceId(String token) throws Exception {
        Claims claims = validateToken(token);
        String deviceId = (String) claims.get("deviceId");
        System.out.println("🔍 JWT DeviceId: " + deviceId);
        return deviceId;
    }
    
}
