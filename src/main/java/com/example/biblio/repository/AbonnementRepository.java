package com.example.biblio.repository;

import com.example.biblio.model.Abonnement;
import com.example.biblio.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    
    // Trouver les abonnements par adhérent
    List<Abonnement> findByAdherentId(Long adherentId);
    
    // Trouver les abonnements actifs
    @Query("SELECT a FROM Abonnement a WHERE a.dateDebut <= :date AND a.dateFin >= :date")
    List<Abonnement> findAbonnementsActifs(LocalDate date);
    
    // Trouver les abonnements expirant bientôt
    @Query("SELECT a FROM Abonnement a WHERE a.dateFin BETWEEN :today AND :futureDate")
    List<Abonnement> findAbonnementsExpirantEntre(LocalDate today, LocalDate futureDate);
    
    // Vérifier si un adhérent a un abonnement actif
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
           "FROM Abonnement a WHERE a.adherent.id = :adherentId " +
           "AND a.dateDebut <= CURRENT_DATE AND a.dateFin >= CURRENT_DATE")
    boolean hasAbonnementActif(Long adherentId);

    boolean existsByAdherentIdAndDateFinAfter(Long adherentId, LocalDate date);

    @Query("SELECT COUNT(a) > 0 FROM Abonnement a " +
           "WHERE a.adherent = :adherent " +
           "AND a.dateDebut <= :currentDate " +
           "AND a.dateFin >= :currentDate")
    boolean existsByAdherentAndDateDebutBeforeAndDateFinAfter(
            @Param("adherent") Adherent adherent,
            @Param("currentDate") LocalDate currentDate);

}