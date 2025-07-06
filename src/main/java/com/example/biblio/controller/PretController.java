package com.example.biblio.controller;

import com.example.biblio.model.*;
import com.example.biblio.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import java.util.List;


@Controller
public class PretController {
    private final PretService pretService;
    private final LivreService livreService;
    private final AdherentService adherentService;
    private final ExemplaireService exemplaireService;

    public PretController(PretService pretService, LivreService livreService, 
                        AdherentService adherentService, ExemplaireService exemplaireService) {
        this.pretService = pretService;
        this.livreService = livreService;
        this.adherentService = adherentService;
        this.exemplaireService = exemplaireService;
    }

    // @GetMapping("/pret")
    // public String showPretPage() {
    //     return "/adherent/pret";  // Va chercher pret.html dans templates/
    // }

    @GetMapping("/pret")
    public String showPretForm(Model model) {
        model.addAttribute("livre", new Livre());
        return "/adherent/pret";
    }

    @PostMapping("/pret")
    // @PostMapping("/pret/preter")
    public String demanderPret(
            @RequestParam String titre,
            @RequestParam String auteur,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Récupération de l'adhérent depuis la session
            Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
            if (adherent == null) {
                throw new RuntimeException("Vous devez être connecté pour effectuer un prêt");
            }

            Pret pret = pretService.demanderPret(titre, auteur, adherent);
            redirectAttributes.addFlashAttribute("success", "Demande de prêt enregistrée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/pret";
    }

    @GetMapping("/mes-prets")
    public String afficherPretsAdherent(HttpSession session, Model model) {
        // Récupération de l'adhérent depuis la session
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");

        if (adherent == null) {
            // Redirection vers la page de connexion si non connecté
            return "redirect:/login";
        }

        // Récupération des prêts de l’adhérent via le service
        List<Pret> prets = pretService.getPretsByAdherent(adherent.getId());

        // Passage des prêts au modèle pour affichage
        model.addAttribute("prets", prets);

        return "adherent/mes-prets";  // correspond à mes-prets.html dans templates
    }
}
