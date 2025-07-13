package com.example.biblio.controller;

import com.example.biblio.model.*;
import com.example.biblio.repository.ReservationRepository;
import com.example.biblio.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class ReservationController {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Autowired
    private LivreService livreService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private ReservationService reservationService;
    

    // Formulaire réservation
    // @GetMapping("/reserver/{idLivre}")
    // public String showReservationForm(@PathVariable Long idLivre, Model model, HttpSession session) {
    //     Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
    //     if (adherent == null) return "redirect:/login";

    //     Livre livre = livreService.findById(idLivre);
    //     if (livre == null || livre.getExemplaires().isEmpty()) {
    //         model.addAttribute("error", "Aucun exemplaire disponible.");
    //         return "redirect:/listeLivres";
    //     }

    //     // Choix d’un exemplaire disponible (par défaut le premier)
    //     Exemplaire exemplaire = livre.getExemplaires().iterator().next();

    //     model.addAttribute("livre", livre);
    //     model.addAttribute("exemplaire", exemplaire);
    //     return "adherent/reservation";
    // }

    @GetMapping("/reserver/{idLivre}")
    public String showReservationForm(@PathVariable Long idLivre, 
                                    Model model, 
                                    HttpSession session) {
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
        if (adherent == null) return "redirect:/login";

        try {
            Livre livre = livreService.findById(idLivre);
            if (livre == null || livre.getExemplaires().isEmpty()) {
                model.addAttribute("error", "Aucun exemplaire disponible.");
                return "redirect:/listeLivres";
            }

            Exemplaire exemplaire = livre.getExemplaires().iterator().next();
            
            // Ajout des attributs nécessaires
            model.addAttribute("livre", livre);
            model.addAttribute("exemplaire", exemplaire);
            model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
            model.addAttribute("minDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));
            
            return "adherent/reservation";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
            return "redirect:/listeLivres";
        }
    }

    // Traitement réservation
    // @PostMapping("/reserver")
    // public String effectuerReservation(@RequestParam Long exemplaireId,
    //                                    @RequestParam String dateAReserver,
    //                                    HttpSession session, Model model) {
    //     Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
    //     if (adherent == null) return "redirect:/login";

    //     Exemplaire exemplaire = exemplaireService.getExemplaireById(exemplaireId);
    //     if (exemplaire == null) return "redirect:/listeLivres";

    //     LocalDateTime dateRes = LocalDateTime.parse(dateAReserver);
    //     reservationService.createReservation(adherent, exemplaire, dateRes);

    //     return "redirect:/mes-reservations"; // page à créer
    // }

    @PostMapping("/reserver")
    public String effectuerReservation(@RequestParam Long exemplaireId,
                                    @RequestParam String dateAReserver,
                                    HttpSession session, 
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        
        Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
        if (adherent == null) return "redirect:/login";

        try {
            // Validation des données
            Exemplaire exemplaire = exemplaireService.getExemplaireById(exemplaireId);
            if (exemplaire == null) {
                redirectAttributes.addFlashAttribute("error", "Exemplaire introuvable");
                return "redirect:/listeLivres";
            }

            // Conversion de la date
            LocalDateTime dateRes = LocalDateTime.parse(dateAReserver);
            
            // Vérification date future
            if (dateRes.isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", "La date doit être dans le futur");
                return "redirect:/reserver/" + exemplaire.getLivre().getId();
            }

            // Création de la réservation
            reservationService.createReservation(adherent, exemplaire, dateRes);
            
            // Succès
            redirectAttributes.addFlashAttribute("success", "Réservation effectuée avec succès");
            return "redirect:/mes-reservations";

        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Format de date invalide");
            return "redirect:/reserver/" + exemplaireService.getExemplaireById(exemplaireId).getLivre().getId();
            
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reserver/" + exemplaireService.getExemplaireById(exemplaireId).getLivre().getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la réservation: " + e.getMessage());
            // return "redirect:/listeLivres";
            return "redirect:/reserver/" + exemplaireService.getExemplaireById(exemplaireId).getLivre().getId();
        }
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

    @GetMapping("/reservation_en_cours")
    public String listReservationsEnCours(Model model) {
        model.addAttribute("reservations", 
            reservationRepository.findByEtat(Reservation.EtatReservation.EN_COURS));
        return "admin/reservations";
    }

    @PostMapping("/valider_reservation/{id}")
    public String validerReservation(@PathVariable Long id) {
        reservationRepository.validerReservation(id);
        return "redirect:/reservation_en_cours";
    }

}
