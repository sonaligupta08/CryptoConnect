package com.example.cryproconnect.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryproconnect.entity.User;
import com.example.cryproconnect.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public Map<String, String> signup(@RequestBody User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return Map.of("message", "Email already exists!");
		}
		userRepository.save(user);
		return Map.of("message", "Signup successful!");

	}

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody User user) {
		return userRepository.findByEmail(user.getEmail()).filter(u -> u.getPassword().equals(user.getPassword()))
				.map(u -> Map.of("message", "Login successful!"))
				.orElse(Map.of("message", "Invalid email or password"));
	}
}
