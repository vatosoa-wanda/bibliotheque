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
}