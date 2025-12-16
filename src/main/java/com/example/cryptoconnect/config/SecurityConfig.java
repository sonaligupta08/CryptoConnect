package com.example.cryptoconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
		.csrf(csrf -> csrf.disable())
		.cors(cors -> {})
		.authorizeHttpRequests(auth -> auth

				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/posts/**").permitAll()

				.requestMatchers("/", "/index.html", "/login", "/signup", "/login.html", "/signup.html", "/about.html", "/dashboard.html", 
						"/profile.html")
				.permitAll()

				.requestMatchers(
		                "/**/*.css",
		                "/**/*.js",
		                "/**/*.png",
		                "/**/*.jpg",
		                "/**/*.jpeg",
		                "/favicon.ico"
		            ).permitAll()

				.anyRequest().authenticated());
		         
		return http.build();
	}
}
