package com.example.cryptoconnect.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
        	    "http://localhost:5500",
        	    "http://localhost:8080"
        	));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

              
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/posts/**").permitAll()
                .requestMatchers("/api/likes/**").permitAll()
                .requestMatchers("/uploads/**").permitAll()

                
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/login.html",
                        "/signup.html",
                        "/dashboard.html",
                        "/profile.html",
                        "/about.html",
                        "/message.html",
                        "/profilesetup.html"
                ).permitAll()

              
                .requestMatchers(
                        "/*.png",
                        "/*.jpg",
                        "/*.jpeg",
                        "/*.svg",
                        "/favicon.ico"
                ).permitAll()

                
                .requestMatchers("/api/chat/**").permitAll()
            );

        return http.build();
    }
}
