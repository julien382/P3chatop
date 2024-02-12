package com.openclassrooms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.entity.User;
import com.openclassrooms.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Méthode pour enregistrer un nouvel utilisateur
    public User createUser(User user) {
        // Vérifiez si l'utilisateur existe déjà dans la base de données
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        // Hasher le mot de passe avant de l'enregistrer
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Enregistrez l'utilisateur dans la base de données
        return userRepository.save(user);
    }

    // Méthode pour récupérer un utilisateur par son ID
    @SuppressWarnings("null")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Méthode pour récupérer un utilisateur par son nom d'utilisateur
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Méthode pour mettre à jour les informations de l'utilisateur
    @SuppressWarnings("null")
    public User updateUser(User user) {
        // Vous pouvez implémenter la logique de mise à jour selon vos besoins
        // Par exemple, vous pouvez vérifier si l'utilisateur existe dans la base de données
        // et ensuite mettre à jour ses informations
        // Ici, nous supposons simplement que l'utilisateur existe déjà dans la base de données
        return userRepository.save(user);
    }

    // Méthode pour supprimer un utilisateur
    @SuppressWarnings("null")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
