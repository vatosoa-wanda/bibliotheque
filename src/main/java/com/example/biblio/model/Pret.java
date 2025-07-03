package com.example.biblio.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pret")
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pret")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adherent", nullable = false)
    private Adherent adherent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exemplaire", nullable = false)
    private Exemplaire exemplaire;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut = LocalDateTime.now();

    @Column(name = "date_retour_prevue", nullable = false)
    private LocalDate dateRetourPrevue;

    @Column(name = "date_retour_effective")
    private LocalDate dateRetourEffective;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_pret", length = 20)
    private TypePret typePret;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_pret", length = 20)
    private StatutPret statutPret = StatutPret.EN_DEMANDE;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_traitement", length = 20)
    private EtatTraitement etatTraitement = EtatTraitement.EN_ATTENTE;

    @OneToMany(mappedBy = "pret", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Prolongement> prolongements = new HashSet<>();

    // Enumérations
    public enum TypePret {
        SUR_PLACE, EMPORTE
    }

    public enum StatutPret {
        EN_DEMANDE, EN_COURS, RETOURNE, RETARD
    }

    public enum EtatTraitement {
        EN_ATTENTE, VALIDE, REJETE, ANNULE
    }

    // Constructeurs
    public Pret() {}

    public Pret(Adherent adherent, Exemplaire exemplaire, LocalDate dateRetourPrevue, TypePret typePret) {
        this.adherent = adherent;
        this.exemplaire = exemplaire;
        this.dateRetourPrevue = dateRetourPrevue;
        this.typePret = typePret;
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

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
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

    public TypePret getTypePret() {
        return typePret;
    }

    public void setTypePret(TypePret typePret) {
        this.typePret = typePret;
    }

    public StatutPret getStatutPret() {
        return statutPret;
    }

    public void setStatutPret(StatutPret statutPret) {
        this.statutPret = statutPret;
    }

    public EtatTraitement getEtatTraitement() {
        return etatTraitement;
    }

    public void setEtatTraitement(EtatTraitement etatTraitement) {
        this.etatTraitement = etatTraitement;
    }

    public Set<Prolongement> getProlongements() {
        return prolongements;
    }

    public void setProlongements(Set<Prolongement> prolongements) {
        this.prolongements = prolongements;
    }

    // Méthodes utilitaires
    public void addProlongement(Prolongement prolongement) {
        prolongements.add(prolongement);
        prolongement.setPret(this);
    }

    public void removeProlongement(Prolongement prolongement) {
        prolongements.remove(prolongement);
        prolongement.setPret(null);
    }
}