package com.example.gestionfichaje.services;

import com.example.gestionfichaje.dto.FichajeDTO;
import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.FichajesRepository;
import com.example.gestionfichaje.repository.HorariosRepository;
import com.example.gestionfichaje.repository.SolicitudesRepository;
import com.example.gestionfichaje.repository.UsuariosRepository;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FichajeServices {

    // Inyeccion de repositorios para poder trabajarlos en el services
    @Autowired
    private FichajesRepository fichajesRepository;

    @Autowired
    private HorariosRepository horariosRepository;

    @Autowired
    private SolicitudesRepository solicitudesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public Fichajes saveFichaje(Fichajes fichaje) {
        return fichajesRepository.save(fichaje);
    }

    public Fichajes registrarEntrada(FichajeDTO req) {
        Fichajes abierto = findAbierto(req.getUsuarioId(), LocalDate.parse(req.getFecha()));
        if (abierto != null) {
            throw new RuntimeException("Ya tienes entrada abierta para " + req.getFecha());
        }

        Fichajes fichaje = new Fichajes();
        fichaje.setUsuarioId(req.getUsuarioId());
        fichaje.setFecha(LocalDate.parse(req.getFecha()));
        fichaje.setHora_entrada(LocalDateTime.now());
        fichaje.setDescanso_minutos(req.getDescansoMinutos());

        return saveFichaje(fichaje);
    }

    public Fichajes registrarSalida(FichajeDTO req) {
        Fichajes abierto = findAbierto(req.getUsuarioId(), LocalDate.parse(req.getFecha()));

        if (abierto == null) {
            throw new RuntimeException("No tienes entrada abierta para cerrar");
        }

        // Calcular automáticamente
        abierto.setHora_salida(LocalDateTime.now());
        calcularHorasTrabajadas(abierto);

        return saveFichaje(abierto);
    }

    private Fichajes findAbierto(Integer usuarioId, LocalDate fecha) {
        return fichajesRepository.findByUsuarioIdAndFechaAndHoraSalidaIsNull(usuarioId, fecha);
    }

    private void calcularHorasTrabajadas(Fichajes fichaje) {
        Duration total = Duration.between(fichaje.getHora_entrada(), fichaje.getHora_salida());

        BigDecimal horas = BigDecimal.valueOf(total.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // Restar descanso
        if (fichaje.getDescanso_minutos() != null && fichaje.getDescanso_minutos() > 0) {
            BigDecimal descuento = BigDecimal.valueOf(fichaje.getDescanso_minutos())
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP); 
            horas = horas.subtract(descuento);
        }
        //poner horas trabajadas en el fichaje y en la base de datos
        fichaje.setHorasTrabajadas(horas);
    }

    // Obtener todos los fichajes con paginacion
    public Page<Fichajes> getAllFichajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findAll(pageable);
    }

    // Buscar fichajes por usuario
    public Page<Fichajes> getFichajesByUsuario(Integer usuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findByUsuarioId(usuarioId, pageable);
    }

    // CRUD para Fichajes
    public Fichajes getFichajeById(Integer id) {
        return fichajesRepository.findById(id).orElse(null);
    }

    public void deleteFichaje(Integer id) {
        fichajesRepository.deleteById(id);
    }

    // CRUD para Horarios
    public List<Horarios> getAllHorarios() {
        return horariosRepository.findAll();
    }

    public Horarios saveHorario(Horarios horario) {
        return horariosRepository.save(horario);
    }

    public void deleteHorario(Integer id) {
        horariosRepository.deleteById(id);
    }

    // CRUD para Solicitudes
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

    // CRUD para Usuarios
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
