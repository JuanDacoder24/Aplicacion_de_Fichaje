package com.example.gestionfichaje.services;

import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.Pausas;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.FichajesRepository;
import com.example.gestionfichaje.repository.HorariosRepository;
import com.example.gestionfichaje.repository.PausasRepository;
import com.example.gestionfichaje.repository.SolicitudesRepository;
import com.example.gestionfichaje.repository.UsuariosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichajeServices {

    //Inyeccion de repositorios para poder trabajarlos en el services
    @Autowired
    private FichajesRepository fichajesRepository;

    @Autowired
    private HorariosRepository horariosRepository;

    @Autowired
    private PausasRepository pausasRepository;

    @Autowired
    private SolicitudesRepository solicitudesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    //Registrar un fichaje
    public Fichajes saveFichaje(Fichajes fichaje) {
        return fichajesRepository.save(fichaje);
    }

    //Obtener todos los fichajes con paginacion
    public Page<Fichajes> getAllFichajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findAll(pageable);
    }

    //Buscar fichajes por usuario
    public Page<Fichajes> getFichajesByUsuario(Integer usuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findByUsuarioId(usuarioId, pageable);
    }

    //CRUD para Fichajes
    public Fichajes getFichajeById(Integer id) {
        return fichajesRepository.findById(id).orElse(null);
    }

    public void deleteFichaje(Integer id) {
        fichajesRepository.deleteById(id);
    }

    //CRUD para Horarios
    public List<Horarios> getAllHorarios() {
        return horariosRepository.findAll();
    }

    public Horarios saveHorario(Horarios horario) {
        return horariosRepository.save(horario);
    }

    public void deleteHorario(Integer id) {
        horariosRepository.deleteById(id);
    }

    //CRUD para Pausas
    public List<Pausas> getAllPausas() {
        return pausasRepository.findAll();
    }

    public Pausas savePausa(Pausas pausa) {
        return pausasRepository.save(pausa);
    }

    public void deletePausa(Integer id) {
        pausasRepository.deleteById(id);
    }

    // Paginación para Pausas
    public List<Pausas> getAllPausas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pausasRepository.findAll(pageable).getContent();
    }

    public Pausas getPausaById(Integer id) {
        return pausasRepository.findById(id).orElse(null);
    }

    //CRUD para Solicitudes
    public List<Solicitudes> getAllSolicitudes() {
        return solicitudesRepository.findAll();
    }

    public Solicitudes saveSolicitud(Solicitudes solicitud) {
        return solicitudesRepository.save(solicitud);
    }

    public void deleteSolicitud(Integer id) {
        solicitudesRepository.deleteById(id);
    }

    // Paginación para Solicitudes
    public List<Solicitudes> getAllSolicitudes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return solicitudesRepository.findAll(pageable).getContent();
    }

    public Solicitudes getSolicitudById(Integer id) {
        return solicitudesRepository.findById(id).orElse(null);
    }

    //CRUD para Usuarios
    public List<Usuarios> getAllUsuarios() {
        return usuariosRepository.findAll();
    }

    public Usuarios getUsuarioById(Integer id) {
        return usuariosRepository.findById(id).orElse(null);
    }

    public Usuarios saveUsuario(Usuarios usuario) {
        return usuariosRepository.save(usuario);
    }

    public void deleteUsuario(Integer id) {
        usuariosRepository.deleteById(id);
    }
}
