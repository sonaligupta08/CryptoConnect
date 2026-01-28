package com.example.cryptoconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cryptoconnect.entity.CommunityMember;
import com.example.cryptoconnect.repository.CommunityMemberRepository;
import com.example.cryptoconnect.repository.CommunityRepository;

@Service
public class CommunityService {

    @Autowired
    private CommunityMemberRepository memberRepo;

    @Autowired
    private CommunityRepository communityRepo;

    
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

        
        if (count == 0) {
            member.setRole("ADMIN");
        } else {
            member.setRole("MEMBER");
        }

        memberRepo.save(member);
    }

   
    public String getUserRole(Long communityId, Long userId) {
        return memberRepo.findByCommunityIdAndUserId(communityId, userId)
                .map(CommunityMember::getRole)
                .orElse("NONE");
    }

   
    public void deleteCommunity(Long communityId, Long userId) {

        
        String role = getUserRole(communityId, userId);

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Only ADMIN can delete the community");
        }

    
        memberRepo.deleteByCommunityId(communityId);
        communityRepo.deleteById(communityId);
    }
}
