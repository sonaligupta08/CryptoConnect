package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cryptoconnect.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findByUserId(Long userId);

}