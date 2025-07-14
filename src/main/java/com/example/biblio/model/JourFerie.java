package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

@Entity
public class JourFerie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFerie;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    // Getters and Setters

    public Long getIdFerie() {
        return idFerie;
    }

    public void setIdFerie(Long idFerie) {
        this.idFerie = idFerie;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
