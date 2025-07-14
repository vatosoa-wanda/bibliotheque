package com.example.biblio.repository;

import com.example.biblio.model.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {

    boolean existsByDate(LocalDate date);

    List<JourFerie> findByDateBetween(LocalDate start, LocalDate end);

    List<JourFerie> findAllByDate(LocalDate date);
}
