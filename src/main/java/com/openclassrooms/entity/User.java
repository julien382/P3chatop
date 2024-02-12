package com.openclassrooms.entity; // Définit le package auquel appartient la classe

import javax.persistence.Entity; // Importe l'annotation Entity de javax.persistence pour définir la classe comme une entité JPA
import javax.persistence.GeneratedValue; // Importe l'annotation GeneratedValue de javax.persistence pour spécifier la génération automatique de la valeur de la clé primaire
import javax.persistence.GenerationType; // Importe l'enum GenerationType de javax.persistence pour spécifier la stratégie de génération de la clé primaire
import javax.persistence.Id; // Importe l'annotation Id de javax.persistence pour spécifier la clé primaire de l'entité

@Entity // Indique que la classe User est une entité JPA, ce qui signifie qu'elle est mappée à une table dans la base de données
public class User {

    @Id // Indique que l'attribut id est la clé primaire de l'entité
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la valeur de l'attribut id est générée automatiquement par la base de données
    private Long id; // Déclare un attribut id de type Long

    private String username; // Déclare un attribut username de type String
    private String password; // Déclare un attribut password de type String

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}