package com.example.gestionfichaje.services;

import com.example.gestionfichaje.entity.EstadoJustificante;
import com.example.gestionfichaje.entity.Justificante;
import com.example.gestionfichaje.entity.Usuarios;
import com.example.gestionfichaje.repository.FichajesRepository;
import com.example.gestionfichaje.repository.JustificanteRepository;
import com.example.gestionfichaje.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JustificanteService {

    @Value("${app.upload.directorio:./uploads/justificantes}")
    private String directorioUpload;

    @Autowired
    private JustificanteRepository justificanteRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private FichajesRepository fichajesRepository;

    public Justificante guardar(MultipartFile archivo, String tipoDocumento,
            Integer fichajeId, String emailUsuario) throws IOException {

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
        j.setFechaSubida(LocalDateTime.now());

        if (fichajeId != null) {
            fichajesRepository.findById(fichajeId).ifPresent(j::setFichaje);
        }

        return justificanteRepository.save(j);
    }



    public Resource cargarArchivo(Integer id) throws IOException {
        Justificante j = justificanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificante no encontrado"));

        Path ruta = Paths.get(j.getRutaArchivo());
        Resource resource = new UrlResource(ruta.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Archivo no encontrado en disco");
        }
        return resource;
    }

    public Justificante revisar(Integer id, EstadoJustificante estado,
            String comentario, String emailAdmin) {
        Justificante j = justificanteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Justificante no encontrado"));

        Usuarios admin = usuariosRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        j.setEstado(EstadoJustificante.PENDIENTE);
        j.setRevisadoPor(admin);

        return justificanteRepository.save(j);
    }

    public List<Justificante> getPendientes() {
        return justificanteRepository.findByEstado(EstadoJustificante.PENDIENTE);
    }

    public List<Justificante> getMisJustificantes(String email) {
        return justificanteRepository.findByUsuarioEmail(email);
    }
}