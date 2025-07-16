package com.example.biblio.service;

import com.example.biblio.model.Prolongement;
import com.example.biblio.model.Pret;
import com.example.biblio.model.Adherent;
import com.example.biblio.model.Exemplaire;
import com.example.biblio.repository.ProlongementRepository;
import com.example.biblio.repository.PretRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProlongementService {

    private final ProlongementRepository prolongementRepository;
    private final PretRepository pretRepository;

    public ProlongementService(ProlongementRepository prolongementRepository, PretRepository pretRepository) {
        this.prolongementRepository = prolongementRepository;
        this.pretRepository = pretRepository;
    }

    // Créer une demande de prolongement
    @Transactional
    public Prolongement createProlongement(Prolongement prolongement) {
        return prolongementRepository.save(prolongement);
    }

    // // Valider un prolongement
    // @Transactional
    // public Prolongement validerProlongement(Long id) {
    //     Prolongement prolongement = getProlongementById(id);
    //     prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);
    //     return prolongementRepository.save(prolongement);
    // }

    // @Transactional
    // public Prolongement validerProlongement(Long id) {
    //     Prolongement prolongement = getProlongementById(id);
    //     prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);

    //     // Récupérer le prêt original
    //     Pret ancienPret = prolongement.getPret();
    //     Adherent adherent = ancienPret.getAdherent();
    //     Exemplaire exemplaire = ancienPret.getExemplaire();

    //     // Vérifier quota de prêt en cours
    //     long nbPretsEnCours = ancienPret.getAdherent().getPrets().stream()
    //             .filter(p -> p.getStatutPret() == Pret.StatutPret.EN_COURS)
    //             .count();
    //     if (nbPretsEnCours >= adherent.getProfil().getQuota()) {
    //         throw new RuntimeException("Quota de prêt atteint (" + adherent.getProfil().getQuota() + ")");
    //     }

    //     // Créer un nouveau prêt lié à ce prolongement
    //     Pret nouveauPret = new Pret();
    //     nouveauPret.setAdherent(adherent);
    //     nouveauPret.setExemplaire(exemplaire);
    //     nouveauPret.setDateDebut(prolongement.getDateDebut().atStartOfDay()); // ou .now() si souhaité
    //     nouveauPret.setDateRetourPrevue(prolongement.getDateRetourPrevue());
    //     nouveauPret.setTypePret(ancienPret.getTypePret());
    //     nouveauPret.setStatutPret(Pret.StatutPret.EN_COURS);
    //     nouveauPret.setEtatTraitement(Pret.EtatTraitement.VALIDE);

    //     // Sauvegarder le prêt
    //     pretRepository.save(nouveauPret);

    //     // Sauvegarder le prolongement validé
    //     return prolongementRepository.save(prolongement);
    // }
    @Transactional
    public Prolongement validerProlongement(Long id) {
        // Récupérer le prolongement
        Prolongement prolongement = getProlongementById(id);
        prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);

        // Récupérer le prêt original lié
        Pret pret = prolongement.getPret();

        // Vérifier le quota de prêt en cours (facultatif selon logique métier)
        Adherent adherent = pret.getAdherent();
        long nbPretsEnCours = adherent.getPrets().stream()
                .filter(p -> p.getStatutPret() == Pret.StatutPret.EN_COURS)
                .count();
        if (nbPretsEnCours >= adherent.getProfil().getQuota()) {
            throw new RuntimeException("Quota de prêt atteint (" + adherent.getProfil().getQuota() + ")");
        }

        // Mettre à jour la date de retour prévue du prêt
        pret.setDateRetourPrevue(prolongement.getDateRetourPrevue());

        // Sauvegarder les modifications
        pretRepository.save(pret);
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