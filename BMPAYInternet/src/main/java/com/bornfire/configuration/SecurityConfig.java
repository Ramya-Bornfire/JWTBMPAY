package com.bornfire.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.bornfire.security.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers()
				.contentSecurityPolicy(
						"default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none';")
				.and().xssProtection().block(true).and().contentTypeOptions() 
				.and().frameOptions().deny()
				.and().authorizeRequests()
				.antMatchers("/api/LoginAndroid", "/api/LoginForTab", "/api/OtpForAndroid", "/api/OtpForMerchant",
						"/api/OtpForUser", "/api/CheckDeviceId")
				.permitAll().anyRequest().authenticated().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}