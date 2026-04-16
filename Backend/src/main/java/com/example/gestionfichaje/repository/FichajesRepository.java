package com.example.gestionfichaje.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Usuarios;

@Repository
public interface FichajesRepository extends JpaRepository<Fichajes, Integer> {

    Page<Fichajes> findByUsuarioId(int usuarioId, Pageable pageable);

    @Query("SELECT f FROM Fichajes f WHERE f.usuario.id = :usuarioId AND f.fecha = :fecha AND f.horaSalida IS NULL")
    Optional<Fichajes> findByUsuarioIdAndFechaAndHoraSalidaIsNull( 
            @Param("usuarioId") Integer usuarioId, 
            @Param("fecha") LocalDate fecha);

    Optional<Fichajes> findFirstByUsuarioAndFechaAndHoraSalidaIsNull(Usuarios usuario, LocalDate fecha);

    List<Fichajes> findByFechaBetween(LocalDate inicio, LocalDate fin);
}
