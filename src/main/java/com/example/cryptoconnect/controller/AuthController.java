package com.example.cryptoconnect.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.cryptoconnect.entity.User;
import com.example.cryptoconnect.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/signup")
	public Map<String, String> signup(@RequestBody User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return Map.of("message", "Email already exists!");
		}
		user.setPassword(encoder.encode(user.getPassword()));

		userRepository.save(user);

		return Map.of("message", "Signup successful!");

	}

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody User user) {

		Optional<User> dbUser = userRepository.findByEmail(user.getEmail());

		if (dbUser.isPresent() && encoder.matches(user.getPassword(), dbUser.get().getPassword())) {
			return Map.of("message", "Login successful!");
		}

		return Map.of("message", "Invalid email or password");
	}

}