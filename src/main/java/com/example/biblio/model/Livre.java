package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livre")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livre")
    private Long id;

    @Column(name = "nom_livre", nullable = false, length = 100)
    private String titre;

    @Column(nullable = false, length = 100)
    private String auteur;

    @Column(nullable = false)
    private boolean restreint = false;

    @OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exemplaire> exemplaires = new HashSet<>();

   
    // Constructeurs
    public Livre() {
    }

    public Livre(String titre, String auteur, boolean restreint) {
        this.titre = titre;
        this.auteur = auteur;
        this.restreint = restreint;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public boolean isRestreint() {
        return restreint;
    }

    public void setRestreint(boolean restreint) {
        this.restreint = restreint;
    }

    public Set<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(Set<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }

    // Méthodes utilitaires
    public void addExemplaire(Exemplaire exemplaire) {
        exemplaires.add(exemplaire);
        exemplaire.setLivre(this);
    }

    public void removeExemplaire(Exemplaire exemplaire) {
        exemplaires.remove(exemplaire);
        exemplaire.setLivre(null);
    }
    
}