package com.example.gestionfichaje.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;

@Repository
public interface SolicitudesRepository extends JpaRepository<Solicitudes, Integer> {

    List<Solicitudes> findByUsuario(Usuarios usuario);

}
