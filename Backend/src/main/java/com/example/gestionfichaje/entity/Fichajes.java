package com.example.gestionfichaje.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    private LocalDate fecha;

    @Column(name = "hora_entrada")
    private LocalDateTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Column(name = "descanso_minutos")
    private Integer descansoMinutos;

    @Column(name = "horas_trabajadas")
    private Integer horasTrabajadas;

    @OneToMany(mappedBy = "fichaje", cascade = CascadeType.ALL)
    private List<Ubicacion> ubicacion;

    @JsonIgnore
    @OneToMany(mappedBy = "fichaje", cascade = CascadeType.ALL)
    private List<Solicitudes> solicitudes;

    private String comentario;
    public Fichajes() {
    }

    public Fichajes(int id, Usuarios usuario, LocalDate fecha, LocalDateTime horaEntrada, LocalDateTime horaSalida,
            Integer descansoMinutos, int horasTrabajadas, String comentario) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.descansoMinutos = descansoMinutos;
        this.horasTrabajadas = 0;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
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

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Integer getDescansoMinutos() {
        return descansoMinutos;
    }

    public void setDescansoMinutos(Integer descansoMinutos) {
        this.descansoMinutos = descansoMinutos;
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

    public int getHorasTrabajadas() {
        return horasTrabajadas != null ? horasTrabajadas : 0;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
