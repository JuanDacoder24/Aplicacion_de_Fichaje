package com.example.gestionfichaje.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // Muchos horarios pertenecen a un usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;
    private LocalDateTime hora_inicio;
    private LocalDateTime hora_fin;
    private int horas_semanales;

    public Horarios() {}

    public Horarios(int id, Usuarios usuario, LocalDateTime hora_inicio, LocalDateTime hora_fin, int horas_semanales) {
        this.id = id;
        this.usuario = usuario;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.horas_semanales = horas_semanales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
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

    public int getHoras_semanales() {
        return horas_semanales;
    }

    public void setHoras_semanales(int horas_semanales) {
        this.horas_semanales = horas_semanales;
    }

    

    



}
    