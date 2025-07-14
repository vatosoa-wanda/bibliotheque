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
    private final ProlongementRepository prolongementRepository;
    private final ReservationRepository reservationRepository;
    private final JourFerieService jourFerieService;

    // public PretService(PretRepository pretRepository) {
    //     this.pretRepository = pretRepository;
    // }
    public PretService(PretRepository pretRepository, LivreRepository livreRepository,
                     ExemplaireRepository exemplaireRepository, AdherentRepository adherentRepository,
                     PenalisationRepository penalisationRepository, AbonnementRepository abonnementRepository,
                     ProlongementRepository prolongementRepository, ReservationRepository reservationRepository,
                     JourFerieService jourFerieService) {
        this.pretRepository = pretRepository;
        this.livreRepository = livreRepository;
        this.exemplaireRepository = exemplaireRepository;
        this.adherentRepository = adherentRepository;
        this.penalisationRepository = penalisationRepository;
        this.abonnementRepository = abonnementRepository;
        this.prolongementRepository = prolongementRepository;
        this.reservationRepository = reservationRepository;
        this.jourFerieService = jourFerieService;
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
        // 1. Vérifier que le livre existe (recherche flexible)
        Livre livre;

        boolean hasTitre = titre != null && !titre.isBlank();
        boolean hasAuteur = auteur != null && !auteur.isBlank();

        if (hasTitre && hasAuteur) {
            livre = livreRepository.findByTitreAndAuteur(titre, auteur)
                    .orElseThrow(() -> new RuntimeException("Livre non trouvé avec ce titre et cet auteur"));
        } else if (hasTitre) {
            livre = livreRepository.findByTitre(titre)
                    .orElseThrow(() -> new RuntimeException("Livre non trouvé avec ce titre"));
        } else if (hasAuteur) {
            livre = livreRepository.findByAuteur(auteur)
                    .orElseThrow(() -> new RuntimeException("Livre non trouvé avec cet auteur"));
        } else {
            throw new RuntimeException("Vous devez fournir au moins un titre ou un auteur");
        }


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

        // Avant
        // pret.setDateRetourPrevue(LocalDate.now().plusDays(adherent.getProfil().getNbrJourPretPenalite()));
        // Apres
        // Gestion date retour prevu si jour ferie
        LocalDate retourInitial = LocalDate.now().plusDays(adherent.getProfil().getNbrJourPretPenalite());
        LocalDate retourAjuste = jourFerieService.ajusterSiNonOuvrable(retourInitial);
        pret.setDateRetourPrevue(retourAjuste);

        pret.setTypePret(Pret.TypePret.EMPORTE);
        pret.setStatutPret(Pret.StatutPret.EN_DEMANDE);
        pret.setEtatTraitement(Pret.EtatTraitement.EN_ATTENTE);

        // Marquer l'exemplaire comme indisponible
        exemplaire.setDisponible(false);
        exemplaireRepository.save(exemplaire);

        return pretRepository.save(pret);
    }


    @Transactional
    public Pret prolongerPret(Long pretId) {
        // 1. Vérifier que le prêt existe et est en cours
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt introuvable"));
        
        if (pret.getStatutPret() != Pret.StatutPret.EN_COURS) {
            throw new RuntimeException("Seuls les prêts en cours peuvent être prolongés");
        }

        Adherent adherent = pret.getAdherent();
        Exemplaire exemplaire = pret.getExemplaire();

        // 2. Vérifications de base
        if (exemplaire == null) {
            throw new RuntimeException("Exemplaire introuvable");
        }
        if (penalisationRepository.existsByAdherentIdAndEtat(adherent.getId(), Penalisation.Etat.EN_COURS)) {
            throw new RuntimeException("Vous avez une sanction en cours");
        }
        if (!abonnementRepository.existsByAdherentIdAndDateFinAfter(adherent.getId(), LocalDate.now())) {
            throw new RuntimeException("Abonnement non valide");
        }

        // 3. Vérifier les réservations (version corrigée)
        LocalDateTime maintenant = LocalDateTime.now();
        if (reservationRepository.existsByExemplaireAndEtatValideAndDate(
                exemplaire, 
                maintenant)) {
            throw new RuntimeException("L'exemplaire est réservé");
        }

        // 4. Vérifier le quota de prolongement de l'adhérent (version corrigée)
        long nbProlongementsValides = prolongementRepository.countByPretAdherentAndEtatTraitement(
                adherent, 
                Prolongement.EtatTraitement.VALIDE);
        
        if (nbProlongementsValides >= adherent.getProfil().getQuotaProlongement()) {
            throw new RuntimeException("Quota de prolongement atteint (" + 
                adherent.getProfil().getQuotaProlongement() + " maximum)");
        }

        // 4. Vérifier le quota de prolongement
        int nbProlongements = pret.getProlongements().stream()
                .filter(p -> p.getEtatTraitement() == Prolongement.EtatTraitement.VALIDE)
                .toList()
                .size();
        
        if (nbProlongements >= adherent.getProfil().getQuotaProlongement()) {
            throw new RuntimeException("Prolonger un pret un seule fois");
        }

        // 5. Calculer les nouvelles dates
        LocalDate dateDebutProlongement = LocalDate.now();
        // Avant
        // LocalDate nouvelleDateRetour = dateDebutProlongement.plusDays(
                // adherent.getProfil().getNbrJourPretPenalite());
        // Apres
        LocalDate retourInitial = dateDebutProlongement.plusDays(adherent.getProfil().getNbrJourPretPenalite());
        LocalDate retourAjuste = jourFerieService.ajusterSiNonOuvrable(retourInitial);


        // 7. Créer et enregistrer le prolongement
        Prolongement prolongement = new Prolongement();
        prolongement.setPret(pret);
        prolongement.setDateDebut(dateDebutProlongement);
        // AVANT
        // prolongement.setDateRetourPrevue(nouvelleDateRetour);
        // APRES
        prolongement.setDateRetourPrevue(retourAjuste);

        prolongement.setEtatTraitement(Prolongement.EtatTraitement.EN_ATTENTE);
        
        prolongementRepository.save(prolongement);
        pret.addProlongement(prolongement);

        // 8. Mettre à jour la date de retour du prêt
        // pret.setDateRetourPrevue(nouvelleDateRetour);
        
        return pretRepository.save(pret);
    }


    public void traiterRetourPret(Long pretId, LocalDate dateRetourEffective) {
        Pret pret = pretRepository.findById(pretId)
                .orElseThrow(() -> new RuntimeException("Prêt non trouvé"));
        
        // Vérifier que le prêt est bien en cours et validé
        if (pret.getStatutPret() != Pret.StatutPret.EN_COURS || 
            pret.getEtatTraitement() != Pret.EtatTraitement.VALIDE) {
            throw new IllegalStateException("Ce prêt n'est pas éligible au retour");
        }
        
        // Mettre à jour le prêt
        pret.setDateRetourEffective(dateRetourEffective);
        
        // Déterminer le statut final
        if (dateRetourEffective.isAfter(pret.getDateRetourPrevue())) {
            pret.setStatutPret(Pret.StatutPret.RETARD);
            creerPenalisation(pret, dateRetourEffective);
        } else {
            pret.setStatutPret(Pret.StatutPret.RETOURNE);
        }

        // Rendre l'exemplaire disponible
        Exemplaire exemplaire = pret.getExemplaire();
        exemplaire.setDisponible(true);
        
        pretRepository.save(pret);
    }

    private void creerPenalisation(Pret pret, LocalDate dateRetourEffective) {
        Profil profil = pret.getAdherent().getProfil();
        LocalDate dateFinPenalisation = dateRetourEffective.plusDays(profil.getNbrJourPenalite());
        // LocalDate dateFinPenalisation = LocalDate.now().plusDays(profil.getNbrJourPenalite());
        
        Penalisation penalisation = new Penalisation(
            pret.getAdherent(),
            dateRetourEffective,
            // LocalDate.now(),
            dateFinPenalisation,
            Penalisation.Etat.EN_COURS
        );
        
        penalisationRepository.save(penalisation);
    }

    public Optional<Pret> findById(Long id) {
        return pretRepository.findById(id);
    }
}



    
