package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "penalisation")
public class Penalisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penalisation")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    // @Column(nullable = false, length = 20)
    // private String etat = "en_cours"; // valeurs possibles : en_cours, termine

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Etat etat = Etat.EN_COURS;


    public enum Etat {
        EN_COURS, TERMINE
    }

    // Constructeurs
    public Penalisation() {}

    // public Penalisation(Adherent adherent, LocalDate dateDebut, LocalDate dateFin, String etat) {
    //     this.adherent = adherent;
    //     this.dateDebut = dateDebut;
    //     this.dateFin = dateFin;
    //     this.etat = etat;
    // }

    // Constructeur avec enum
public Penalisation(Adherent adherent, LocalDate dateDebut, LocalDate dateFin, Etat etat) {
    this.adherent = adherent;
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    this.etat = etat;
}

// ✅ Getter
public Etat getEtat() {
    return etat;
}

// ✅ Setter
public void setEtat(Etat etat) {
    this.etat = etat;
}


    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    // public String getEtat() {
    //     return etat;
    // }

    // public void setEtat(String etat) {
    //     this.etat = etat;
    // }
}