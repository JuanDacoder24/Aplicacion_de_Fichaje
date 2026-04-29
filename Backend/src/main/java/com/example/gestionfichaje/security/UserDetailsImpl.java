package com.example.gestionfichaje.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.gestionfichaje.entity.Usuarios;

public class UserDetailsImpl implements UserDetails {

    private int id;
    private String nombre;
    private String password;
    private String rol;

    public UserDetailsImpl(int id, String nombre, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
    }

    // Aqui construimos el UserDetailsImpl a partir de tu entidad Usuarios
    public static UserDetailsImpl build(Usuarios user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getNombre(),
                user.getPasswordHash(),
                user.getRol().getNombre());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nombre;
    } // login por nombre

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }
}