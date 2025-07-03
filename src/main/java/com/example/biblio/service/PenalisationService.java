package com.example.biblio.service;

import com.example.biblio.model.Penalisation;
import com.example.biblio.repository.PenalisationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PenalisationService {

    private final PenalisationRepository penalisationRepository;

    public PenalisationService(PenalisationRepository penalisationRepository) {
        this.penalisationRepository = penalisationRepository;
    }

    public List<Penalisation> getAllPenalisations() {
        return penalisationRepository.findAll();
    }

    public Optional<Penalisation> getPenalisationById(Long id) {
        return penalisationRepository.findById(id);
    }

    public List<Penalisation> getPenalisationsByAdherent(Long idAdherent) {
        return penalisationRepository.findByAdherentId(idAdherent);
    }

    public Penalisation createPenalisation(Penalisation penalisation) {
        return penalisationRepository.save(penalisation);
    }

    public void deletePenalisation(Long id) {
        penalisationRepository.deleteById(id);
    }

    public Penalisation updatePenalisation(Penalisation penalisation) {
        return penalisationRepository.save(penalisation);
    }
}
