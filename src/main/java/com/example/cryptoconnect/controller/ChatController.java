package com.example.cryptoconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;


import com.example.cryptoconnect.entity.ChatMessage;

@Controller
public class ChatController {
	
	 @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	 
	  @MessageMapping("/chat.send")
	    public void sendMessage(ChatMessage message) {
	        messagingTemplate.convertAndSendToUser(
	                message.getReceiver(),
	                "/queue/messages",
	                message
	        );
	    }
	  
	  @MessageMapping("/notify")
	    public void sendNotification(ChatMessage message) {
	        messagingTemplate.convertAndSend("/topic/notifications", message);
	    }

}
