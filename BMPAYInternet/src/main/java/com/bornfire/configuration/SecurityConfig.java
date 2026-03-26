package com.bornfire.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.bornfire.security.JwtFilter; 
@Configuration
public class SecurityConfig {
	  @Bean
	    public JwtFilter jwtFilter() {
	        return new JwtFilter(); // ✅ Create as bean
	    }    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers( "/api/LoginAndroid",
            	    "/api/LoginForTab",
            	    "/api/LoginforMobile",
            	    "/api/OtpForUser",    
                    "/api/OtpForMerchant",  
                    "/api/OtpForAndroid",
                    "/api/getStaticPaydetails",      
                    "/api/ws/StaticMaucas",
                    "/api/CheckTwoFactorAnswer",
            	    "/api/CheckDeviceId").permitAll() 
            .anyRequest().authenticated()
            .and()
            .headers()
                .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; connect-src 'self'; font-src 'self'; frame-ancestors 'none';")
                .and()
                .xssProtection()
                    .block(true)
                .and()
                .contentTypeOptions()
                .and()
                .httpStrictTransportSecurity()
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000);
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
