package com.example.biblio.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profil")
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profil")
    private Long id;

    @Column(nullable = false, length = 50)
    private String profil;

    @Column(nullable = false)
    private Integer quota;

    @Column(name = "nbr_jour_pret_penalite", nullable = false)
    private Integer nbrJourPenalite;

    @OneToMany(mappedBy = "profil", cascade = CascadeType.ALL)
    private Set<Adherent> adherents = new HashSet<>();

    // Constructeurs
    public Profil() {}

    public Profil(String profil, Integer quota, Integer nbrJourPenalite) {
        this.profil = profil;
        this.quota = quota;
        this.nbrJourPenalite = nbrJourPenalite;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getNbrJourPenalite() {
        return nbrJourPenalite;
    }

    public Integer getNbrJourPretPenalite() {
        return nbrJourPenalite;
    }

    public void setNbrJourPenalite(Integer nbrJourPenalite) {
        this.nbrJourPenalite = nbrJourPenalite;
    }

    public Set<Adherent> getAdherents() {
        return adherents;
    }

    public void setAdherents(Set<Adherent> adherents) {
        this.adherents = adherents;
    }

    // Méthodes utilitaires
    public void addAdherent(Adherent adherent) {
        adherents.add(adherent);
        adherent.setProfil(this);
    }

    public void removeAdherent(Adherent adherent) {
        adherents.remove(adherent);
        adherent.setProfil(null);
    }
}