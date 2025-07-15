package com.example.biblio.model;

import java.time.LocalDate;
import java.util.List;

public class AdherentDetailsDTO {
    public Long id;
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public LocalDate dateNaissance;
    public String profil;
    
    public boolean abonnementActif;
    public LocalDate dateDebutAbonnement;
    public LocalDate dateFinAbonnement;

    public int quotaPretRestant;
    public int quotaReservationRestant;
    public int quotaProlongementRestant;

    public List<PenalisationDTO> penalites;

    // Sous-DTO
    public static class PenalisationDTO {
        public LocalDate dateDebut;
        public LocalDate dateFin;
        public String etat;
    }
}
