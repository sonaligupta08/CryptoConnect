package com.example.cryptoconnect.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        String username = UriComponentsBuilder
                .fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("username");

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username required for websocket");
        }

        String normalized = username.trim().toLowerCase();
        System.out.println("WS USER = " + normalized);

        return () -> normalized;
    }
}