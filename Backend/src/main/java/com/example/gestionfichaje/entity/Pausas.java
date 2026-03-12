package com.example.gestionfichaje.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pausas")
public class Pausas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "fichaje_id")
    private Fichajes fichaje;
    private LocalDateTime hora_inicio;
    private LocalDateTime hora_fin;

    public Pausas() {}

    public Pausas(int id, Fichajes fichaje, LocalDateTime hora_inicio, LocalDateTime hora_fin) {
        this.id = id;
        this.fichaje = fichaje;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fichajes getFichaje() {
        return fichaje;
    }

    public void setFichaje(Fichajes fichaje) {
        this.fichaje = fichaje;
    }

    public LocalDateTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(LocalDateTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public LocalDateTime getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(LocalDateTime hora_fin) {
        this.hora_fin = hora_fin;
    }

    

}
