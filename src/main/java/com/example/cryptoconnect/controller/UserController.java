package com.example.cryptoconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptoconnect.entity.User;
import com.example.cryptoconnect.repository.UserRepository;


@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	
	  @Autowired
	    private UserRepository userRepository;

	    @PutMapping("/profile/{id}")
	    public User updateProfile(@PathVariable Long id, @RequestBody User updated) {
	        User user = userRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        user.setUsername(updated.getUsername());
	        user.setBio(updated.getBio());
	        user.setProfileImage(updated.getProfileImage());

	        return userRepository.save(user);
	    }

}
