package com.example.biblio.repository;

import com.example.biblio.model.Exemplaire;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    int countByLivreIdAndDisponibleTrue(Long livreId);

    List<Exemplaire> findByLivreIdAndDisponibleTrue(Long id); // findDisponiblesByLivreId

    List<Exemplaire> findByLivreId(Long livreId);

    Optional<Exemplaire> findFirstByLivreIdAndDisponibleTrue(Long livreId);

}