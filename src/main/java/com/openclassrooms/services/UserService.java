package com.openclassrooms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.entity.User;
import com.openclassrooms.repository.UserRepository;

@Service
public class UserService {

    // Déclaration des dépendances nécessaires
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Méthode pour enregistrer un nouvel utilisateur
    public User createUser(User user) {
        // Vérifiez si l'utilisateur existe déjà dans la base de données
        if (userRepository.findByUsername(user.getName()) != null) {
            throw new RuntimeException("Username already exists");
        }

        user.setName(user.getName()); // Ajouter le name de l'utilisateur
        user.setEmail(user.getEmail()); // Ajouter l'e-mail de l'utilisateur
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hasher le mot de passe avant de l'enregistrer
        return userRepository.save(user); // Enregistrez l'utilisateur dans la base de données
    }

    // Méthode pour authentifier un utilisateur
    public User authenticateUser(String username, String password) {
        // Récupérer l'utilisateur à partir de son nom d'utilisateur
        User user = userRepository.findByUsername(username);
        
        // Vérifier si l'utilisateur existe
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Vérifier si le mot de passe fourni correspond au mot de passe hashé stocké
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Authentification réussie
        return user;
    }

    // Méthode pour récupérer un utilisateur par son ID
    @SuppressWarnings("null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Méthode pour supprimer un utilisateur
    @SuppressWarnings("null")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
