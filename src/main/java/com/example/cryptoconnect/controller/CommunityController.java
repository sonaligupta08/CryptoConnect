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
import org.springframework.web.bind.annotation.RestController;

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
	    
	    //create community
	    @PostMapping("/create")
	    public Community create(@RequestBody Community community, HttpServletRequest request) {
	    	
	    	Object uid = request.getAttribute("userId");
	    	
	    	if(uid == null) {
	    		throw new RuntimeException("Unauthorized");
	    	}
	    	
	    	Long userId = Long.parseLong(uid.toString());
	    	
	    	community.setCreatedBy(userId);
	    	Community saved = communityRepo.save(community);
	    	
	    	CommunityMember m = new CommunityMember();
	    	m.setCommunityId(saved.getId());
	    	m.setRole("ADMIN");
	    	m.setUserId(userId);
	    	
	    	memberRepo.save(m);
	    	return saved;
	    }
	    
	    //get all community
	    @GetMapping("/all")
	    public List<Community> getAll() {
	    	return communityRepo.findAll();
	    }
	     
	    //join community
	    @PostMapping("/{id}/join")
	    public ResponseEntity<?> join(@PathVariable Long id, HttpServletRequest request) { 
	    	
	    	Object uid = request.getAttribute("userId");
	    	
	    	if (uid == null) {
	    		return ResponseEntity.status(401).body("Unauthorized");
	    	}
	    	
	    	Long userId = Long.parseLong(uid.toString());
	    	
	    	if(!communityRepo.existsById(id)) {
	    		return ResponseEntity.badRequest().body("community not found");
	    	}
	    	
	    	if (memberRepo.findByCommunityIdAndUserId(id, userId).isPresent()){
	    		return ResponseEntity.ok("Already joined");
	    	}
	    	
	    	CommunityMember m = new CommunityMember();
	    	m.setCommunityId(id);
	    	m.setUserId(userId);
	    	m.setRole("MEMBER");
	    	
	    	memberRepo.save(m);
	    	return ResponseEntity.ok("joined");
	    	
	    }
	
	    //leave community
	    @PostMapping("/{id}/leave")
	    public ResponseEntity<?> leave(@PathVariable Long id, HttpServletRequest request){
	    	
	    	Object uid = request.getAttribute("userId");
	    	
	    	if(uid == null) {
	    		return ResponseEntity.status(401).body("Unauthorized");
	    	}
	    	
	    	Long userId = Long.parseLong(uid.toString());
	    	
	    	//checking community exists or not
	    	if(!communityRepo.existsById(id)) {
	    		return ResponseEntity.badRequest().body("community not found!!");
	    	}
	    	
	    	//checking membership
	    	var memberOpt = memberRepo.findByCommunityIdAndUserId(id, userId);
	    	
	    	
	    	if(memberOpt.isEmpty()) {
	    		return ResponseEntity.badRequest().body("you are not a member!");
	    	}
	    	
	    	CommunityMember member = memberOpt.get();
	    	
	    	if("ADMIN".equals(member.getRole())) {
	    		return ResponseEntity.badRequest().body("Admin cannot leave. Delete community instead.");
	    	}
	    	memberRepo.delete(member);
			return ResponseEntity.ok("left community");
	    	
	    }
	    
	    //delete community
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request){
	    	
	    	Object uid = request.getAttribute("userId");
	    	
	    	if(uid == null) {
	    		return ResponseEntity.status(401).body("Unauthorized");
	    	}
	    	Long userId = Long.parseLong(uid.toString());
	    	
	    	Community community = communityRepo.findById(id).orElse(null);
	    	
	    	if(community == null) {
	    		return ResponseEntity.badRequest().body("community not found");
	    	}
	    	
	    	if(!community.getCreatedBy().equals(userId)) {
	    		return ResponseEntity.status(403).body("only user can delete");
	    	}
	    	memberRepo.deleteAll(memberRepo.findByCommunityId(id));
	    	communityRepo.delete(community);
			return ResponseEntity.ok("community deleted");
	    	
	    }
	

}
