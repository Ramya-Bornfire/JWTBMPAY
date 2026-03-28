package com.bornfire.security;

import java.io.IOException; 
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
            	 logger.info("✅ Valid token for user: {}", jwtUtil.extractUserId(token));
                String userId = jwtUtil.extractUserId(token);
                String merchantId = jwtUtil.extractMerchantId(token);
                String role = jwtUtil.extractRole(token);

                // Create an Authentication object and set it in SecurityContext
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                // Optionally add additional details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // You can also store the extracted claims in the request attributes for later use
                request.setAttribute("authenticatedUserId", userId);
                request.setAttribute("authenticatedMerchantId", merchantId);
                request.setAttribute("authenticatedRole", role);
                
            }
            else {
                logger.warn("❌ Invalid token: {}", token);
            }
        }
        chain.doFilter(request, response);
    }

    // Exclude login and OTP endpoints from filtering
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.equals("/api/LoginAndroid") || path.equals("/api/LoginForTab") ||path.equals("/api/CheckDeviceId") ||
               path.equals("/api/OtpForAndroid") || path.equals("/api/OtpForMerchant") ||
               path.equals("/api/OtpForUser");
    }
}