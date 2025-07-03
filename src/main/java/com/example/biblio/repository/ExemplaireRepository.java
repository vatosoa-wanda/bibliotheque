package com.example.biblio.repository;

import com.example.biblio.model.Exemplaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    int countByLivreIdAndDisponibleTrue(Long livreId);
}