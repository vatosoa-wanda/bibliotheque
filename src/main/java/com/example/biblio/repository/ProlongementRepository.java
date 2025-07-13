package com.example.biblio.repository;

import com.example.biblio.model.Prolongement;
import com.example.biblio.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ProlongementRepository extends JpaRepository<Prolongement, Long> {
    
    // Trouver les prolongements par prêt
    List<Prolongement> findByPretId(Long pretId);
    
    // Trouver les prolongements par état de traitement
    List<Prolongement> findByEtatTraitement(Prolongement.EtatTraitement etat);
    
    // Vérifier s'il existe un prolongement en attente pour un prêt
    boolean existsByPretIdAndEtatTraitement(Long pretId, Prolongement.EtatTraitement etat);

    long countByPretAdherentAndEtatTraitement(Adherent adherent, Prolongement.EtatTraitement etatTraitement);

}