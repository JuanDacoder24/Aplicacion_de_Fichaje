package com.example.gestionfichaje.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fichajes")
public class Fichajes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // Muchos fichajes pueden pertenecer a un usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;
    private LocalDate fecha;
    private LocalDateTime hora_entrada;
    private LocalDateTime hora_salida;
    private int descanso_minutos;
    //calcular horas trabajadas a partir de hora_entrada, hora_salida y descanso_minutos

    // En Fichajes.java — un fichaje tiene hasta 2 ubicaciones (entrada y salida)
    @OneToMany(mappedBy = "fichaje", cascade = CascadeType.ALL)
    private List<Ubicacion> ubicacion;

    // Un fichaje puede tener muchas solicitudes
    @OneToMany(mappedBy = "fichaje", cascade = CascadeType.ALL)
    private List<Solicitudes> solicitudes;

    private String comentario;

    public Fichajes() {
    }

    public Fichajes(int id, Usuarios usuario, LocalDate fecha, LocalDateTime hora_entrada, LocalDateTime hora_salida,
            int descanso_minutos, String comentario) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.descanso_minutos = descanso_minutos;
        this.comentario = comentario;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(LocalDateTime hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public LocalDateTime getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(LocalDateTime hora_salida) {
        this.hora_salida = hora_salida;
    }

    public int getDescanso_minutos() {
        return descanso_minutos;
    }

    public void setDescanso_minutos(int descanso_minutos) {
        this.descanso_minutos = descanso_minutos;
    }

    public List<Ubicacion> getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(List<Ubicacion> ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Solicitudes> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitudes> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
