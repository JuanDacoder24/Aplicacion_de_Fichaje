package com.example.gestionfichaje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestionfichaje.entity.Fichajes;
import com.example.gestionfichaje.entity.Horarios;
import com.example.gestionfichaje.entity.LoginRequest;
import com.example.gestionfichaje.entity.LoginResponse;
import com.example.gestionfichaje.entity.Pausas;
import com.example.gestionfichaje.entity.Solicitudes;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.security.JwtUtil;
import com.example.gestionfichaje.services.FichajeServices;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
        try{
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getNombre());
            if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userDetails.getUsername())));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
            }
        }catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

    //Obtener todos los usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<?> getAllUsuarios() {
        try {
            return ResponseEntity.ok(fichajeServices.getAllUsuarios());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los usuarios");
        }
    }

    //Obtener usuario por id
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(fichajeServices.getUsuarioById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario");
        }
    }

    //Registrar un nuevo usuario
    @PostMapping("/usuarios")
    public ResponseEntity<?> createUsuario(@RequestBody Usuarios usuario) {
        try {
            Usuarios savedUsuario = fichajeServices.saveUsuario(usuario);
            return ResponseEntity.ok(savedUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
        }
    }

    //Actualizar usuario por id
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

    //Obtener todos los fichajes con paginacion
    @GetMapping(value = "/fichajes")
    public ResponseEntity<Page<Fichajes>> getFichajes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {

        return ResponseEntity.ok(fichajeServices.getAllFichajes(page, size));
    }

    //Obtener fichajes por usuario
    @GetMapping("/fichajes/{usuarioId}")
    public ResponseEntity<?> getFichajesByUsuario(@PathVariable Integer usuarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        try {
            return ResponseEntity.ok(fichajeServices.getFichajesByUsuario(usuarioId, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los fichajes del usuario");
        }
    }

    //Registrar un fichaje
    @PostMapping("/fichajes")
    public ResponseEntity<?> createFichaje(@RequestBody Fichajes fichaje) {
        try {
            Fichajes savedFichaje = fichajeServices.saveFichaje(fichaje);
            return ResponseEntity.ok(savedFichaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el fichaje");
        }
    }

    //Actualizar fichaje por id
    @PutMapping("/fichajes/{id}")
    public ResponseEntity<?> updateFichaje(@PathVariable Integer id, @RequestBody Fichajes fichaje) {
        try {
            fichaje.setId(id);
            Fichajes updatedFichaje = fichajeServices.saveFichaje(fichaje);
            return ResponseEntity.ok(updatedFichaje);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el fichaje");
        }
    }

    //Eliminar fichaje por id
    @DeleteMapping("/fichajes/{id}")
    public ResponseEntity<?> deleteFichaje(@PathVariable Integer id) {
        try {
            fichajeServices.deleteFichaje(id);
            return ResponseEntity.ok("Fichaje eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el fichaje");
        }
    }

    //Obtener todos los horarios
    @GetMapping("/horarios")
    public ResponseEntity<?> getHorarios() {
        try {
            return ResponseEntity.ok(fichajeServices.getAllHorarios());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los horarios");
        }
    }

    //Registrar nuevo horario
    @PostMapping("/horarios")
    public ResponseEntity<?> createHorario(@RequestBody Horarios horario) {
        try {
            Horarios savedHorario = fichajeServices.saveHorario(horario);
            return ResponseEntity.ok(savedHorario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el horario");
        }
    }

    //Actualizar horario por id
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

    //Eliminar horario por id del usuario
    @DeleteMapping("/horarios/{usuarioId}")
    public ResponseEntity<?> deleteHorario(@PathVariable Integer usuarioId) {
        try {
            fichajeServices.deleteHorario(usuarioId);
            return ResponseEntity.ok("Horario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el horario");
        }
    }

    //CRUD para Pausas
    @GetMapping("/pausas")
    public ResponseEntity<?> getAllPausas(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
        try {
            return ResponseEntity.ok(fichajeServices.getAllPausas(page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las pausas");
        }
    }

    @GetMapping("/pausas/{id}")
    public ResponseEntity<?> getPausaById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(fichajeServices.getPausaById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la pausa");
        }
    }

    @PostMapping("/pausas")
    public ResponseEntity<?> createPausa(@RequestBody Pausas pausa) {
        try {
            Pausas savedPausa = fichajeServices.savePausa(pausa);
            return ResponseEntity.ok(savedPausa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la pausa");
        }
    }

    @PutMapping("/pausas/{id}")
    public ResponseEntity<?> updatePausa(@PathVariable Integer id, @RequestBody Pausas pausa) {
        try {
            pausa.setId(id);
            Pausas updatedPausa = fichajeServices.savePausa(pausa);
            return ResponseEntity.ok(updatedPausa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la pausa");
        }
    }

    @DeleteMapping("/pausas/{id}")
    public ResponseEntity<?> deletePausa(@PathVariable Integer id) {
        try {
            fichajeServices.deletePausa(id);
            return ResponseEntity.ok("Pausa eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la pausa");
        }
    }

    //CRUD para Solicitudes
    @GetMapping("/solicitudes")
    public ResponseEntity<?> getAllSolicitudes(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size) {
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
