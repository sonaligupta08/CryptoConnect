package com.example.cryptoconnect.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cryptoconnect.entity.Community;
import com.example.cryptoconnect.entity.CommunityMember;
import com.example.cryptoconnect.repository.CommunityRepository;
import com.example.cryptoconnect.repository.CommunityMemberRepository;
import com.example.cryptoconnect.repository.UserRepository;

@Service
public class CommunityService {

	private final CommunityRepository communityRepo;
	private final CommunityMemberRepository memberRepo;
	private final UserRepository userRepo;

	public CommunityService(CommunityRepository communityRepo, CommunityMemberRepository memberRepo,
			UserRepository userRepo) {
		this.communityRepo = communityRepo;
		this.memberRepo = memberRepo;
		this.userRepo = userRepo;
	}

	// =====================================
	// CREATE COMMUNITY
	// =====================================
	public Community createCommunity(String name, String description, Long userId) {

		// validate user exists
		userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// create community
		Community community = new Community();
		community.setName(name);
		community.setDescription(description);
		community.setAdminId(userId);

		Community saved = communityRepo.save(community);

		// add creator as ADMIN member
		CommunityMember member = new CommunityMember();
		member.setCommunityId(saved.getId());
		member.setUserId(userId);
		member.setRole("ADMIN");

		memberRepo.save(member);

		return saved;
	}

	// =====================================
	// JOIN COMMUNITY
	// =====================================
	public String joinCommunity(Long communityId, Long userId) {

		communityRepo.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));

		userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// prevent duplicate join
		if (memberRepo.findByCommunityIdAndUserId(communityId, userId).isPresent()) {
			return "Already joined";
		}

		CommunityMember member = new CommunityMember();
		member.setCommunityId(communityId);
		member.setUserId(userId);
		member.setRole("MEMBER");

		memberRepo.save(member);

		return "Joined successfully";
	}

	// =====================================
	// DELETE COMMUNITY
	// =====================================
	public String deleteCommunity(Long communityId, Long userId) {

		Community community = communityRepo.findById(communityId)
				.orElseThrow(() -> new RuntimeException("Community not found"));

		if (!community.getAdminId().equals(userId)) {
			return "Only admin can delete";
		}

		communityRepo.delete(community);
		return "Deleted successfully";
	}

	public List<Community> getAllCommunities() {
		return communityRepo.findAll();
	}
	
	public List<Community> getUserCommunities(Long userId) {

	    List<CommunityMember> memberships =
	            memberRepo.findByUserId(userId);

	    List<Long> ids = memberships.stream()
	            .map(CommunityMember::getCommunityId)
	            .toList();

	    return communityRepo.findAllById(ids);
	}
}