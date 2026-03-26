package com.example.gestionfichaje.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.gestionfichaje.dto.FichajeDTO;
import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.LoginRequest;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.security.JwtUtil;
import com.example.gestionfichaje.security.UserDetailsImpl;
import com.example.gestionfichaje.services.FichajeServices;

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
                "token",  token,
                "id",     userDetails.getId(),
                "nombre", userDetails.getUsername(),
                "rol",    userDetails.getRol()   // "ADMIN" o "EMPLEADO"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        }

    } catch (UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
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
    public ResponseEntity<Page<Fichajes>> getFichajes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        return ResponseEntity.ok(fichajeServices.getAllFichajes(page, size));
    }

    @GetMapping("/fichajes/{usuarioId}")
    public ResponseEntity<?> getFichajesByUsuario(@PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        try {
            return ResponseEntity.ok(fichajeServices.getFichajesByUsuario(usuarioId, page, size));
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
    public ResponseEntity<?> createHorario(@RequestBody Horarios horario) {
        try {
            Horarios savedHorario = fichajeServices.saveHorario(horario);
            return ResponseEntity.ok(savedHorario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el horario");
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

    @DeleteMapping("/horarios/{usuarioId}")
    public ResponseEntity<?> deleteHorario(@PathVariable Integer usuarioId) {
        try {
            fichajeServices.deleteHorario(usuarioId);
            return ResponseEntity.ok("Horario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el horario");
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
}
