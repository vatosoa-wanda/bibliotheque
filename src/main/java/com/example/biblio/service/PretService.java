package com.example.biblio.service;

import com.example.biblio.model.Pret;
import com.example.biblio.model.Prolongement;
import com.example.biblio.repository.PretRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PretService {

    private final PretRepository pretRepository;

    public PretService(PretRepository pretRepository) {
        this.pretRepository = pretRepository;
    }

    // Créer un nouveau prêt
    @Transactional
    public Pret createPret(Pret pret) {
        pret.setDateDebut(LocalDateTime.now());
        pret.setStatutPret(Pret.StatutPret.EN_DEMANDE);
        pret.setEtatTraitement(Pret.EtatTraitement.EN_ATTENTE);
        return pretRepository.save(pret);
    }

    // Valider un prêt
    @Transactional
    public Pret validerPret(Long id) {
        Pret pret = getPretById(id);
        pret.setStatutPret(Pret.StatutPret.EN_COURS);
        pret.setEtatTraitement(Pret.EtatTraitement.VALIDE);
        pret.getExemplaire().setDisponible(false);
        return pretRepository.save(pret);
    }

    // Rejeter un prêt
    @Transactional
    public Pret rejeterPret(Long id) {
        Pret pret = getPretById(id);
        pret.setEtatTraitement(Pret.EtatTraitement.REJETE);
        return pretRepository.save(pret);
    }

    // Enregistrer le retour d'un prêt
    @Transactional
    public Pret retournerPret(Long id, LocalDate dateRetour) {
        Pret pret = getPretById(id);
        pret.setDateRetourEffective(dateRetour);
        pret.setStatutPret(Pret.StatutPret.RETOURNE);
        pret.getExemplaire().setDisponible(true);
        
        // Vérifier si le retour est en retard
        if (dateRetour.isAfter(pret.getDateRetourPrevue())) {
            pret.setStatutPret(Pret.StatutPret.RETARD);
        }
        
        return pretRepository.save(pret);
    }

    // Prolonger un prêt
    @Transactional
    public Pret prolongerPret(Long pretId, LocalDate nouvelleDateRetour) {
        Pret pret = getPretById(pretId);
        pret.setDateRetourPrevue(nouvelleDateRetour);
        
        // Créer un historique de prolongement
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setDateDebut(LocalDate.now());
        prolongement.setDateRetourPrevue(nouvelleDateRetour);
        prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);
        
        pret.addProlongement(prolongement);
        return pretRepository.save(pret);
    }

    // Récupérer un prêt par son ID
    public Pret getPretById(Long id) {
        return pretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prêt non trouvé"));
    }

    // Récupérer tous les prêts d'un adhérent
    public List<Pret> getPretsByAdherent(Long adherentId) {
        return pretRepository.findByAdherentId(adherentId);
    }

    // Récupérer les prêts en cours
    public List<Pret> getPretsEnCours() {
        return pretRepository.findByStatutPret(Pret.StatutPret.EN_COURS);
    }

    // Récupérer les prêts en retard
    public List<Pret> getPretsEnRetard() {
        return pretRepository.findByStatutPretAndDateRetourPrevueBefore(
            Pret.StatutPret.EN_COURS, 
            LocalDate.now()
        );
    }

    // Vérifier si un exemplaire est déjà emprunté
    public boolean isExemplaireEmprunte(Long exemplaireId) {
        return pretRepository.existsByExemplaireIdAndStatutPret(
            exemplaireId, 
            Pret.StatutPret.EN_COURS
        );
    }
}