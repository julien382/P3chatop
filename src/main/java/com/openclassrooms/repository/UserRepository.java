package com.openclassrooms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Méthode pour trouver un utilisateur par son nom d'utilisateur
    User findByName(String name);
    Optional<User> findByEmail(String email);
}
