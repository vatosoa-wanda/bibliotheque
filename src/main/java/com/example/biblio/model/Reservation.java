package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Long id;

    @Column(name = "date_reservation")
    private LocalDateTime dateReservation = LocalDateTime.now();

    @Column(name = "date_a_reserver", nullable = false)
    private LocalDateTime dateAReserver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exemplaire", nullable = false)
    private Exemplaire exemplaire;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EtatReservation etat = EtatReservation.EN_COURS;

    public enum EtatReservation {
        EN_COURS,
        VALIDE,
        ANNULE
    }

    // Constructeurs
    public Reservation() {}

    public Reservation(Adherent adherent, Exemplaire exemplaire, LocalDateTime dateAReserver) {
        this.adherent = adherent;
        this.exemplaire = exemplaire;
        this.dateAReserver = dateAReserver;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public LocalDateTime getDateAReserver() {
        return dateAReserver;
    }

    public void setDateAReserver(LocalDateTime dateAReserver) {
        this.dateAReserver = dateAReserver;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public EtatReservation getEtat() {
        return etat;
    }

    public void setEtat(EtatReservation etat) {
        this.etat = etat;
    }
}