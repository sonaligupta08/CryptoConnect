package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.cryptoconnect.entity.Community;
import com.example.cryptoconnect.entity.CommunityMember;
import com.example.cryptoconnect.repository.CommunityMemberRepository;
import com.example.cryptoconnect.repository.CommunityPostRepository;
import com.example.cryptoconnect.repository.CommunityRepository;
import com.example.cryptoconnect.service.CommunityService;
@RestController
@RequestMapping("/api/community")
@CrossOrigin("*")
public class CommunityController {

    @Autowired
    private CommunityRepository communityRepo;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityMemberRepository memberRepo;

    @Autowired
    private CommunityPostRepository postRepo;


    @PostMapping("/create")
    public Community create(@RequestBody Community community) {

        Community saved = communityRepo.save(community);

        CommunityMember member = new CommunityMember();
        member.setCommunityId(saved.getId());
        member.setUserId(community.getAdminId());
        member.setRole("ADMIN");
        memberRepo.save(member);

        return saved;
    }


    @GetMapping("/{communityId}")
    public Community getCommunity(@PathVariable Long communityId) {
        return communityRepo.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));
    }

  
    @PostMapping("/add-member/{communityId}/{userId}")
    public void addMember(@PathVariable Long communityId, @PathVariable Long userId) {
        communityService.addMember(communityId, userId);
    }

    @GetMapping("/user/{userId}")
    public List<CommunityMember> userCommunities(@PathVariable Long userId) {
        return memberRepo.findByUserId(userId);
    }

   
    @GetMapping("/all")
    public List<Community> getAll() {
        return communityRepo.findAll();
    }

 
    @GetMapping("/member-count/{communityId}")
    public int getMemberCount(@PathVariable Long communityId) {
        return memberRepo.countByCommunityId(communityId);
    }

   
    @GetMapping("/is-member/{communityId}/{userId}")
    public boolean isMember(@PathVariable Long communityId, @PathVariable Long userId) {
        return memberRepo.existsByCommunityIdAndUserId(communityId, userId);
    }

  
    @DeleteMapping("/leave/{communityId}/{userId}")
    public ResponseEntity<?> leaveCommunity(
            @PathVariable Long communityId,
            @PathVariable Long userId) {

        memberRepo.deleteByCommunityIdAndUserId(communityId, userId);
        return ResponseEntity.ok().build();
    }

  
    @DeleteMapping("/{communityId}")
    public ResponseEntity<?> deleteCommunity(
            @PathVariable Long communityId,
            @RequestParam Long userId) {


        String role = communityService.getUserRole(communityId, userId);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body("Only ADMIN can delete community");
        }

        postRepo.deleteByCommunityId(communityId);
        memberRepo.deleteByCommunityId(communityId);
        communityRepo.deleteById(communityId);

        return ResponseEntity.ok().build();
    }

 
    @GetMapping("/role/{communityId}/{userId}")
    public String getUserRole(
            @PathVariable Long communityId,
            @PathVariable Long userId) {

        return communityService.getUserRole(communityId, userId);
    }
}
