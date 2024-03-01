package com.openclassrooms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.dto.LoginDTO;
import com.openclassrooms.dto.RegisterDTO;
import com.openclassrooms.dto.UserDTO;
import com.openclassrooms.entity.User;
import com.openclassrooms.repository.UserRepository;

@Service
public class UserService {

    // Déclaration des dépendances nécessaires
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Méthode pour enregistrer un nouvel utilisateur
    public User registerNewUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        return userRepository.save(user);
    }

    // Méthode pour authentifier un utilisateur
    public Authentication authenticateUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication; // Authentification réussie
    }

    // Méthode pour récupérer les infos d'un utilisateur
    public UserDTO getCurrentUser(Authentication authentication) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(authentication.getName()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setCreated_at(user.getCreated_at());
        userDTO.setUpdated_at(user.getUpdated_at());
        return userDTO;
    }

    // Méthode pour récupérer un utilisateur par son ID
    @SuppressWarnings("null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
