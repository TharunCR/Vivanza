package com.event.controller;

import com.event.DTO.SigninRequest;
import com.event.DTO.UserDTO;
import com.event.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Auth management APIs")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@Valid @RequestBody UserDTO request) {
        log.info("Processing user registration for: {}", request.getEmail());
        try {
            // CORRECTED: The locale parameter is removed from the service call
            return ResponseEntity.ok(authenticationService.registration(request));
        } catch (Exception e) {
            log.error("Error processing user registration for request: {}. Error: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody SigninRequest request) {
        log.info("Processing login for: {}", request.getEmail());
        try {
            //  CORRECTED: The locale parameter is removed from the service call
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (Exception e) {
            log.error("Error processing login for {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}