package com.example.biblio.service;

import com.example.biblio.model.JourFerie;
import com.example.biblio.repository.JourFerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class JourFerieService {

    @Autowired
    private JourFerieRepository jourFerieRepository;

    /**
     * Vérifie si une date est un jour non ouvrable (samedi, dimanche ou jour férié).
     */
    public boolean estNonOuvrable(LocalDate date) {
        return estWeekend(date) || estJourFerie(date);
    }

    /**
     * Vérifie si une date est un samedi ou dimanche.
     */
    public boolean estWeekend(LocalDate date) {
        DayOfWeek jour = date.getDayOfWeek();
        return jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY;
    }

    /**
     * Vérifie si une date est un jour férié enregistré en base.
     */

    public boolean estJourFerie(LocalDate date) {
        boolean existe = jourFerieRepository.existeDate(java.sql.Date.valueOf(date));
        System.out.println("estJourFerie(" + date + ") => " + existe);
        return existe;
    }

    /**
     * Indique si une date est ouvrable (ni weekend ni jour férié).
     */
    public boolean isOuvrable(LocalDate date) {
        return !estNonOuvrable(date);
    }

    /**
     * Retourne le prochain jour ouvrable si la date fournie ne l’est pas.
     */
    public LocalDate ajusterSiNonOuvrable(LocalDate date) {
        LocalDate d = date;
        while (!isOuvrable(d)) {
            d = d.plusDays(1);
        }
        return d;
    }

    /**
     * Enregistre un nouveau jour férié si la date n’existe pas déjà.
     */
    public JourFerie ajouterJourFerie(LocalDate date, String description) {
        if (!estJourFerie(date)) {
            JourFerie jf = new JourFerie();
            jf.setDate(date);
            jf.setDescription(description);
            return jourFerieRepository.save(jf);
        }
        return null;
    }

    /**
     * Liste tous les jours fériés enregistrés.
     */
    public List<JourFerie> listerTous() {
        return jourFerieRepository.findAll();
    }

    /**
     * Supprime un jour férié par son ID.
     */
    public void supprimerJourFerie(Long id) {
        jourFerieRepository.deleteById(id);
    }

    /**
     * Liste les jours fériés dans une plage de dates.
     */
    public List<JourFerie> getFeriesDansPlage(LocalDate debut, LocalDate fin) {
        return jourFerieRepository.findByDateBetween(debut, fin);
    }
}
