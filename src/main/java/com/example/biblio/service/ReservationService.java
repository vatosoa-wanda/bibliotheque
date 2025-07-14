package com.example.biblio.service;

import com.example.biblio.model.Reservation;
import com.example.biblio.model.Adherent;
import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.model.Penalisation;
import com.example.biblio.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.time.LocalDate;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    @Autowired
    private PenalisationRepository penalisationRepository;
    private final AbonnementRepository abonnementRepository;


    public ReservationService(ReservationRepository reservationRepository, AbonnementRepository abonnementRepository ) {
        this.reservationRepository = reservationRepository;
        this.abonnementRepository = abonnementRepository;
    }

    // Créer une nouvelle réservation
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    // Annuler une réservation
    @Transactional
    public Reservation annulerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setEtat(Reservation.EtatReservation.ANNULE);
        return reservationRepository.save(reservation);
    }

    // Valider une réservation
    @Transactional
    public Reservation validerReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        reservation.setEtat(Reservation.EtatReservation.VALIDE);
        return reservationRepository.save(reservation);
    }

    // Récupérer toutes les réservations d'un adhérent
    public List<Reservation> getReservationsByAdherent(Long adherentId) {
        return reservationRepository.findByAdherentId(adherentId);
    }

    // Vérifier la disponibilité d'un exemplaire
    public boolean isExemplaireDisponiblePourReservation(Long exemplaireId) {
        return !reservationRepository.existsByExemplaireIdAndEtat(
            exemplaireId, 
            Reservation.EtatReservation.EN_COURS
        );
    }

    // Récupérer toutes les réservations en cours
    public List<Reservation> getReservationsEnCours() {
        return reservationRepository.findByEtat(Reservation.EtatReservation.EN_COURS);
    }


    public void createReservation(Adherent adherent, Exemplaire exemplaire, LocalDateTime dateAReserver) {
        // 4. Vérifier que l'adhérent a un abonnement valide
        if (!abonnementRepository.existsByAdherentAndDateDebutBeforeAndDateFinAfter(
                adherent,  
                LocalDate.now())) {
            throw new RuntimeException("Vous devez avoir un abonnement valide pour effectuer une réservation.");
        }

        // 1. Vérifier que l'exemplaire existe
        if (exemplaire == null) {
            throw new RuntimeException("Exemplaire introuvable.");
        }

        Livre livre = exemplaire.getLivre();

        // 2. Vérifier que le livre associé existe
        if (livre == null) {
            throw new RuntimeException("Livre associé introuvable.");
        }

        // 3. Vérifier que l'adhérent n'a pas de sanction en cours
        if (penalisationRepository.existsByAdherentIdAndEtat(adherent.getId(), Penalisation.Etat.EN_COURS)) {
            throw new RuntimeException("Vous avez une sanction en cours.");
        }

        // // 4. Vérifier le quota de réservation (et non de prêt ici)
        // long nbReservationsEnCours = reservationRepository.countByAdherentIdAndEtat(adherent.getId(), Reservation.EtatReservation.EN_COURS);
        // if (nbReservationsEnCours >= adherent.getProfil().getQuotaReservation()) {
        //     throw new RuntimeException("Quota de réservation atteint (" + adherent.getProfil().getQuotaReservation() + " maximum).");
        // }

        // 4. Vérifier le quota de réservation VALIDE uniquement
        long nbReservationsValides = reservationRepository.countByAdherentIdAndEtat(
            adherent.getId(), 
            Reservation.EtatReservation.VALIDE
        );
        if (nbReservationsValides >= adherent.getProfil().getQuotaReservation()) {
            throw new RuntimeException(
                "Quota de réservation atteint (" 
                + adherent.getProfil().getQuotaReservation() 
                + " réservations valides maximum)."
            );
        }

        // 4. Vérifier le quota de réservations actives (EN_COURS + VALIDE)
        // long nbReservationsActives = reservationRepository.countByAdherentIdAndEtatIn(
        //     adherent.getId(),
        //     List.of(Reservation.EtatReservation.EN_COURS, Reservation.EtatReservation.VALIDE)
        // );

        // if (nbReservationsActives >= adherent.getProfil().getQuotaReservation()) {
        //     throw new RuntimeException(
        //         "Quota de réservation atteint (" 
        //         + adherent.getProfil().getQuotaReservation() 
        //         + " réservations actives maximum)."
        //     );
        // }

        // 5. Vérifier si le livre est restreint aux mineurs
        if (livre.isRestreint() && adherent.getAge() < 18) {
            throw new RuntimeException("Ce livre est restreint aux mineurs.");
        }

        // 6. Insérer la réservation
        Reservation reservation = new Reservation(adherent, exemplaire, dateAReserver);
        reservationRepository.save(reservation);
    }


    public boolean estExemplaireReserveALaDate(Exemplaire exemplaire, LocalDateTime date) {
        return reservationRepository.existsByExemplaireAndEtatValideAndDate(exemplaire, date);
    }

}