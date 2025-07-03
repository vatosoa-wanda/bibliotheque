package com.example.biblio.service;

import com.example.biblio.model.Livre;
import com.example.biblio.repository.LivreRepository;
import com.example.biblio.repository.ExemplaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LivreService {
    private final LivreRepository livreRepository;
    private final ExemplaireRepository exemplaireRepository;

    public LivreService(LivreRepository livreRepository, ExemplaireRepository exemplaireRepository) {
        this.livreRepository = livreRepository;
        this.exemplaireRepository = exemplaireRepository;
    }

    public List<Livre> findAll() {
        return livreRepository.findAll();
    }

    public Livre findById(Long id) {
        return livreRepository.findById(id).orElse(null);
    }

    public Livre save(Livre livre) {
        return livreRepository.save(livre);
    }

    public void delete(Long id) {
        livreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean estDisponible(Long livreId) {
        if (livreId == null) {
            throw new IllegalArgumentException("L'ID du livre ne peut pas être null");
        }
        
        return exemplaireRepository.countByLivreIdAndDisponibleTrue(livreId) > 0;
    }
}