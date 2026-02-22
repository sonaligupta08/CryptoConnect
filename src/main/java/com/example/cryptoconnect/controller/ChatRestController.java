package com.example.cryptoconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.cryptoconnect.entity.Message;
import com.example.cryptoconnect.repository.MessageRepository;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatRestController {

    @Autowired
    private MessageRepository repo;

    @GetMapping("/{user1}/{user2}")
    public List<Message> getChat(
            @PathVariable String user1,
            @PathVariable String user2
    ) {
        user1 = user1.toLowerCase();
        user2 = user2.toLowerCase();

        repo.markAsSeen(user2, user1);
        return repo.getChat(user1, user2);
    }
}