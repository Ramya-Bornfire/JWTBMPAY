package com.bornfire.configuration;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.bornfire.security.JwtRequestFilter; 
//@Configuration
//public class SecurityConfig {
//	  @Bean
//	    public JwtFilter jwtFilter() {
//	        return new JwtFilter(); // ✅ Create as bean
//	    }    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//            .csrf().disable()
//            .authorizeRequests()
//            .antMatchers( "/api/LoginAndroid",
//            	    "/api/LoginForTab",
//            	    "/api/LoginforMobile",
//            	    "/api/OtpForUser",    
//                    "/api/OtpForMerchant",  
//                    "/api/OtpForAndroid",
//                    "/api/getStaticPaydetails",      
//                    "/api/ws/StaticMaucas",
//                    "/api/CheckTwoFactorAnswer",
//            	    "/api/CheckDeviceId").permitAll() 
//            .anyRequest().authenticated()
//            .and()
//            .headers()
//                .contentSecurityPolicy("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; connect-src 'self'; font-src 'self'; frame-ancestors 'none';")
//                .and()
//                .xssProtection()
//                    .block(true)
//                .and()
//                .contentTypeOptions()
//                .and()
//                .httpStrictTransportSecurity()
//                    .includeSubDomains(true)
//                    .maxAgeInSeconds(31536000);
//        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/LoginAndroid", "/api/LoginForTab", "/api/OtpForAndroid", "/api/OtpForMerchant", "/api/OtpForUser","/api/CheckDeviceId").permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}