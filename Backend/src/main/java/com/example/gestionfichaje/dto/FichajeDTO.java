package com.example.gestionfichaje.dto;

import java.time.LocalDateTime;

public class FichajeDTO {

    private int id;
    private int usuarioId;
    private String fecha; 
    private LocalDateTime hora;
    private String tipo;
    private int descansoMinutos;
    private String nombreUsuario;
    private String departamento;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private int horasTrabajadas;
    
    public FichajeDTO() {
    }

    public FichajeDTO(int id, int usuarioId, String fecha, LocalDateTime hora, String tipo, int descansoMinutos, String nombreUsuario, String departamento, LocalDateTime horaEntrada, LocalDateTime horaSalida, int horasTrabajadas) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.descansoMinutos = descansoMinutos;
        this.nombreUsuario = nombreUsuario;
        this.departamento = departamento;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horasTrabajadas = horasTrabajadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDescansoMinutos() {
        return descansoMinutos;
    }

    public void setDescansoMinutos(int descansoMinutos) {
        this.descansoMinutos = descansoMinutos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

   
    

}
