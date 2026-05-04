package com.example.gestionfichaje.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestionfichaje.entity.EstadoJustificante;
import com.example.gestionfichaje.entity.Justificante;

@Repository
public interface JustificanteRepository extends JpaRepository<Justificante, Integer> {

    // Para el admin: todos los pendientes
    List<Justificante> findByEstado(EstadoJustificante estado);

    // Para el empleado: sus justificantes
    List<Justificante> findByUsuarioEmail(String email);

    // Por empleado y estado (útil para filtros)
    List<Justificante> findByUsuarioEmailAndEstado(String email, EstadoJustificante estado);

    // Por fichaje asociado
    List<Justificante> findByFichajeId(Integer fichajeId);

    // Por solicitud asociada
    List<Justificante> findBySolicitudId(Integer solicitudId);

    List<Justificante> findByUsuarioId(Integer usuarioId);

    // Contar pendientes (para badge/notificación en el panel admin)
    long countByEstado(EstadoJustificante estado);
}
