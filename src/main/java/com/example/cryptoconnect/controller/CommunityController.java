package com.example.cryptoconnect.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import com.example.cryptoconnect.entity.Community;
import com.example.cryptoconnect.service.CommunityService;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService service;

    public CommunityController(CommunityService service) {
        this.service = service;
    }
    
    @GetMapping("/all")
    public List<Community> getAllCommunities() {
        return service.getAllCommunities();
    }

    @PostMapping("/create")
    public Community create(@RequestBody Map<String, Object> data) {

        String name = (String) data.get("name");
        String description = (String) data.get("description");
        Long userId = Long.valueOf(data.get("userId").toString());

        return service.createCommunity(name, description, userId);
    }

    @PostMapping("/{id}/join")
    public String join(@PathVariable Long id,
                       @RequestParam Long userId) {

        return service.joinCommunity(id, userId);
    }
    
    @GetMapping("/user/{userId}")
    public List<Community> getUserCommunities(@PathVariable Long userId) {
        return service.getUserCommunities(userId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam Long userId) {

        return service.deleteCommunity(id, userId);
    }
}