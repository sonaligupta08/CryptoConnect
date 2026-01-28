package com.example.cryptoconnect.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cryptoconnect.entity.User;
import com.example.cryptoconnect.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PutMapping("/profile/{id}")
	public User updateProfile(@PathVariable Long id, @RequestParam String username,
			@RequestParam(required = false) String bio, @RequestParam(required = false) MultipartFile profileImage)
			throws IOException {

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		user.setUsername(username);
		user.setBio(bio);

		if (profileImage != null && !profileImage.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
			Path uploadPath = Paths.get("src/main/resources/static/uploads");

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			Files.copy(profileImage.getInputStream(), uploadPath.resolve(fileName),
					StandardCopyOption.REPLACE_EXISTING);

			user.setProfileImage("/uploads/" + fileName);
		}

		return userRepository.save(user);
	}

}
