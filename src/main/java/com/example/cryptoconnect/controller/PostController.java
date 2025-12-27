package com.example.cryptoconnect.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cryptoconnect.entity.Post;
import com.example.cryptoconnect.repository.PostRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	// Create a new post
	@PostMapping("/create")
	public Post createPost(@RequestBody Post post) {
		return postRepository.save(post);
	}

	// Get all posts for a specific user
	@GetMapping("/user/{userId}")
	public List<Post> getUserPosts(@PathVariable Long userId) {
		return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	// Get all posts in the system
	@GetMapping("/all")
	public List<Post> getAllPosts() {
		return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	@DeleteMapping("/{id}/user/{userId}")
	public ResponseEntity<String> deletePost(
	        @PathVariable Long id,
	        @PathVariable Long userId) {

	    Optional<Post> post = postRepository.findById(id);

	    if (post.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    if (!post.get().getUserId().equals(userId)) {
	        return ResponseEntity.status(403).body("Forbidden");
	    }

	    postRepository.deleteById(id);
	    return ResponseEntity.ok("Deleted");
	}


}