package com.example.biblio.controller;

import com.example.biblio.model.AdherentDetailsDTO;
import com.example.biblio.model.*;
import com.example.biblio.repository.AdherentRepository;
import com.example.biblio.repository.AbonnementRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adherents")
public class AdherentRestController {

    private final AdherentRepository adherentRepository;
    private final AbonnementRepository abonnementRepository;

    public AdherentRestController(AdherentRepository adherentRepository, AbonnementRepository abonnementRepository) {
        this.adherentRepository = adherentRepository;
        this.abonnementRepository = abonnementRepository;
    }

    @GetMapping("/{id}")
    public AdherentDetailsDTO getAdherentDetails(@PathVariable Long id) {
        Adherent adherent = adherentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adhérent introuvable"));

        AdherentDetailsDTO dto = new AdherentDetailsDTO();
        dto.id = adherent.getId();
        dto.nom = adherent.getNom();
        dto.prenom = adherent.getPrenom();
        dto.email = adherent.getEmail();
        dto.telephone = adherent.getTelephone();
        dto.dateNaissance = adherent.getDateNaissance();
        dto.profil = adherent.getProfil().getProfil();

        // Abonnement actif
        Optional<Abonnement> abonnementActif = adherent.getAbonnements().stream()
                .filter(Abonnement::isActif)
                .findFirst();
        abonnementActif.ifPresent(ab -> {
            dto.abonnementActif = true;
            dto.dateDebutAbonnement = ab.getDateDebut();
            dto.dateFinAbonnement = ab.getDateFin();
        });

        // Calcul quota restant (simplement basé sur le profil)
        dto.quotaPretRestant = adherent.getProfil().getQuota() - adherent.getPrets().size();
        dto.quotaReservationRestant = adherent.getProfil().getQuotaReservation() - adherent.getReservations().size();
        dto.quotaProlongementRestant = adherent.getProfil().getQuotaProlongement(); // à ajuster si suivi ailleurs

        // Penalisations
        dto.penalites = adherent.getPenalisations().stream()
                .map(p -> {
                    AdherentDetailsDTO.PenalisationDTO penalDto = new AdherentDetailsDTO.PenalisationDTO();
                    penalDto.dateDebut = p.getDateDebut();
                    penalDto.dateFin = p.getDateFin();
                    penalDto.etat = p.getEtat().name();
                    return penalDto;
                })
                .collect(Collectors.toList());

        return dto;
    }
}
