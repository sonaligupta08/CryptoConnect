package com.example.cryptoconnect.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.cryptoconnect.entity.Post;
import com.example.cryptoconnect.entity.User;
import com.example.cryptoconnect.repository.PostRepository;
import com.example.cryptoconnect.repository.UserRepository;
import com.example.cryptoconnect.service.BlockchainService;
import com.example.cryptoconnect.service.NotificationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private NotificationService notificationService;

    // ================================
    // CREATE POST
    // ================================
    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam Long userId,
            @RequestParam String content,
            @RequestParam(required = false) String postHash,
            @RequestParam(required = false) String walletAddress,
            @RequestParam(required = false) MultipartFile image
    ) {

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Post content required");
        }

        // get real user from DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setUserId(userId);
        post.setUsername(user.getUsername()); // SAFE SOURCE
        post.setContent(content);
        post.setWalletAddress(walletAddress);
        post.setPostHash(postHash);
        post.setDeleted(false);

        // save image if exists
        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            post.setImageUrl(imageUrl);
        }

        // save post first
        Post savedPost = postRepository.save(post);

        // blockchain (non-blocking)
        try {
            if (postHash != null && !postHash.isBlank()) {
                String txHash = blockchainService.savePostHashToBlockchain(postHash);
                savedPost.setBlockchainTxHash(txHash);
                postRepository.save(savedPost);
            }
        } catch (Exception e) {
            System.out.println("Blockchain failed but post saved: " + e.getMessage());
        }

        return ResponseEntity.ok(savedPost);
    }

    // ================================
    // SAVE IMAGE TO SERVER
    // ================================
    private String saveImage(MultipartFile file) {
        try {
            String uploadDir = "uploads/posts/";
            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uploadDir + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Post image upload failed");
        }
    }

    // ================================
    // USER POSTS
    // ================================
    @GetMapping("/user/{userId}")
    public List<Post> getPostByUser(@PathVariable Long userId) {
        return postRepository.findByUserId(userId);
    }

    // ================================
    // DELETE POST
    // ================================
    @PostMapping("/{postId}/delete/{userId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @PathVariable Long userId) {

        Post post = postRepository.findById(postId).orElse(null);

        if (post == null)
            return ResponseEntity.notFound().build();

        if (!post.getUserId().equals(userId))
            return ResponseEntity.status(403).body("Not allowed");

        post.setDeleted(true);
        post.setContent("This post was deleted");
        post.setImageUrl(null);

        postRepository.save(post);

        notificationService.notifyDelete(postId, post.getUsername());

        return ResponseEntity.ok("Post deleted successfully");
    }

    // ================================
    // ALL POSTS
    // ================================
    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}