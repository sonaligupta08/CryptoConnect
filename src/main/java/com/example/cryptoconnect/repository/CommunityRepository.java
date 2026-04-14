package com.example.cryptoconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {
	
}
