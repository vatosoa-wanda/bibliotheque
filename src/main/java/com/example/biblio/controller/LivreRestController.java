package com.example.biblio.controller;

import com.example.biblio.model.Exemplaire;
import com.example.biblio.model.Livre;
import com.example.biblio.model.ExemplaireDTO;
import com.example.biblio.model.LivreDTO;
import com.example.biblio.repository.LivreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/livres")
public class LivreRestController {

    private final LivreRepository livreRepository;

    public LivreRestController(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreDTO> getLivreById(@PathVariable Long id) {
        return livreRepository.findById(id)
                .map(livre -> {
                    // Mapper les exemplaires vers ExemplaireDTO
                    List<ExemplaireDTO> exemplaireDTOs = livre.getExemplaires()
                            .stream()
                            .map(e -> new ExemplaireDTO(e.getId(), e.isDisponible()))
                            .collect(Collectors.toList());

                    // Créer le LivreDTO
                    LivreDTO livreDTO = new LivreDTO(
                            livre.getId(),
                            livre.getTitre(),
                            livre.getAuteur(),
                            livre.isRestreint(),
                            exemplaireDTOs
                    );

                    return ResponseEntity.ok(livreDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
