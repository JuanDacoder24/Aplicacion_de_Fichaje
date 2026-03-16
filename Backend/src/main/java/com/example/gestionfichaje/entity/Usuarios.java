package com.example.gestionfichaje.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String email;
    private String password_hash;

    @Column(name = "departamento_id")
    private int departamento; 
     
    @Column(name = "rol_id")
    private int rol;

    private LocalDateTime fecha_alta;
    private boolean activo;

    // Un usuario tiene muchos fichajes
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Fichajes> fichajes;

    // Un usuario tiene muchos horarios
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Horarios> horarios;

    // Un usuario tiene muchas solicitudes
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Solicitudes> solicitudes;

    public Usuarios() {}


    public Usuarios(int id, String nombre, String email, String password_hash, int rol, int departamento,
            LocalDateTime fecha_alta, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password_hash = password_hash;
        this.rol = rol;
        this.departamento = departamento;
        this.fecha_alta = fecha_alta;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public LocalDateTime getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(LocalDateTime fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    

}
