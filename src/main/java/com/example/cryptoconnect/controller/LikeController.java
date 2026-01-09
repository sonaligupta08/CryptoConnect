package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cryptoconnect.entity.PostLike;
import com.example.cryptoconnect.repository.PostLikeRepository;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

	@Autowired
	private PostLikeRepository likeRepo;

	@PostMapping("/toggle")
	public void toggleLike(@RequestBody PostLike like) {

		if (like.getPostId() == null || like.getUserId() == null) {
			return;
		}

		var existing = likeRepo.findByPostIdAndUserId(like.getPostId(), like.getUserId());

		if (existing.isPresent()) {
			likeRepo.delete(existing.get());
		} else {
			likeRepo.save(like);
		}
	}

	// Count likes
	@GetMapping("/count/{postId}")
	public Long likeCount(@PathVariable Long postId) {
		return likeRepo.countByPostId(postId);
	}

	// Who liked
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

}
