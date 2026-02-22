package com.example.cryptoconnect.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.cryptoconnect.entity.ChatMessage;
import com.example.cryptoconnect.entity.Message;
import com.example.cryptoconnect.repository.MessageRepository;
import com.example.cryptoconnect.repository.UserRepository;

@Controller
public class ChatSocketController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private MessageRepository messageRepo;
	
	@Autowired
	private UserRepository userRepository;

	@MessageMapping("/chat.send")
	public void send(ChatMessage chat) {

	    String sender = chat.getSender().trim().toLowerCase();
	    String receiver = chat.getReceiver().trim().toLowerCase();

	    if (!userRepository.existsByUsername(receiver)) {

	        messagingTemplate.convertAndSendToUser(
	            sender,
	            "/queue/errors",
	            "User does not exist"
	        );

	        return;
	    }

	    Message m = new Message();
	    m.setSender(sender);
	    m.setReceiver(receiver);
	    m.setContent(chat.getContent());
	    m.setTimestamp(LocalDateTime.now());
	    m.setSeen(false);

	    Message saved = messageRepo.save(m);

	    messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", saved);
	    messagingTemplate.convertAndSendToUser(sender, "/queue/messages", saved);
	}

}
