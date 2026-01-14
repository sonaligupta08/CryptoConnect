package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.CommunityMember;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

	boolean existsByCommunityIdAndUserId(Long communityId, Long userId);

	List<CommunityMember> findByUserId(Long userId);
	
	 int countByCommunityId(Long communityId);
	 
	 void deleteByCommunityIdAndUserId(Long communityId, Long userId);
	 
	 void deleteByCommunityId(Long communityId);


}
