package com.example.cryproconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.cryproconnect.CryproconnectApplication;
import com.example.cryproconnect.entity.Post;
import com.example.cryproconnect.repository.PostRepository;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final CryproconnectApplication cryproconnectApplication;

	@Autowired
	PostRepository postRepository;

    PostController(CryproconnectApplication cryproconnectApplication) {
        this.cryproconnectApplication = cryproconnectApplication;
    }

	@PostMapping("/create")
	public Post createPost(@RequestBody Post post) {
		return postRepository.save(post);
	}

	@GetMapping("/user/{userId}")
	public List<Post> getUserPosts(@PathVariable Long userId) {
		return postRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	@GetMapping("/all")
	public List<Post> getAllPosts() {
		return postRepositoruserRepositorysc();
	}

}
