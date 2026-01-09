package com.example.cryptoconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cryptoconnect.entity.CommunityMember;
import com.example.cryptoconnect.repository.CommunityMemberRepository;

@Service
public class CommunityService {

    @Autowired
    private CommunityMemberRepository memberRepo;

    public void addMember(Long communityId, Long userId) {

        if (memberRepo.existsByCommunityIdAndUserId(communityId, userId)) {
            return;
        }

        long count = memberRepo.countByCommunityId(communityId);

        if (count >= 5) {
            throw new RuntimeException("Community is full");
        }

        CommunityMember member = new CommunityMember();
        member.setCommunityId(communityId);
        member.setUserId(userId);

        memberRepo.save(member);
    }
}

