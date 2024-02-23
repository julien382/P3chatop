package com.openclassrooms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType; 
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity // Indique que la classe User est une entité JPA, ce qui signifie qu'elle est mappée à une table dans la base de données
@Getter
@Setter
public class User {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id; // Déclare un attribut id de type Long

    @Column
    private String name; 

    @Column
    private String email; 

    @Column
    private String password; 

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    // Constructeur par défaut (nécessaire pour JPA)
    public User() {
    }
}