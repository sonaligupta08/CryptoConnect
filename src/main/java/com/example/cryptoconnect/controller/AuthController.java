package com.example.cryptoconnect.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.cryptoconnect.entity.User;
import com.example.cryptoconnect.repository.UserRepository;
import com.example.cryptoconnect.security.JwtUtil;
import com.example.cryptoconnect.dto.LoginRequest;
import com.example.cryptoconnect.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/signup")
	public Map<String, Object> signup(@RequestBody User user) {

		Map<String, Object> response = new HashMap<>();

		// Age validation
		if (user.getAge() < 18) {
			response.put("message", "You must be 18 or above to sign up");
			return response;
		}

		// Null / empty email protection
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			response.put("message", "Email is required");
			return response;
		}

		// Duplicate email check
		if (userRepository.existsByEmail(user.getEmail().trim())) {
		    response.put("message", "Email already exists!");
		    return response;
		}
		// Password validation
		if (user.getPassword() == null || user.getPassword().length() < 8) {
			response.put("message", "Password must be at least 8 characters");
			return response;
		}
		

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setUsername(null);
		user.setBio("");
		user.setProfileImage("");

		User savedUser = userRepository.save(user);

		response.put("message", "Signup successful!");
		response.put("userId", savedUser.getId());

		return response;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {

		Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
		}

		User user = optionalUser.get();

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
		}

		String token = jwtUtil.generateToken(user.getId().toString());

		return ResponseEntity.ok(Map.of("token", token, "userId", user.getId(), "username", user.getUsername(), "email",
				user.getEmail()));
	}
}