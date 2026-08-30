package com.pragma.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/secured")
public class SecuredController {

    @GetMapping("/hello")
    public Map<String, String> hello(Authentication authentication) {
        return Map.of(
                "message", "Hello, secured endpoint!",
                "user", authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Map<String, String> admin(Authentication authentication) {
        return Map.of(
                "message", "Administrative access granted",
                "user", authentication.getName());
    }
}
