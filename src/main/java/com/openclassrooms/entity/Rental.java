package com.openclassrooms.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;

@Entity
public class Rental {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id;

    private String name;

    private Integer surface;

    private Integer price;

    private String picture;

    private String description;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
    
}
