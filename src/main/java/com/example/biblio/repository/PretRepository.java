package com.example.biblio.repository;

import com.example.biblio.model.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {
    Optional<Pret> findById(Long id);
    // Trouver les prêts par adhérent
    List<Pret> findByAdherentId(Long adherentId);
    
    // Trouver les prêts par exemplaire
    List<Pret> findByExemplaireId(Long exemplaireId);
    
    // Trouver les prêts par statut
    List<Pret> findByStatutPret(Pret.StatutPret statut);
    
    // Trouver les prêts en cours avec date de retour dépassée
    List<Pret> findByStatutPretAndDateRetourPrevueBefore(
        Pret.StatutPret statut, 
        LocalDate date
    );
    
    // Vérifier si un exemplaire est en cours de prêt
    boolean existsByExemplaireIdAndStatutPret(
        Long exemplaireId, 
        Pret.StatutPret statut
    );
    
    // Compter les prêts en cours d'un adhérent
    @Query("SELECT COUNT(p) FROM Pret p WHERE p.adherent.id = ?1 AND p.statutPret = 'EN_COURS'")
    int countPretsEnCoursByAdherent(Long adherentId);

    long countByAdherentIdAndStatutPret(Long adherentId, Pret.StatutPret statut);

    @Query("""
        SELECT COUNT(p) FROM Pret p
        WHERE p.exemplaire.id = :idExemplaire
        AND p.statutPret IN (:statuts)
        AND (:dateAReserver BETWEEN p.dateDebut AND p.dateRetourPrevue)
    """)
    long countActivePretsForExemplaireAtDate(
        @Param("idExemplaire") Long idExemplaire,
        @Param("dateAReserver") LocalDateTime dateAReserver,
        @Param("statuts") List<Pret.StatutPret> statuts
    );

}