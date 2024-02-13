package com.openclassrooms.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;

@Entity
public class Message {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id;

    private Rental rental;

    private User user;

    private String message;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
    
}
