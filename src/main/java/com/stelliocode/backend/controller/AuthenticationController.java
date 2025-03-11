package com.stelliocode.backend.controller;

import com.stelliocode.backend.dto.*;
import com.stelliocode.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerDeveloper(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register-admin")
    public RegisterResponseDTO registerAdmin(@RequestBody RegisterRequestDTO request) {
        return authenticationService.registerAdmin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseBaseDTO> authenticate(@Valid @RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
