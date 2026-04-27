package com.example.gestionfichaje.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.UsuariosRepository;
import com.example.gestionfichaje.security.UserDetailsImpl;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuarios usuario = usuariosRepository.findByNombre(nombre)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario no activo: " + nombre);
        }

        return UserDetailsImpl.build(usuario); 
    }
}