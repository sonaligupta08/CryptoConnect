package com.example.cryptoconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cryptoconnect.entity.CommunityMember;
import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, Long> {

    List<CommunityMember> findByCommunityId(Long communityId);

    Optional<CommunityMember> findByCommunityIdAndUserId(Long communityId, Long userId);

    long countByCommunityId(Long communityId);
    List<CommunityMember> findByUserId(Long userId);
}