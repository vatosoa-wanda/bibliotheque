package com.example.biblio.service;

import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.repository.ExemplaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.util.List;

@Service
public class ExemplaireService {
    
    private final ExemplaireRepository exemplaireRepository;
    
    public ExemplaireService(ExemplaireRepository exemplaireRepository) {
        this.exemplaireRepository = exemplaireRepository;
    }
    
    // Créer un nouvel exemplaire
    @Transactional
    public Exemplaire creerExemplaire(Livre livre) {
        Exemplaire exemplaire = new Exemplaire(livre);
        return exemplaireRepository.save(exemplaire);
    }
    
    // Marquer un exemplaire comme disponible/indisponible
    @Transactional
    public void changerDisponibilite(Long idExemplaire, boolean disponible) {
        exemplaireRepository.findById(idExemplaire).ifPresent(exemplaire -> {
            exemplaire.setDisponible(disponible);
            exemplaireRepository.save(exemplaire);
        });
    }
    
    // Trouver un exemplaire disponible pour un livre
    @Transactional(readOnly = true)
    public Exemplaire trouverExemplaireDisponible(Long livreId) {
        return exemplaireRepository.findByLivreIdAndDisponibleTrue(livreId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun exemplaire disponible"));
    }
    
    // Vérifier la disponibilité d'un livre
    @Transactional(readOnly = true)
    public boolean estDisponible(Long livreId) {
        return exemplaireRepository.countByLivreIdAndDisponibleTrue(livreId) > 0;
        // return exemplaireRepository.countDisponiblesByLivreId(livreId) > 0;
    }
    
    // Récupérer tous les exemplaires d'un livre
    @Transactional(readOnly = true)
    public List<Exemplaire> getExemplairesByLivre(Long livreId) {
        return exemplaireRepository.findByLivreId(livreId);
    }

    public List<Exemplaire> findDisponiblesByLivreId(Long livreId) {
        return exemplaireRepository.findByLivreIdAndDisponibleTrue(livreId);
    }

    public Exemplaire getExemplaireById(Long id) {
        Optional<Exemplaire> opt = exemplaireRepository.findById(id);
        return opt.orElse(null); // ou lever une exception si tu préfères
    }
}