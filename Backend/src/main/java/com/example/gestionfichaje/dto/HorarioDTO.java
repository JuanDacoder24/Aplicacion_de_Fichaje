package com.example.gestionfichaje.dto;

public class HorarioDTO {

    public Integer usuarioId;
    public String diaSemana;
    public String horaInicio;
    public String horaFin;

    public HorarioDTO(String diaSemana, String horaFin, String horaInicio, Integer usuarioId) {
        this.diaSemana = diaSemana;
        this.horaFin = horaFin;
        this.horaInicio = horaInicio;
        this.usuarioId = usuarioId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

}