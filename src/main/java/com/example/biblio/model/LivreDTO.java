package com.example.biblio.model;

import java.util.List;

public class LivreDTO {
    private Long id;
    private String titre;
    private String auteur;
    private boolean restreint;
    private List<ExemplaireDTO> exemplaires;

    public LivreDTO() {}

    public LivreDTO(Long id, String titre, String auteur, boolean restreint, List<ExemplaireDTO> exemplaires) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.restreint = restreint;
        this.exemplaires = exemplaires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public boolean isRestreint() {
        return restreint;
    }

    public void setRestreint(boolean restreint) {
        this.restreint = restreint;
    }

    public List<ExemplaireDTO> getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(List<ExemplaireDTO> exemplaires) {
        this.exemplaires = exemplaires;
    }
}
