package com.example.gestionfichaje.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.gestionfichaje.dto.FichajeDTO;
import com.example.gestionfichaje.dto.HorarioDTO;
import com.example.gestionfichaje.entity.DiaSemana;
import com.example.gestionfichaje.entity.EstadoJustificante;
import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.Justificante;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.FichajesRepository;
import com.example.gestionfichaje.repository.HorariosRepository;
import com.example.gestionfichaje.repository.JustificanteRepository;
import com.example.gestionfichaje.repository.SolicitudesRepository;
import com.example.gestionfichaje.repository.UsuariosRepository;

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

    @Autowired
    private HorariosRepository repo;

    public Horarios guardar(Horarios h) {
        return repo.save(h);
    }

    public List<Horarios> obtenerPorUsuario(Long id) {
        return repo.findByUsuarioId(id);
    }

    public List<Horarios> obtenerTodos() {
        return repo.findAll();
    }

    public Fichajes saveFichaje(Fichajes fichaje) {
        return fichajesRepository.save(fichaje);
    }

    public Horarios crearHorario(HorarioDTO dto) {

        Usuarios usuario = usuariosRepository.findById(dto.usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Horarios h = new Horarios();
        h.setUsuario(usuario);
        h.setDiaSemana(DiaSemana.valueOf(dto.diaSemana));
        h.setHoraInicio(LocalTime.parse(dto.horaInicio));
        h.setHoraFin(LocalTime.parse(dto.horaFin));

        return horariosRepository.save(h);
    }

    public List<Fichajes> getByRango(String inicio, String fin) {

        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);

        return fichajesRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public Fichajes registrarEntrada(FichajeDTO req) {
        LocalDate fecha = LocalDate.parse(req.getFecha());

        Fichajes abierto = findFichajeAbierto(req.getUsuarioId(), fecha);
        if (abierto != null) {
            throw new RuntimeException("Ya tienes entrada abierta para " + req.getFecha());
        }

        Usuarios usuario = usuariosRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + req.getUsuarioId()));

        Fichajes fichaje = new Fichajes();
        fichaje.setUsuario(usuario);
        fichaje.setFecha(fecha);
        fichaje.setHoraEntrada(LocalDateTime.now());
        fichaje.setDescansoMinutos(req.getDescansoMinutos());

        return saveFichaje(fichaje);
    }

    public Fichajes registrarSalida(FichajeDTO req) {

        LocalDate fecha = LocalDate.parse(req.getFecha());
        Fichajes abierto = findFichajeAbierto(req.getUsuarioId(), fecha);

        if (abierto == null) {
            throw new RuntimeException("No tienes entrada abierta para cerrar");
        }

        abierto.setHoraSalida(LocalDateTime.now());
        calcularHorasTrabajadas(abierto);

        return saveFichaje(abierto);
    }

    public Fichajes findFichajeAbierto(Integer usuarioId, LocalDate fecha) {
        Optional<Fichajes> result = fichajesRepository.findByUsuarioIdAndFechaAndHoraSalidaIsNull(usuarioId, fecha);
        return result.orElse(null);
    }

    private void calcularHorasTrabajadas(Fichajes fichaje) {
        Duration total = Duration.between(fichaje.getHoraEntrada(), fichaje.getHoraSalida());

        BigDecimal horas = BigDecimal.valueOf(total.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        if (fichaje.getDescansoMinutos() != null && fichaje.getDescansoMinutos() > 0) {
            BigDecimal descuento = BigDecimal.valueOf(fichaje.getDescansoMinutos())
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
            horas = horas.subtract(descuento);
        }
        fichaje.setHorasTrabajadas(horas.intValue());
    }

    public Page<Fichajes> getAllFichajes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findAll(pageable);
    }

    public Page<Fichajes> getFichajesByUsuario(Integer usuarioId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fichajesRepository.findByUsuarioId(usuarioId, pageable);
    }

    public Fichajes getFichajeById(Integer id) {
        return fichajesRepository.findById(id).orElse(null);
    }

    public void deleteFichaje(Integer id) {
        fichajesRepository.deleteById(id);
    }

    public List<Horarios> getAllHorarios() {
        return horariosRepository.findAll();
    }

    public Horarios saveHorario(Horarios horario) {
        return horariosRepository.save(horario);
    }

    public void deleteHorario(Integer id) {
        horariosRepository.deleteById(id);
    }

    public void deleteSolicitud(Integer id) {
        solicitudesRepository.deleteById(id);
    }

    public List<Solicitudes> getAllSolicitudes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return solicitudesRepository.findAll(pageable).getContent();
    }

    public Solicitudes getSolicitudById(Integer id) {
        System.out.println("Buscando solicitud con ID: " + id);
        Solicitudes solicitud = solicitudesRepository.findById(id).orElse(null);
        System.out.println("Solicitud encontrada: " + (solicitud != null));
        return solicitud;
    }

    public Solicitudes saveSolicitud(Solicitudes solicitud) {
        System.out.println("Guardando solicitud ID: " + solicitud.getId() +
                ", Estado: " + solicitud.getEstado());
        return solicitudesRepository.save(solicitud);
    }

    public List<Solicitudes> getSolicitudesByEmail(String identifier) {
        Usuarios usuario = usuariosRepository.findByEmail(identifier)
                .or(() -> usuariosRepository.findByNombre(identifier))
                .orElseThrow(() -> new RuntimeException("No se encontró al usuario con: " + identifier));

        return solicitudesRepository.findByUsuario(usuario);
    }

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

    public List<FichajeDTO> getAllFichajesDTO() {
        return fichajesRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Value("${app.upload.directorio:./uploads/justificantes}")
    private String directorioUpload;

    @Autowired
    private JustificanteRepository justificanteRepository;

    public Justificante guardarJustificante(MultipartFile archivo, String tipoDocumento,
            Integer solicitudId, Integer fichajeId,
            String emailUsuario) {
        try {
            Path dirPath = Paths.get(directorioUpload);
            Files.createDirectories(dirPath);

            String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path rutaFinal = dirPath.resolve(nombreUnico);
            Files.copy(archivo.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            Usuarios usuario = usuariosRepository.findByEmail(emailUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Justificante j = new Justificante();
            j.setUsuario(usuario);
            j.setNombreArchivo(archivo.getOriginalFilename());
            j.setTipoDocumento(tipoDocumento);
            j.setRutaArchivo(rutaFinal.toString());
            j.setEstado(EstadoJustificante.PENDIENTE);

            if (solicitudId != null) {
                solicitudesRepository.findById(solicitudId).ifPresent(j::setSolicitud);
            }
            if (fichajeId != null) {
                fichajesRepository.findById(fichajeId).ifPresent(j::setFichaje);
            }

            return justificanteRepository.save(j);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    public Resource cargarJustificante(Integer id) throws Exception {
        Justificante justificante = justificanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificante no encontrado: " + id));

        Path filePath = Paths.get(justificante.getRutaArchivo());

        if (!Files.exists(filePath)) {
            throw new RuntimeException("Archivo no encontrado en: " + justificante.getRutaArchivo());
        }

        return new UrlResource(filePath.toUri());
    }

    public List<Justificante> getJustificantesPendientes() {
        return justificanteRepository.findByEstado(EstadoJustificante.PENDIENTE);
    }

    public Justificante getJustificanteById(Integer id) {
        return justificanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificante no encontrado: " + id));
    }

    public List<Justificante> getAllJustificantes() {
        return justificanteRepository.findAll();
    }

    public List<Justificante> getMisJustificantes(String email) {
        Usuarios usuario = usuariosRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return justificanteRepository.findByUsuarioId(usuario.getId());
    }

    public Optional<Usuarios> findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    public Justificante revisarJustificante(Integer id, EstadoJustificante estado,
            String comentario, String emailAdmin) {
        Justificante j = justificanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado"));

        Usuarios admin = usuariosRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        j.setEstado(estado);
        j.setRevisadoPor(admin);

        return justificanteRepository.save(j);
    }

    // Mapeo de entidad Fichajes a FichajeDTO
    private FichajeDTO toDTO(Fichajes f) {
        FichajeDTO dto = new FichajeDTO();
        dto.setId(f.getId());
        dto.setUsuarioId(f.getUsuario() != null ? f.getUsuario().getId() : 0);
        dto.setFecha(f.getFecha() != null ? f.getFecha().toString() : null);
        dto.setHora(f.getHoraEntrada());
        dto.setHoraEntrada(f.getHoraEntrada());
        dto.setHoraSalida(f.getHoraSalida());
        dto.setHorasTrabajadas(f.getHorasTrabajadas());
        dto.setTipo(f.getHoraSalida() == null ? "ENTRADA" : "SALIDA");
        dto.setDescansoMinutos(f.getDescansoMinutos() != null ? f.getDescansoMinutos() : 0);
        if (f.getUsuario() != null) {
            dto.setNombreUsuario(f.getUsuario().getNombre());
            dto.setDepartamento(String.valueOf(f.getUsuario().getDepartamento()));
        }
        return dto;
    }

}
