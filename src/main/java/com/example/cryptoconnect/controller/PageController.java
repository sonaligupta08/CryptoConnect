package com.example.cryptoconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	
	@GetMapping("/login")
     public String login() {
    	 return "redirect:/login.html";
     }
	
	@GetMapping("/signup")
	public 	String signup() {
		return "redirect:/signup.html";
	}
}