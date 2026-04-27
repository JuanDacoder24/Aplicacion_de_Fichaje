package com.example.gestionfichaje.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "justificantes")
public class Justificante {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "fichaje_id")
    private Fichajes fichaje; 

    @ManyToOne
    @JoinColumn(name = "solicitud_id")
    private Solicitudes solicitud; 

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "tipo_documento")
    private String tipoDocumento; 

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Enumerated(EnumType.STRING)
    private EstadoJustificante estado = EstadoJustificante.PENDIENTE;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "revisado_por")
    private Usuarios revisadoPor;

    public Justificante() {
    }

    public Justificante(Integer id, Usuarios usuario, Fichajes fichaje, Solicitudes solicitud, String nombreArchivo,
            String tipoDocumento, String rutaArchivo, EstadoJustificante estado,
            LocalDateTime fechaSubida, Usuarios revisadoPor) {
        this.id = id;
        this.usuario = usuario;
        this.fichaje = fichaje;
        this.solicitud = solicitud;
        this.nombreArchivo = nombreArchivo;
        this.tipoDocumento = tipoDocumento;
        this.rutaArchivo = rutaArchivo;
        this.estado = estado;
        this.fechaSubida = fechaSubida;
        this.revisadoPor = revisadoPor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Solicitudes getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitudes solicitud) {
        this.solicitud = solicitud;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public EstadoJustificante getEstado() {
        return estado;
    }

    public void setEstado(EstadoJustificante estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Usuarios getRevisadoPor() {
        return revisadoPor;
    }

    public void setRevisadoPor(Usuarios revisadoPor) {
        this.revisadoPor = revisadoPor;
    }

    
}


