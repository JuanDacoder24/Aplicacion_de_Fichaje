package com.example.gestionfichaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Fichajes;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface FichajesRepository extends JpaRepository<Fichajes, Integer> {

    Page<Fichajes> findByUsuarioId(Integer usuarioId, Pageable pageable);

    @Query("SELECT f FROM Fichajes f WHERE f.usuarioId = :usuarioId " +
            "AND f.fecha = :fecha AND f.horaSalida IS NULL")
    Fichajes findByUsuarioIdAndFechaAndHoraSalidaIsNull(
            @Param("usuarioId") Integer usuarioId,
            @Param("fecha") LocalDate fecha);

}