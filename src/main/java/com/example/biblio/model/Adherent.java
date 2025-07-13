package com.example.biblio.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Period;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "adherent")
public class Adherent implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adherent")
    private Long id;

    @Column(nullable = false, length = 50)
    private String nom;

    @Column(nullable = false, length = 50)
    private String prenom;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String mdp;

    @Column(length = 20)
    private String telephone;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    private String adresse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_profil", nullable = false)
    private Profil profil;

    @Column(nullable = false)
    private boolean actif = false;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription = LocalDateTime.now();

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pret> prets = new HashSet<>();

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Penalisation> penalisations = new HashSet<>();

    @OneToMany(mappedBy = "adherent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Abonnement> abonnements = new HashSet<>();

    // Constructeurs
    public Adherent() {}

    public Adherent(String nom, String prenom, String email, String mdp, LocalDate dateNaissance, Profil profil) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.dateNaissance = dateNaissance;
        this.profil = profil;
    }

    // Getters et setters classiques

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Set<Pret> getPrets() {
        return prets;
    }

    public void setPrets(Set<Pret> prets) {
        this.prets = prets;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Set<Penalisation> getPenalisations() {
        return penalisations;
    }

    public void setPenalisations(Set<Penalisation> penalisations) {
        this.penalisations = penalisations;
    }

    public Set<Abonnement> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(Set<Abonnement> abonnements) {
        this.abonnements = abonnements;
    }

    // Méthodes utilitaires

    public void addPret(Pret pret) {
        prets.add(pret);
        pret.setAdherent(this);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setAdherent(this);
    }

    // Implémentation de UserDetails pour Spring Security

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     // Pas de rôles définis, renvoie une liste vide
    //     return Collections.emptyList();
    // }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_ADHERENT");
    }


    @Override
    public String getPassword() {
        return mdp;
    }

    @Override
    public String getUsername() {
        return email;  // On utilise email comme identifiant
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Pas de gestion d'expiration pour l'instant
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Pas de gestion de verrouillage pour l'instant
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Pas de gestion d'expiration des credentials
    }

    @Override
    public boolean isEnabled() {
        return actif;  // actif=true signifie compte activé
    }
}
