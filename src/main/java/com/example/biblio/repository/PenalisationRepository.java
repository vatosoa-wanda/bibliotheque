package com.example.biblio.repository;

import com.example.biblio.model.Penalisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenalisationRepository extends JpaRepository<Penalisation, Long> {

    // Trouver les pénalisations d'un adhérent
    List<Penalisation> findByAdherentId(Long idAdherent);

    // Optionnel : trouver celles en cours
    List<Penalisation> findByEtat(String etat);

    boolean existsByAdherentIdAndEtat(Long adherentId, Penalisation.Etat etat);
}
