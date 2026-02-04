package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cryptoconnect.entity.Post;
import com.example.cryptoconnect.repository.PostRepository;
import com.example.cryptoconnect.service.BlockchainService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BlockchainService blockchainService;

    // creating post
    @PostMapping("/create")
    public Post createPost(
            @RequestParam Long userId,
            @RequestParam String username,
            @RequestParam String content,
            @RequestParam(required = false) String postHash,
            @RequestParam(required = false) String walletAddress,
            @RequestParam(required = false) MultipartFile image
    ) {

        Post post = new Post();
        post.setUserId(userId);
        post.setUsername(username);
        post.setContent(content);
        post.setPostHash(postHash);
        post.setWalletAddress(walletAddress);
        post.setDeleted(false);

        if (image != null && !image.isEmpty()) {
            post.setImageUrl(image.getOriginalFilename());
        }

        return postRepository.save(post);
    }

    // get post of a user
    @GetMapping("/user/{userId}")
    public List<Post> getPostByUser(@PathVariable Long userId) {
        return postRepository.findByUserId(userId);
    }

    // delete post
    @PostMapping("/{postId}/delete/{userId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @PathVariable Long userId) {

        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        if (!post.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        post.setDeleted(true);
        post.setContent("This post was deleted");
        post.setImageUrl(null);

        postRepository.save(post);

        return ResponseEntity.ok("Post deleted successfully");
    }

    // get all posts
    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}
