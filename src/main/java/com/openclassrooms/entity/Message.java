package com.openclassrooms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Data
@Entity
public class Message {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String message;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
    
}
