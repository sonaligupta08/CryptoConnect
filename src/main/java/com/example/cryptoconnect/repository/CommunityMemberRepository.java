package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.CommunityMember;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

	long countByCommunityId(Long communityId);

	boolean existsByCommunityIdAndUserId(Long communityId, Long userId);

	List<CommunityMember> findByUserId(Long userId);

}
