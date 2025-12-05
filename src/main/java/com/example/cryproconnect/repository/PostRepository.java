package com.example.cryproconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryproconnect.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
 
	List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);
	List<Post> findAllByOrderByCreatedAtDesc();

}
