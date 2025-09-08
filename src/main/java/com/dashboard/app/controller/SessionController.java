package com.dashboard.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @GetMapping("/session/status")
    public boolean sessionStatus(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
