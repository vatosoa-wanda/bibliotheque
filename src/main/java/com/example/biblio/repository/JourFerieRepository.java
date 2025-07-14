package com.example.biblio.repository;

import com.example.biblio.model.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {

    boolean existsByDate(LocalDate date);

    List<JourFerie> findByDateBetween(LocalDate start, LocalDate end);

    List<JourFerie> findAllByDate(LocalDate date);

    // @Query("SELECT COUNT(j) > 0 FROM JourFerie j WHERE j.date = :date")
    // boolean existeDate(@Param("date") LocalDate date);

    @Query(value = "SELECT COUNT(*) > 0 FROM jourferie WHERE date = :date", nativeQuery = true)
    boolean existeDate(@Param("date") java.sql.Date date);


}
