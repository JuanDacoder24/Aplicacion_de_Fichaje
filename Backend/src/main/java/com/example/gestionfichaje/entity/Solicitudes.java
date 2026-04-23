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
@Table(name = "solicitudes")
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "fichaje_id")
    private Fichajes fichaje;

    private String motivo;
    private String estado;
    private LocalDateTime fechaSolicitud;

    public Solicitudes() {} 

    public Solicitudes(int id, Usuarios usuario, Fichajes fichaje, String motivo, String estado, LocalDateTime fechaSolicitud) {
        this.id = id;
        this.usuario = usuario;
        this.fichaje = fichaje;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaSolicitud = LocalDateTime.now();
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

    public Fichajes getFichaje() {
        return fichaje;
    }

    public void setFichaje(Fichajes fichaje) {
        this.fichaje = fichaje;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }
    
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    
}
