package com.example.cryproconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf().disable()
	        .cors().and()
	        .authorizeRequests()
	            .requestMatchers(
	                "/index.html",
	                "/login.html",
	                "/signup.html",
	                "/css/**",
	                "/js/**",
	                "/images/**",
	                "/api/auth/**" 
	            ).permitAll()
	            .anyRequest().authenticated() 
	        .and()
	        .formLogin().disable(); 

	    return http.build();
	}

}
