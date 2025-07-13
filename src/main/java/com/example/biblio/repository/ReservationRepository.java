package com.example.biblio.repository;

import com.example.biblio.model.Reservation;
import com.example.biblio.model.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Trouver les réservations par adhérent
    List<Reservation> findByAdherentId(Long adherentId);
    
    // Trouver les réservations par exemplaire
    List<Reservation> findByExemplaireId(Long exemplaireId);
    
    // Trouver les réservations par état
    List<Reservation> findByEtat(Reservation.EtatReservation etat);

    // // Trouver toutes les réservations en cours
    // List<Reservation> findByEtat(EtatReservation etat);

    // Valider une réservation
    @Transactional
    @Modifying
    @Query("UPDATE Reservation r SET r.etat = 'VALIDE' WHERE r.id = :id")
    int validerReservation(Long id);
    
    // Vérifier si un exemplaire a déjà une réservation en cours
    boolean existsByExemplaireIdAndEtat(Long exemplaireId, Reservation.EtatReservation etat);

    long countByAdherentIdAndEtat(Long adherentId, Reservation.EtatReservation etat);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
           "WHERE r.exemplaire = :exemplaire " +
           "AND r.etat = 'VALIDE' " +
           "AND r.dateAReserver = :dateRecherchee")
    boolean existsByExemplaireAndEtatValideAndDate(
            @Param("exemplaire") Exemplaire exemplaire,
            @Param("dateRecherchee") LocalDateTime dateRecherchee);

}