package com.example.biblio.repository;

import com.example.biblio.model.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdherentRepository extends JpaRepository<Adherent, Long> {

    Optional<Adherent> findByEmail(String email);
    Adherent findAdByEmail(String email);

    Optional<Adherent> findByNom(String nom);

}
