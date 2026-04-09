package com.example.gestionfichaje.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Rol {
    @Id
    private Integer id;
    private String nombre;

    @JsonCreator
    public Rol(@JsonProperty("id") Integer id) {
        this.id = id;
    }

    public Rol() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    } 

    
}