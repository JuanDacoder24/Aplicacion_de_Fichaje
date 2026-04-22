package com.example.gestionfichaje.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Usuarios;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {

    Usuarios findByNombre(String nombre);

    Optional<Usuarios> findByEmail(String email); 


}
