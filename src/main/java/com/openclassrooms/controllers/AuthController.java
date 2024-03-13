package com.openclassrooms.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;*/
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.dto.LoginDTO;
import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.dto.UserDTO;
import com.openclassrooms.entity.User;
import com.openclassrooms.services.UserService;
import com.openclassrooms.services.JWTService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private JWTService jwtService;

   /*  @Autowired     
    private AuthenticationManager authenticationManager;*/

    public AuthController(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDTO registerDTO) {
        // Utilisez simplement la méthode register de UserService pour enregistrer l'utilisateur
        /*userService.registerNewUser(registerDTO);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(registerDTO.getEmail());
        loginDTO.setPassword(registerDTO.getPassword());*/
        User user = userService.registerNewUser(registerDTO);    
        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

/*
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }*/
    
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        //Authentication authentication = userService.authenticateUser(loginDTO);
        //String token = jwtService.generateToken(authentication);
        User user = userService.loginUser(loginDTO); 
        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    } 

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("log authentication: {}", authentication);
            String userEmail = authentication.getName();
            logger.info("User email: {}", userEmail);
            UserDTO userDTO = userService.getCurrentUser(authentication);
            logger.info("User DTO: {}", userDTO);
            return ResponseEntity.ok(userDTO);
        } else {
            // Gérer le cas où l'utilisateur n'est pas authentifié
            logger.warn("User not authenticated");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*  @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO userDTO = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(userDTO);
    }
*/
    
}
