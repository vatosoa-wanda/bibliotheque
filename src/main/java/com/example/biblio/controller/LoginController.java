package com.example.biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // retournera la vue admin/dashboard.html
    }

    @GetMapping("/adherent/accueil")
    public String adherentAccueil() {
        return "adherent/accueil"; // retournera la vue adherent/accueil.html
    }
}