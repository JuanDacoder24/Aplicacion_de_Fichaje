package com.example.gestionfichaje.dto;

import java.time.LocalDateTime;

public class FichajeDTO {

    private int id;
    private int usuarioId;
    private String fecha; 
    private LocalDateTime hora;
    private String tipo;
    private int descansoMinutos;
    
    public FichajeDTO() {
    }

    public FichajeDTO(int id, int usuarioId, String fecha, LocalDateTime hora, String tipo, int descansoMinutos) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.descansoMinutos = descansoMinutos;
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

   
    

}
