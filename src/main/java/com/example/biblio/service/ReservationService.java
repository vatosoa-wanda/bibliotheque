package com.example.biblio.service;

import com.example.biblio.model.Reservation;
import com.example.biblio.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservationsByAdherent(Long adherentId) {
        return reservationRepository.findByAdherentId(adherentId);
    }

    // Créer une nouvelle réservation
    // @Transactional
    // public Reservation createReservation(Reservation reservation) {
    //     return reservationRepository.save(reservation);
    // }

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

        // 4. Vérifier le quota de réservation (et non de prêt ici)
        long nbReservationsEnCours = reservationRepository.countByAdherentIdAndEtat(adherent.getId(), Reservation.EtatReservation.EN_COURS);
        if (nbReservationsEnCours >= adherent.getProfil().getQuotaReservation()) {
            throw new RuntimeException("Quota de réservation atteint (" + adherent.getProfil().getQuotaReservation() + " maximum).");
        }

        // 5. Vérifier si le livre est restreint aux mineurs
        if (livre.isRestreint() && adherent.getAge() < 18) {
            throw new RuntimeException("Ce livre est restreint aux mineurs.");
        }

        // 6. Insérer la réservation
        Reservation reservation = new Reservation(adherent, exemplaire, dateAReserver);
        reservationRepository.save(reservation);
    }

}