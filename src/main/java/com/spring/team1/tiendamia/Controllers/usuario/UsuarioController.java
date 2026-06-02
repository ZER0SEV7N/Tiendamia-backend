package com.spring.team1.tiendamia.controllers.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.repository.usuario.DireccionUsuarioRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;

import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DireccionUsuarioRepository direccionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // GET /api/usuario/me
    @GetMapping("/me")
    public ResponseEntity<?> getPerfil() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuarios> opt = usuariosRepository.findByCorreo(correo);
        if (opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));

        Usuarios u = opt.get();

        List<DireccionesUsuarios> direcciones = direccionRepository.findByUsuarioId(u.getId());

        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "nombres", u.getNombres(),
                "apellidos", u.getApellidos(),
                "correo", u.getCorreo(),
                "telefono", u.getTelefono(),
                "activo", u.getEstado(),
                "rol", u.getRol() != null ? u.getRol().getNombre() : null,
                "direcciones", direcciones
        ));
    }

    // PATCH /api/usuario/me
    @PatchMapping("/me")
    public ResponseEntity<?> updatePerfil(@RequestBody Map<String, Object> body) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios u = usuariosRepository.findByCorreo(correo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (body.containsKey("nombres"))
            u.setNombres((String) body.get("nombres"));
        if (body.containsKey("apellidos"))
            u.setApellidos((String) body.get("apellidos"));
        if (body.containsKey("telefono"))
            u.setTelefono((String) body.get("telefono"));
        if (body.containsKey("password") && body.get("password") != null) {
            String raw = (String) body.get("password");
            if (!raw.isBlank()) {
                u.setPassword(passwordEncoder.encode(raw));
            }
        }

        usuariosRepository.save(u);

        return ResponseEntity.ok(Map.of("mensaje", "Perfil actualizado"));
    }

    // GET /api/usuario/direcciones
    @GetMapping("/direcciones")
    public ResponseEntity<?> getDirecciones() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios u = usuariosRepository.findByCorreo(correo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        List<DireccionesUsuarios> direcciones = direccionRepository.findByUsuarioId(u.getId());
        return ResponseEntity.ok(direcciones);
    }

    // POST /api/usuario/direcciones
    @PostMapping("/direcciones")
    public ResponseEntity<?> saveDireccion(@RequestBody DireccionesUsuarios direccion) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios u = usuariosRepository.findByCorreo(correo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        direccion.setUsuario(u);
        DireccionesUsuarios saved = direccionRepository.save(direccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // DELETE /api/usuario/direcciones/{id}
    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<?> deleteDireccion(@PathVariable Integer id) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios u = usuariosRepository.findByCorreo(correo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Optional<DireccionesUsuarios> opt = direccionRepository.findById(id);
        if (opt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Dirección no encontrada"));

        DireccionesUsuarios d = opt.get();
        if (d.getUsuario() == null || !d.getUsuario().getId().equals(u.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No autorizado"));

        direccionRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Dirección eliminada"));
    }
}
