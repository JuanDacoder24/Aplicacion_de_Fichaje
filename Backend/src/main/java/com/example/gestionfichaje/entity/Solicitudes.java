package com.example.gestionfichaje.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class Solicitudes {

    private int id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    // Muchas solicitudes pertenecen a un fichaje
    @ManyToOne
    @JoinColumn(name = "fichaje_id")
    private Fichajes fichaje;

    private String motivo;
    private String estado;

    public Solicitudes() {} 

    public Solicitudes(int id, Usuarios usuario, Fichajes fichaje, String motivo, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.fichaje = fichaje;
        this.motivo = motivo;
        this.estado = estado;
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

    
}
