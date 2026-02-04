package com.example.cryptoconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.cryptoconnect.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
      SELECT m FROM Message m
      WHERE (m.sender = :u1 AND m.receiver = :u2)
         OR (m.sender = :u2 AND m.receiver = :u1)
      ORDER BY m.timestamp
    """)
    List<Message> getChat(
        @Param("u1") String u1,
        @Param("u2") String u2
    );
}
