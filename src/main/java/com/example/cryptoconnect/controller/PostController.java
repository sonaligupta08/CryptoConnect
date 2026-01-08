package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptoconnect.entity.Post;
import com.example.cryptoconnect.repository.PostRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostRepository postRepository;

//creating post
	@PostMapping("/create")
	public Post createPost(@RequestBody Post post) {
		return postRepository.save(post);
	}

//get post of a user
	@GetMapping("/user/{userId}")
	public List<Post> getPostByUser(@PathVariable Long userId) {
		return postRepository.findByUserId(userId);
	}

//delete post
	@DeleteMapping("/{postId}/user/{userId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId, @PathVariable Long userId) {

		Post post = postRepository.findById(postId).orElse(null);

		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		if (!post.getUserId().equals(userId)) {
			return ResponseEntity.status(403).body("Not allowed");
		}

		postRepository.deleteById(postId);
		return ResponseEntity.ok().build();
	}
	
	//get all posts
	@GetMapping("/all")
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

}