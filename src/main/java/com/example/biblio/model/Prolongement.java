package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "prolongement")
public class Prolongement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prolong")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret pret;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_effective")
    private LocalDate dateRetourEffective;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_traitement", length = 20)
    private EtatTraitement etatTraitement = EtatTraitement.EN_ATTENTE;

    // Enumération
    public enum EtatTraitement {
        EN_ATTENTE, VALIDE, REJETE, ANNULE
    }

    // Constructeurs
    public Prolongement() {
    }

    public Prolongement(Pret pret, LocalDate dateDebut, LocalDate dateRetourPrevue) {
        this.pret = pret;
        this.dateDebut = dateDebut;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public EtatTraitement getEtatTraitement() {
        return etatTraitement;
    }

    public void setEtatTraitement(EtatTraitement etatTraitement) {
        this.etatTraitement = etatTraitement;
    }
}