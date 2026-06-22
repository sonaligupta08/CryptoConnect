package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cryptoconnect.entity.Community;
import com.example.cryptoconnect.entity.CommunityMember;
import com.example.cryptoconnect.repository.CommunityMemberRepository;
import com.example.cryptoconnect.repository.CommunityRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/communities")
@CrossOrigin("*")
public class CommunityController {

    @Autowired
    private CommunityRepository communityRepo;

    @Autowired
    private CommunityMemberRepository memberRepo;

    // ✅ CREATE
    @PostMapping("/create")
    public Community create(@RequestBody Community community, HttpServletRequest request) {

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        community.setCreatedBy(userId);
        Community saved = communityRepo.save(community);

        CommunityMember m = new CommunityMember();
        m.setCommunityId(saved.getId());
        m.setUserId(userId);
        m.setRole("ADMIN");

        memberRepo.save(m);

        return saved;
    }

    // ✅ GET ALL
    @GetMapping("/all")
    public List<Community> getAll() {
        return communityRepo.findAll();
    }

    // ✅ GET USER COMMUNITIES
    @GetMapping("/user/{userId}")
    public List<CommunityMember> getUserCommunities(@PathVariable Long userId) {
        return memberRepo.findByUserId(userId);
    }

    // ✅ JOIN
    @PostMapping("/{id}/join")
    public ResponseEntity<?> join(@PathVariable Long id, HttpServletRequest request) {

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        if (memberRepo.findByCommunityIdAndUserId(id, userId).isPresent()) {
            return ResponseEntity.ok("Already joined");
        }

        CommunityMember m = new CommunityMember();
        m.setCommunityId(id);
        m.setUserId(userId);
        m.setRole("MEMBER");

        memberRepo.save(m);
        return ResponseEntity.ok("Joined");
    }

    // ✅ LEAVE
    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leave(@PathVariable Long id, HttpServletRequest request) {

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        var memberOpt = memberRepo.findByCommunityIdAndUserId(id, userId);

        if (memberOpt.isEmpty()) return ResponseEntity.badRequest().body("Not a member");

        CommunityMember member = memberOpt.get();

        if ("ADMIN".equals(member.getRole())) {
            return ResponseEntity.badRequest().body("Admin cannot leave");
        }

        memberRepo.delete(member);
        return ResponseEntity.ok("Left");
    }

    // ✅ DELETE (ONLY CREATOR)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {

        Long userId = Long.parseLong(request.getAttribute("userId").toString());

        Community community = communityRepo.findById(id).orElse(null);

        if (community == null) return ResponseEntity.badRequest().body("Not found");

        if (!community.getCreatedBy().equals(userId)) {
            return ResponseEntity.status(403).body("Only creator can delete");
        }

        memberRepo.deleteAll(memberRepo.findByCommunityId(id));
        communityRepo.delete(community);

        return ResponseEntity.ok("Deleted");
    }

    // ✅ MEMBER COUNT
    @GetMapping("/{id}/members/count")
    public Long getMemberCount(@PathVariable Long id) {
        return (long) memberRepo.findByCommunityId(id).size();
    }
}