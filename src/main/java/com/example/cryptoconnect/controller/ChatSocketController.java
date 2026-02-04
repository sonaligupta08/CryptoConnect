package com.example.cryptoconnect.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.cryptoconnect.entity.ChatMessage;
import com.example.cryptoconnect.entity.Message;
import com.example.cryptoconnect.repository.MessageRepository;

@Controller
public class ChatSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepo;

    @MessageMapping("/chat.send")
    public void send(ChatMessage chat) {

        Message m = new Message();
        m.setSender(chat.getSender());
        m.setReceiver(chat.getReceiver());
        m.setContent(chat.getContent());
        m.setTimestamp(LocalDateTime.now());

        messageRepo.save(m);

        // send to receiver
        messagingTemplate.convertAndSendToUser(
            chat.getReceiver(),
            "/queue/messages",
            chat
        );

        // optional: send back to sender
        messagingTemplate.convertAndSendToUser(
            chat.getSender(),
            "/queue/messages",
            chat
        );
    }
}

