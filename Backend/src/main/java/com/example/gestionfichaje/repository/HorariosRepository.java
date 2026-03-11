package com.example.gestionfichaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.gestionfichaje.entity.Horarios;

@Repository 
public interface HorariosRepository extends JpaRepository<Horarios, Integer> {

}
