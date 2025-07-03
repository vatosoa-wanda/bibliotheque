package com.example.biblio.service;

import com.example.biblio.model.Abonnement;
import com.example.biblio.model.Adherent;
import com.example.biblio.repository.AbonnementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AbonnementService {

    private final AbonnementRepository abonnementRepository;

    public AbonnementService(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }

    // Créer un nouvel abonnement
    @Transactional
    public Abonnement creerAbonnement(Adherent adherent, LocalDate dateDebut, LocalDate dateFin) {
        Abonnement abonnement = new Abonnement(adherent, dateDebut, dateFin);
        return abonnementRepository.save(abonnement);
    }

    // Prolonger un abonnement
    @Transactional
    public Abonnement prolongerAbonnement(Long idAbonnement, LocalDate nouvelleDateFin) {
        Abonnement abonnement = getAbonnementById(idAbonnement);
        abonnement.setDateFin(nouvelleDateFin);
        return abonnementRepository.save(abonnement);
    }

    // Désactiver un abonnement
    @Transactional
    public Abonnement desactiverAbonnement(Long idAbonnement) {
        Abonnement abonnement = getAbonnementById(idAbonnement);
        abonnement.setActif(false);
        return abonnementRepository.save(abonnement);
    }

    // Récupérer un abonnement par son ID
    public Abonnement getAbonnementById(Long id) {
        return abonnementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement non trouvé"));
    }

    // Récupérer les abonnements d'un adhérent
    public List<Abonnement> getAbonnementsByAdherent(Long adherentId) {
        return abonnementRepository.findByAdherentId(adherentId);
    }

    // Vérifier si un adhérent a un abonnement actif
    public boolean verifierAbonnementActif(Long adherentId) {
        return abonnementRepository.hasAbonnementActif(adherentId);
    }

    // Récupérer les abonnements actifs
    public List<Abonnement> getAbonnementsActifs() {
        return abonnementRepository.findAbonnementsActifs(LocalDate.now());
    }

    // Récupérer les abonnements expirant dans les X jours
    public List<Abonnement> getAbonnementsExpirantDans(int jours) {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dateLimite = aujourdhui.plusDays(jours);
        return abonnementRepository.findAbonnementsExpirantEntre(aujourdhui, dateLimite);
    }

    // Mettre à jour l'état des abonnements (à exécuter périodiquement)
    @Transactional
    public void mettreAJourEtatAbonnements() {
        List<Abonnement> abonnements = abonnementRepository.findAll();
        LocalDate aujourdhui = LocalDate.now();
        
        abonnements.forEach(abonnement -> {
            boolean etatActuel = abonnement.isActif();
            boolean nouvelEtat = !aujourdhui.isAfter(abonnement.getDateFin());
            
            if (etatActuel != nouvelEtat) {
                abonnement.setActif(nouvelEtat);
                abonnementRepository.save(abonnement);
            }
        });
    }
}