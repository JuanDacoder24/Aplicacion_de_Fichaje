package com.example.gestionfichaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Solicitudes;

@Repository
public interface SolicitudesRepository extends JpaRepository<Solicitudes, Integer> {

}
