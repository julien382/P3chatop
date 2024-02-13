package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.entity.User;
import com.openclassrooms.services.UserService;
import com.openclassrooms.services.JWTService;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    private JWTService jwtService;

    public AuthController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Utilisez simplement la méthode createUser de UserService pour enregistrer l'utilisateur
        userService.createUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return token;
    }
}
