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

public class JwtFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        logger.info("🔍 Filtering: " + path);

        // ✅ Public endpoints
        if (isPublicEndpoint(path)) {
            logger.debug("✅ Public endpoint: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("❌ No JWT token");
            sendError(response, 401, "JWT token required");
            return;
        }

        String token = authHeader.substring(7);
        String headerDeviceId = request.getHeader("PSU_Device_ID");

        try {
            Claims claims = JwtUtil.validateToken(token);
            String jwtUserId = claims.getSubject();
            String jwtDeviceId = (String) claims.get("deviceId");

            logger.info("✅ JWT Valid - User: {}, Device: {}", jwtUserId, jwtDeviceId);

            // 🔥 STRICT DEVICE VALIDATION
            if (headerDeviceId == null || jwtDeviceId == null) {
                logger.warn("❌ Missing device ID - Header: {}, JWT: {}", headerDeviceId, jwtDeviceId);
                sendError(response, 403, "Device ID required");
                return;
            }

            if (!jwtDeviceId.equals(headerDeviceId)) {
                logger.warn("❌ DEVICE MISMATCH - JWT: {}, Header: {}", jwtDeviceId, headerDeviceId);
                sendError(response, 403, "Device ID mismatch");
                return;
            }

            // ✅ Authentication successful
            UsernamePasswordAuthenticationToken auth = 
                new UsernamePasswordAuthenticationToken(jwtUserId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.setAttribute("userId", jwtUserId);
            request.setAttribute("deviceId", jwtDeviceId);

        } catch (Exception e) {
            logger.error("❌ JWT validation failed: {}", e.getMessage());
            sendError(response, 401, "Invalid JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/api/LoginAndroid") ||
               path.contains("/api/CheckDeviceId") ||
               path.contains("/api/OtpFor") ||
               path.contains("/api/ws/StaticMaucas") ||
               path.contains("/api/getStaticPaydetails") ||
               path.contains("/error");
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(String.format("{\"status\":\"Error\",\"message\":\"%s\"}", message));
    }
}