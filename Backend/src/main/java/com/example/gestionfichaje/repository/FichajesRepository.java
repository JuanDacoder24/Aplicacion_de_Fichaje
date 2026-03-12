package com.example.gestionfichaje.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Fichajes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface FichajesRepository extends JpaRepository<Fichajes, Integer> {


    Page<Fichajes> findByUsuarioId(Integer usuarioId, Pageable pageable);

}