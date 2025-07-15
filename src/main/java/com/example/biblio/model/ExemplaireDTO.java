package com.example.biblio.model;

public class ExemplaireDTO {
    private Long id;
    private boolean disponible;

    public ExemplaireDTO() {}

    public ExemplaireDTO(Long id, boolean disponible) {
        this.id = id;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
