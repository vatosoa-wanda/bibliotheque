package com.example.biblio.controller;

import com.example.biblio.model.Livre;
import com.example.biblio.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/listeLivres")
    public String afficherListeLivres(Model model) {
        List<Livre> livres = livreService.findAll();
        model.addAttribute("livres", livres);
        return "adherent/livres";  // fichier : templates/livre/liste.html
    }
}