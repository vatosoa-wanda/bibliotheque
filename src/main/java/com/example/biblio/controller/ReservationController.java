package com.example.biblio.controller;

import com.example.biblio.model.*;
import com.example.biblio.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class ReservationController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private ReservationService reservationService;

    // Formulaire réservation
    @GetMapping("/reserver/{idLivre}")
    public String showReservationForm(@PathVariable Long idLivre, Model model, HttpSession session) {
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
        if (adherent == null) return "redirect:/login";

        Livre livre = livreService.findById(idLivre);
        if (livre == null || livre.getExemplaires().isEmpty()) {
            model.addAttribute("error", "Aucun exemplaire disponible.");
            return "redirect:/listeLivres";
        }

        // Choix d’un exemplaire disponible (par défaut le premier)
        Exemplaire exemplaire = livre.getExemplaires().iterator().next();

        model.addAttribute("livre", livre);
        model.addAttribute("exemplaire", exemplaire);
        return "adherent/reservation";
    }

    // Traitement réservation
    @PostMapping("/reserver")
    public String effectuerReservation(@RequestParam Long exemplaireId,
                                       @RequestParam String dateAReserver,
                                       HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
        if (adherent == null) return "redirect:/login";

        Exemplaire exemplaire = exemplaireService.getExemplaireById(exemplaireId);
        if (exemplaire == null) return "redirect:/listeLivres";

        LocalDateTime dateRes = LocalDateTime.parse(dateAReserver);
        reservationService.creerReservation(adherent, exemplaire, dateRes);

        return "redirect:/mes-reservations"; // page à créer
    }

    @GetMapping("/mes-reservations")
    public String afficherMesReservations(HttpSession session, Model model) {
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");

        if (adherent == null) {
            return "redirect:/login";
        }

        List<Reservation> reservations = reservationService.getReservationsByAdherent(adherent.getId());
        model.addAttribute("reservations", reservations);
        return "adherent/mes-reservations"; // correspond à templates/adherent/mes-reservations.html
    }

}
