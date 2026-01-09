package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Long> {

	List<Community> findByAdminId(Long adminId);
}
