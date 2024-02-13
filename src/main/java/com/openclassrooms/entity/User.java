package com.openclassrooms.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType; 
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity // Indique que la classe User est une entité JPA, ce qui signifie qu'elle est mappée à une table dans la base de données
public class User {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id; // Déclare un attribut id de type Long

    private String name; 

    private String email; 

    private String password; 

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    // Constructeur par défaut (nécessaire pour JPA)
    public User() {
    }

    // Getters et setters pour les attributs de la classe
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}