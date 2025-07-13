package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "abonnement")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonnement")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "actif", nullable = false)
    private boolean actif;

    // Constructeurs
    public Abonnement() {
        this.actif = false; // Par défaut inactif jusqu'à initialisation des dates
    }

    public Abonnement(Adherent adherent, LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        
        this.adherent = adherent;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.actif = calculerEtatActif(); // Calcul initial basé sur les dates
    }

    // Getters & Setters
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
        this.actif = calculerEtatActif();
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        this.actif = calculerEtatActif();
    }

    public boolean isActif() {
        return calculerEtatActif(); // Toujours calculé dynamiquement
    }

    public void setActif(boolean actif) {
        // On ne permet pas de forcer manuellement l'état actif
        // car il doit être déterminé par les dates
        throw new UnsupportedOperationException("L'état actif est déterminé automatiquement par les dates");
    }

    // Méthodes utilitaires
    private boolean calculerEtatActif() {
        if (dateDebut == null || dateFin == null) {
            return false;
        }
        LocalDate aujourdhui = LocalDate.now();
        return !aujourdhui.isBefore(dateDebut) && !aujourdhui.isAfter(dateFin);
    }

    public boolean estValide() {
        return calculerEtatActif();
    }

    // Méthode pour vérifier si l'abonnement sera actif à une date donnée
    public boolean seraActifA(LocalDate date) {
        if (dateDebut == null || dateFin == null) {
            return false;
        }
        return !date.isBefore(dateDebut) && !date.isAfter(dateFin);
    }
}