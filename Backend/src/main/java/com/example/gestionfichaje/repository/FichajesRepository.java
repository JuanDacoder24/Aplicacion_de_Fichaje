package com.example.gestionfichaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Fichajes;

@Repository
public interface FichajesRepository extends JpaRepository<Fichajes, Integer> {

}