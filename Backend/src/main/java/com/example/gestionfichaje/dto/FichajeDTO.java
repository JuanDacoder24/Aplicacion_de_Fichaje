package com.example.gestionfichaje.dto;

public class FichajeDTO {

    private int id;
    private int usuarioId;
    private String fecha; 
    private String tipo;
    private int descansoMinutos;

    
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
