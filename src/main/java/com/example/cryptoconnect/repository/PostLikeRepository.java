package com.example.cryptoconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
 
	Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

	List<PostLike> findByPostId(Long postId);
	
	Long countByPostId(Long postId);
	
	List<PostLike> findByUserId(Long userId);
}
