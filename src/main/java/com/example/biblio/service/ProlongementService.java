package com.example.biblio.service;

import com.example.biblio.model.Prolongement;
import com.example.biblio.repository.ProlongementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProlongementService {

    private final ProlongementRepository prolongementRepository;

    public ProlongementService(ProlongementRepository prolongementRepository) {
        this.prolongementRepository = prolongementRepository;
    }

    // Créer une demande de prolongement
    @Transactional
    public Prolongement createProlongement(Prolongement prolongement) {
        return prolongementRepository.save(prolongement);
    }

    // Valider un prolongement
    @Transactional
    public Prolongement validerProlongement(Long id) {
        Prolongement prolongement = getProlongementById(id);
        prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);
        return prolongementRepository.save(prolongement);
    }

    // Rejeter un prolongement
    @Transactional
    public Prolongement rejeterProlongement(Long id) {
        Prolongement prolongement = getProlongementById(id);
        prolongement.setEtatTraitement(Prolongement.EtatTraitement.REJETE);
        return prolongementRepository.save(prolongement);
    }

    // Marquer un prolongement comme retourné
    @Transactional
    public Prolongement marquerCommeRetourne(Long id, LocalDate dateRetourEffective) {
        Prolongement prolongement = getProlongementById(id);
        prolongement.setDateRetourEffective(dateRetourEffective);
        prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);
        return prolongementRepository.save(prolongement);
    }

    // Récupérer un prolongement par son ID
    public Prolongement getProlongementById(Long id) {
        return prolongementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prolongement non trouvé"));
    }

    // Récupérer tous les prolongements d'un prêt
    public List<Prolongement> getProlongementsByPret(Long pretId) {
        return prolongementRepository.findByPretId(pretId);
    }

    // Récupérer les prolongements en attente
    public List<Prolongement> getProlongementsEnAttente() {
        return prolongementRepository.findByEtatTraitement(Prolongement.EtatTraitement.EN_ATTENTE);
    }

    // Vérifier si un prêt a déjà une demande en attente
    public boolean hasProlongementEnAttente(Long pretId) {
        return prolongementRepository.existsByPretIdAndEtatTraitement(
            pretId, 
            Prolongement.EtatTraitement.EN_ATTENTE
        );
    }
}