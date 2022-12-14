package com.example.atlantis.repository;


import com.example.atlantis.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findAll();

    @Query(value = "SELECT * FROM Reserva ORDER BY ID DESC LIMIT 1 ", nativeQuery = true)
    Reserva obtenerUltima();
}
