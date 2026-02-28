package com.example.cryptoconnect.entity;

import jakarta.persistence.*;

@Entity
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long communityId;
    private Long userId;

    private String role; // ADMIN or MEMBER

    public Long getCommunityId() { return communityId; }
    public Long getUserId() { return userId; }
    public String getRole() { return role; }

    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setRole(String role) { this.role = role; }
}