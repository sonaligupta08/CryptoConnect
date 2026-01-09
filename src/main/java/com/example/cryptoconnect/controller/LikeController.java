package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.cryptoconnect.entity.Post;
import com.example.cryptoconnect.entity.PostLike;
import com.example.cryptoconnect.repository.PostLikeRepository;
import com.example.cryptoconnect.repository.PostRepository;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

	@Autowired
	private PostLikeRepository likeRepo;

	@Autowired
	private PostRepository postRepo;

	@PostMapping("/toggle")
	public void toggleLike(@RequestBody PostLike like) {

		if (like.getPostId() == null || like.getUserId() == null)
			return;

		var existing = likeRepo.findByPostIdAndUserId(like.getPostId(), like.getUserId());

		if (existing.isPresent()) {
			likeRepo.delete(existing.get());
		} else {
			likeRepo.save(like);
		}
	}

	@GetMapping("/count/{postId}")
	public Long likeCount(@PathVariable Long postId) {
		return likeRepo.countByPostId(postId);
	}

	@GetMapping("/users/{postId}")
	public List<PostLike> likedUsers(@PathVariable Long postId) {
		return likeRepo.findByPostId(postId);
	}

	@GetMapping("/status/{postId}/{userId}")
	public boolean likeStatus(@PathVariable Long postId, @PathVariable Long userId) {

		return likeRepo.findByPostIdAndUserId(postId, userId).isPresent();
	}

	@GetMapping("/user/{userId}")
	public List<Long> likedPostIds(@PathVariable Long userId) {
		return likeRepo.findByUserId(userId).stream().map(PostLike::getPostId).toList();
	}

	
	@GetMapping("/posts/liked/{userId}")
	public List<Post> getLikedPosts(@PathVariable Long userId) {

		List<Long> postIds = likeRepo.findByUserId(userId).stream().map(PostLike::getPostId).toList();

		if (postIds.isEmpty())
			return List.of();

		return postRepo.findByIdIn(postIds);
	}
}
