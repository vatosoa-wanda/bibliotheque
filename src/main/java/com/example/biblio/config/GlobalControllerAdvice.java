package com.example.biblio.config;

import com.example.biblio.model.Adherent;
import com.example.biblio.service.AdherentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final AdherentService adherentService;

    public GlobalControllerAdvice(AdherentService adherentService) {
        this.adherentService = adherentService;
    }

    @ModelAttribute("sessionAdherent")
    public Adherent addAdherentToSession(Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Adherent adherent = (Adherent) session.getAttribute("sessionAdherent");
            if (adherent == null) {
                adherent = adherentService.findByEmail(email);
                session.setAttribute("sessionAdherent", adherent);
            }
            return adherent;
        }
        return null;
    }
}
