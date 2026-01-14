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

import com.example.cryptoconnect.entity.CommunityPost;
import com.example.cryptoconnect.repository.CommunityMemberRepository;
import com.example.cryptoconnect.repository.CommunityPostRepository;

@RestController
@RequestMapping("/api/community-posts")
@CrossOrigin("*")
public class CommunityPostController {

	@Autowired
	private CommunityPostRepository postRepo;

	@Autowired
	private CommunityMemberRepository memberRepo;

	@GetMapping("/{communityId}")
	public List<CommunityPost> getPosts(@PathVariable Long communityId) {
		return postRepo.findByCommunityId(communityId);
	}

	@PostMapping("/create")
	public CommunityPost create(@RequestBody CommunityPost post) {

		Long communityId = post.getCommunityId();
		Long userId = post.getUserId();

		boolean isMember = memberRepo.existsByCommunityIdAndUserId(communityId, userId);

		if (!isMember) {
			throw new RuntimeException("Only members can post");
		}

		return postRepo.save(post);
	}

}
