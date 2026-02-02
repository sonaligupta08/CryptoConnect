package com.example.cryptoconnect.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	  @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	  
	    public void notifyDelete(Long postId, String username) {
	        messagingTemplate.convertAndSend(
	            "/topic/notifications",
	            Map.of(
	                "type", "POST_DELETED",
	                "postId", postId,
	                "message", "A post by @" + username + " was deleted"
	            )
	        );
	    }

}
