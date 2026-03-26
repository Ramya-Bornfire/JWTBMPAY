package com.bornfire.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
        "/api/LoginAndroid", "/api/LoginForTab", "/api/LoginforMobile",
        "/api/OtpForUser", "/api/OtpForMerchant", "/api/OtpForAndroid",
        "/api/getStaticPaydetails", "/api/ws/StaticMaucas", "/api/CheckDeviceId",
        "/api/CheckTwoFactorAnswer", "/error"
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        logger.debug("🔍 Filtering: {}", path);

        // ✅ Public endpoints
        if (isPublicEndpoint(path)) {
            logger.debug("✅ Public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
    

        // ✅ Check Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("❌ Missing or invalid Authorization header");
            sendError(response, 401, "Authorization header with Bearer token required");
            return;
        }

        

        String token = authHeader.substring(7).trim();

        try {
            // ✅ Validate JWT token
            Claims claims = JwtUtil.validateToken(token);
            String jwtUserId = claims.getSubject();
            String jwtMerchantId = JwtUtil.getMerchantId(token); // ✅ Use safe getter

            // ✅ Validate claims
            if (jwtUserId == null || jwtUserId.trim().isEmpty()) {
                logger.warn("❌ Invalid userId in JWT");
                sendError(response, 401, "Invalid user in token");
                return;
            }

            logger.info("✅ JWT Valid - User: {}, Merchant: {}, Device: {}", 
                       jwtUserId, jwtMerchantId);

            // ✅ Set authentication and request attributes
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(jwtUserId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            request.setAttribute("userId", jwtUserId);
            request.setAttribute("merchantId", jwtMerchantId);
         

        } catch (Exception e) {
            logger.error("❌ JWT validation failed for path {}: {}", path, e.getMessage());
            SecurityContextHolder.clearContext();
            sendError(response, 401, "Invalid or expired JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(path::contains);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        logger.warn("Sending error {}: {}", status, message);
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        
        String jsonResponse = String.format(
            "{\"status\":\"error\",\"message\":\"%s\",\"timestamp\":%d}", 
            message.replace("\"", "\\\""), 
            System.currentTimeMillis()
        );
        response.getWriter().write(jsonResponse);
    }
}