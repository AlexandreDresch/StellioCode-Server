package com.stelliocode.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/health")
public class HealthController {
    @GetMapping
    public String healthCheck() {
        return "StellioCode API is up and running!";
    }
}
