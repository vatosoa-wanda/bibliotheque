package com.example.biblio.repository;

import com.example.biblio.model.Livre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    // Recherche par nom_livre (correspond à votre champ 'titre')
    Optional<Livre> findByTitre(String titre);
    
    // Recherche insensible à la casse
    List<Livre> findByTitreIgnoreCase(String titre);
    
    // Recherche partielle
    List<Livre> findByTitreContaining(String partieTitre);
    
    // Recherche par auteur
    Optional<Livre> findByAuteur(String auteur);
    
    // Recherche combinée
    List<Livre> findByTitreContainingAndAuteurContaining(String partieTitre, String partieAuteur);

    Optional<Livre> findByTitreAndAuteur(String titre, String auteur);
}