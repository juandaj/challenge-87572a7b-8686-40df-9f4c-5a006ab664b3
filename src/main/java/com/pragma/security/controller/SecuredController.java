package com.pragma.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secured")
public class SecuredController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, secured endpoint!";
    }
}