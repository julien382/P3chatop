package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.services.UserService;
import com.openclassrooms.services.JWTService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private JWTService jwtService;

    public AuthController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody RegisterDTO registerDTO) {
        // Utilisez simplement la méthode createUser de UserService pour enregistrer l'utilisateur
        userService.registerNewUser(registerDTO);

        String token = jwtService.generateToken(new UsernamePasswordAuthenticationToken(registerDTO.getName(), registerDTO.getPassword()));

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return token;
    }

    /*@PostMapping("/me")*/
    
}
