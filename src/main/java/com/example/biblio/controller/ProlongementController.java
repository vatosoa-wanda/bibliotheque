package com.example.biblio.controller;

import com.example.biblio.model.Prolongement;
import com.example.biblio.model.Pret;
import com.example.biblio.repository.ProlongementRepository;
import com.example.biblio.repository.PretRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProlongementController {

    private final ProlongementRepository prolongementRepository;

    public ProlongementController(ProlongementRepository prolongementRepository) {
        this.prolongementRepository = prolongementRepository;
    }

    @GetMapping("/prolongement_en_cours")
    public String listProlongementsEnAttente(Model model) {
        model.addAttribute("prolongements", 
            prolongementRepository.findByEtatTraitement(Prolongement.EtatTraitement.EN_ATTENTE));
        return "admin/prolongements";
    }

    @PostMapping("/valider-prolongement/{id}")
    public String validerProlongement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Prolongement prolongement = prolongementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prolongement introuvable"));
            
            // Mettre à jour le prêt associé
            Pret pret = prolongement.getPret();
            // pret.setDateRetourPrevue(prolongement.getDateRetourPrevue());
            
            // Mettre à jour le statut du prolongement
            prolongement.setEtatTraitement(Prolongement.EtatTraitement.VALIDE);
            
            // pretRepository.save(pret);
            prolongementRepository.save(prolongement);
            
            redirectAttributes.addFlashAttribute("success", "Prolongement validé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la validation: " + e.getMessage());
        }
        return "redirect:/prolongement_en_cours";
    }

    @PostMapping("/rejeter-prolongement/{id}")
    public String rejeterProlongement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Prolongement prolongement = prolongementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prolongement introuvable"));
            
            prolongement.setEtatTraitement(Prolongement.EtatTraitement.REJETE);
            prolongementRepository.save(prolongement);
            
            redirectAttributes.addFlashAttribute("success", "Prolongement rejeté");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du rejet: " + e.getMessage());
        }
        return "redirect:/prolongement_en_cours";
    }
}