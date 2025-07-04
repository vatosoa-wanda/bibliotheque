package com.example.biblio.service;

import com.example.biblio.model.*;
import com.example.biblio.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class PretService {
    private final PretRepository pretRepository;
    private final LivreRepository livreRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final AdherentRepository adherentRepository;
    private final PenalisationRepository penalisationRepository;
    private final AbonnementRepository abonnementRepository;

    // public PretService(PretRepository pretRepository) {
    //     this.pretRepository = pretRepository;
    // }
    public PretService(PretRepository pretRepository, LivreRepository livreRepository,
                     ExemplaireRepository exemplaireRepository, AdherentRepository adherentRepository,
                     PenalisationRepository penalisationRepository, AbonnementRepository abonnementRepository) {
        this.pretRepository = pretRepository;
        this.livreRepository = livreRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.adherentRepository = adherentRepository;
        this.penalisationRepository = penalisationRepository;
        this.abonnementRepository = abonnementRepository;
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


    @Transactional
    public Pret demanderPret(String titre, String auteur, Adherent adherent) {
        // 1. Vérifier que le livre existe
        Livre livre = livreRepository.findByTitreAndAuteur(titre, auteur)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        // 2. Vérifier qu'il y a un exemplaire disponible
        Exemplaire exemplaire = exemplaireRepository.findFirstByLivreIdAndDisponibleTrue(livre.getId())
                .orElseThrow(() -> new RuntimeException("Aucun exemplaire disponible pour ce livre"));

        // 3. Vérifier que l'adhérent n'a pas de sanction en cours
        if (penalisationRepository.existsByAdherentIdAndEtat(adherent.getId(), Penalisation.Etat.EN_COURS)) {
            throw new RuntimeException("Vous avez une sanction en cours");
        }

        // 4. Vérifier le quota d'emprunt
        long nbPretsEnCours = pretRepository.countByAdherentIdAndStatutPret(adherent.getId(), Pret.StatutPret.EN_COURS);
        if (nbPretsEnCours >= adherent.getProfil().getQuota()) {
            throw new RuntimeException("Quota d'emprunt atteint (" + adherent.getProfil().getQuota() + " livres maximum)");
        }

        // 5. Vérifier si le livre est restreint (âge < 18 ans)
        if (livre.isRestreint() && adherent.getAge() < 18) {
            throw new RuntimeException("Ce livre est restreint aux mineurs");
        }

        // 6. Vérifier que l'adhérent est abonné
        if (!abonnementRepository.existsByAdherentIdAndDateFinAfter(adherent.getId(), LocalDate.now())) {
            throw new RuntimeException("Votre abonnement n'est pas valide");
        }

        // Créer le prêt
        Pret pret = new Pret();
        pret.setAdherent(adherent);
        pret.setExemplaire(exemplaire);
        pret.setDateRetourPrevue(LocalDate.now().plusDays(adherent.getProfil().getNbrJourPretPenalite()));
        pret.setTypePret(Pret.TypePret.EMPORTE);
        pret.setStatutPret(Pret.StatutPret.EN_DEMANDE);
        pret.setEtatTraitement(Pret.EtatTraitement.EN_ATTENTE);

        // Marquer l'exemplaire comme indisponible
        exemplaire.setDisponible(false);
        exemplaireRepository.save(exemplaire);

        return pretRepository.save(pret);
    }


}



    
