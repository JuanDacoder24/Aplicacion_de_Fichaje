package com.example.gestionfichaje.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.gestionfichaje.dto.FichajeDTO;
import com.example.gestionfichaje.dto.HorarioDTO;
import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.Justificante;
import com.example.gestionfichaje.entity.LoginRequest;
import com.example.gestionfichaje.entity.Rol;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.RolRepository;
import com.example.gestionfichaje.security.JwtUtil;
import com.example.gestionfichaje.security.UserDetailsImpl;
import com.example.gestionfichaje.services.FichajeServices;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api")
public class FichajeController {

    @Autowired
    private FichajeServices fichajeServices;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(request.getNombre());

            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getRol());

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "id", userDetails.getId(),
                        "nombre", userDetails.getUsername(),
                        "rol", userDetails.getRol()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
            }

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

    @Autowired
    private RolRepository rolRepository;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {
            Usuarios usuario = new Usuarios();
            usuario.setNombre((String) body.get("nombre"));
            usuario.setEmail((String) body.get("email"));
            usuario.setPasswordHash(passwordEncoder.encode((String) body.get("passwordHash")));
            usuario.setDepartamento((Integer) body.get("departamento"));
            usuario.setActivo((Boolean) body.get("activo"));

            int rolId = (Integer) body.get("rol");
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRol(rol);

            Usuarios savedUsuario = fichajeServices.saveUsuario(usuario);
            return ResponseEntity.ok(savedUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> getAllUsuarios() {
        try {
            return ResponseEntity.ok(fichajeServices.getAllUsuarios());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los usuarios");
        }
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(fichajeServices.getUsuarioById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario");
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> createUsuario(@RequestBody Usuarios usuario) {
        try {
            Usuarios savedUsuario = fichajeServices.saveUsuario(usuario);
            return ResponseEntity.ok(savedUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Integer id, @RequestBody Usuarios usuario) {
        try {
            usuario.setId(id);
            Usuarios updatedUsuario = fichajeServices.saveUsuario(usuario);
            return ResponseEntity.ok(updatedUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario");
        }
    }

    @GetMapping("/fichajes")
    public ResponseEntity<List<FichajeDTO>> getFichajes() {
        return ResponseEntity.ok(fichajeServices.getAllFichajesDTO());
    }

    @GetMapping("/fichajes/rango")
    public ResponseEntity<?> getFichajesPorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        try {
            List<FichajeDTO> dtos = fichajeServices.getByRango(inicio, fin)
                    .stream()
                    .map(fichaje -> {
                        // Use the service's private toDTO method via a public wrapper
                        return fichajeServices.getAllFichajesDTO().stream()
                                .filter(dto -> dto.getId() == fichaje.getId())
                                .findFirst().orElse(null);
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error al filtrar fichajes");
        }
    }

    @GetMapping("/fichajes/{usuarioId}")
    public ResponseEntity<?> getFichajesByUsuario(@PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        try {
            List<FichajeDTO> dtos = fichajeServices.getFichajesByUsuario(usuarioId, page, size)
                    .stream()
                    .map(fichaje -> {
                        return fichajeServices.getAllFichajesDTO().stream()
                                .filter(dto -> dto.getId() == fichaje.getId())
                                .findFirst().orElse(null);
                    })
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los fichajes del usuario");
        }
    }

    @PostMapping("/fichajes")
    public ResponseEntity<?> createFichaje(@RequestBody FichajeDTO req) {
        try {
            Fichajes resultado;

            if ("ENTRADA".equalsIgnoreCase(req.getTipo())) {
                resultado = fichajeServices.registrarEntrada(req);
            } else if ("SALIDA".equalsIgnoreCase(req.getTipo())) {
                resultado = fichajeServices.registrarSalida(req);
            } else {
                return ResponseEntity.badRequest()
                        .body("Tipo inválido. Use: ENTRADA o SALIDA");
            }

            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor");
        }
    }

    @PutMapping("/fichajes/{id}")
    public ResponseEntity<?> updateFichaje(@PathVariable Integer id, @RequestBody Fichajes fichaje) {
        try {
            fichaje.setId(id);
            Fichajes updatedFichaje = fichajeServices.saveFichaje(fichaje);
            return ResponseEntity.ok(updatedFichaje);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el fichaje");
        }
    }

    @DeleteMapping("/fichajes/{id}")
    public ResponseEntity<?> deleteFichaje(@PathVariable Integer id) {
        try {
            fichajeServices.deleteFichaje(id);
            return ResponseEntity.ok("Fichaje eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el fichaje");
        }
    }

    @GetMapping("/horarios")
    public ResponseEntity<?> getHorarios() {
        try {
            return ResponseEntity.ok(fichajeServices.getAllHorarios());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los horarios");
        }
    }

    @PostMapping("/horarios")
    public ResponseEntity<?> createHorario(@RequestBody HorarioDTO dto) {
        try {
            Horarios h = fichajeServices.crearHorario(dto);
            return ResponseEntity.ok(h);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el horario");
        }
    }

    @PutMapping("/horarios/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable Integer id, @RequestBody Horarios horario) {
        try {
            horario.setId(id);
            Horarios updatedHorario = fichajeServices.saveHorario(horario);
            return ResponseEntity.ok(updatedHorario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el horario");
        }
    }

    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<?> deleteHorario(@PathVariable Integer id) {
        try {
            fichajeServices.deleteHorario(id);
            return ResponseEntity.ok("Horario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el horario");
        }
    }

    @GetMapping("/solicitudes")
    public ResponseEntity<?> getAllSolicitudes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        try {
            return ResponseEntity.ok(fichajeServices.getAllSolicitudes(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las solicitudes");
        }
    }

    @GetMapping("/solicitudes/{id}")
    public ResponseEntity<?> getSolicitudById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(fichajeServices.getSolicitudById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la solicitud");
        }
    }

    @PostMapping("/solicitudes")
    public ResponseEntity<?> createSolicitud(@RequestBody Solicitudes solicitud) {
        try {
            Solicitudes savedSolicitud = fichajeServices.saveSolicitud(solicitud);
            return ResponseEntity.ok(savedSolicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la solicitud");
        }
    }

    @PutMapping("/solicitudes/{id}")
    public ResponseEntity<?> updateSolicitud(@PathVariable Integer id, @RequestBody Solicitudes solicitud) {
        try {
            solicitud.setId(id);
            Solicitudes updatedSolicitud = fichajeServices.saveSolicitud(solicitud);
            return ResponseEntity.ok(updatedSolicitud);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la solicitud");
        }
    }

    @DeleteMapping("/solicitudes/{id}")
    public ResponseEntity<?> deleteSolicitud(@PathVariable Integer id) {
        try {
            fichajeServices.deleteSolicitud(id);
            return ResponseEntity.ok("Solicitud eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la solicitud");
        }
    }

    @PostMapping("/justificantes/subir")
    public ResponseEntity<?> subirJustificante(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam(value = "solicitudId", required = false) Integer solicitudId,
            @RequestParam(value = "fichajeId", required = false) Integer fichajeId,
            Authentication auth) {
        try {
            if (archivo.isEmpty())
                return ResponseEntity.badRequest().body("Archivo vacío");
            if (!"application/pdf".equals(archivo.getContentType()))
                return ResponseEntity.badRequest().body("Solo se permiten PDFs");
            if (archivo.getSize() > 5 * 1024 * 1024)
                return ResponseEntity.badRequest().body("Máximo 5MB");

            Justificante j = fichajeServices.guardarJustificante(
                    archivo, tipoDocumento, solicitudId, fichajeId, auth.getName());
            return ResponseEntity.ok(Map.of("id", j.getId(), "mensaje", "Subido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir: " + e.getMessage());
        }
    }

    @GetMapping("/justificantes/{id}/ver")
    public ResponseEntity<?> verJustificante(@PathVariable Integer id, Authentication auth) {
        try {
            Resource resource = fichajeServices.cargarJustificante(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"justificante.pdf\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Archivo no encontrado");
        }
    }

    @GetMapping("/justificantes/pendientes")
    public ResponseEntity<?> getJustificantesPendientes() {
        try {
            return ResponseEntity.ok(fichajeServices.getJustificantesPendientes());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener justificantes");
        }
    }

    //corregir path ver la forma de saber si es admin o no para mostrar unos u otros justificantes
    @GetMapping("/justificantes/miJustificantes")
    public ResponseEntity<?> getMisJustificantes(Authentication auth) {
        try {
            return ResponseEntity.ok(fichajeServices.getMisJustificantes(auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener justificantes");
        }
    }

    @PutMapping("/justificantes/{id}/revisar")
    public ResponseEntity<?> revisarJustificante(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            Authentication auth) {
        try {
            //pendiente a revisar si dejamos como null o no
            Justificante j = fichajeServices.revisarJustificante(id, null, null, null);
            return ResponseEntity.ok(j);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al revisar: " + e.getMessage());
        }
    }

    @GetMapping("/fichajes/abierto")
    public ResponseEntity<?> getFichajeAbierto(@RequestParam Integer usuarioId, @RequestParam String fecha) {
        try {
            Fichajes abierto = fichajeServices.findAbierto(usuarioId, java.time.LocalDate.parse(fecha));
            if (abierto != null) {
                return ResponseEntity.ok(fichajeServices.getAllFichajesDTO().stream()
                        .filter(dto -> dto.getId() == abierto.getId())
                        .findFirst().orElse(null));
            } else {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar fichaje abierto");
        }
    }
}
