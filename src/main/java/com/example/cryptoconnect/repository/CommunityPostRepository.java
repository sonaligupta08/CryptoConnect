package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.CommunityPost;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long>{
	

    List<CommunityPost> findByCommunityId(Long communityId);

}
