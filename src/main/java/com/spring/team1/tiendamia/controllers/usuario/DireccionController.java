package com.spring.team1.tiendamia.Controllers.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.usuario.DireccionesUsuarios;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.repository.usuario.DireccionesUsuariosRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class DireccionController {

    @Autowired
    private DireccionesUsuariosRepository direccionesUsuariosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/direcciones")
    public ResponseEntity<?> getDirecciones() {
        Usuarios usuario = usuariosRepository.findById(1)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        List<DireccionesUsuarios> direccionesBD = direccionesUsuariosRepository.findByUsuario_Id(usuario.getId());
        List<Map<String, Object>> response = new ArrayList<>();

        for (DireccionesUsuarios d : direccionesBD) {
            Map<String, Object> dir = new HashMap<>();
            dir.put("id", d.getId());
            dir.put("direccion", d.getDireccion());
            dir.put("referencia", d.getReferencia());
            dir.put("es_principal", Boolean.TRUE.equals(d.getEs_principal()));
            dir.put("createAt", d.getCreateAt() != null ? d.getCreateAt().toString() : null);
            if (d.getDistrito() != null) {
                dir.put("distrito", d.getDistrito().getNombre());
                if (d.getDistrito().getProvincia() != null) {
                    dir.put("provincia", d.getDistrito().getProvincia().getNombre());
                    if (d.getDistrito().getProvincia().getDepartamento() != null) {
                        dir.put("departamento", d.getDistrito().getProvincia().getDepartamento().getNombre());
                    }
                }
            }
            response.add(dir);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/direcciones/{id}")
    public ResponseEntity<?> getDireccion(@PathVariable Integer id) {
        DireccionesUsuarios direccion = direccionesUsuariosRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dirección no encontrada"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", direccion.getId());
        response.put("direccion", direccion.getDireccion());
        response.put("referencia", direccion.getReferencia());
        response.put("es_principal", Boolean.TRUE.equals(direccion.getEs_principal()));
        response.put("createAt", direccion.getCreateAt() != null ? direccion.getCreateAt().toString() : null);
        if (direccion.getDistrito() != null) {
            response.put("distrito", direccion.getDistrito().getNombre());
            if (direccion.getDistrito().getProvincia() != null) {
                response.put("provincia", direccion.getDistrito().getProvincia().getNombre());
                if (direccion.getDistrito().getProvincia().getDepartamento() != null) {
                    response.put("departamento", direccion.getDistrito().getProvincia().getDepartamento().getNombre());
                }
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/direcciones")
    public ResponseEntity<?> guardarDireccion(@RequestBody Map<String, Object> direccionData) {
        Usuarios usuario = usuariosRepository.findById(1)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        DireccionesUsuarios direccion = new DireccionesUsuarios();
        direccion.setUsuario(usuario);
        direccion.setDireccion((String) direccionData.getOrDefault("direccion", ""));
        direccion.setReferencia((String) direccionData.getOrDefault("referencia", ""));
        direccion.setEs_principal(Boolean.valueOf(String.valueOf(direccionData.getOrDefault("es_principal", false))));

        if (direccion.getReferencia() == null || direccion.getReferencia().isBlank()) {
            String departamento = (String) direccionData.getOrDefault("departamento", "");
            String provincia = (String) direccionData.getOrDefault("provincia", "");
            String distrito = (String) direccionData.getOrDefault("distrito", "");
            direccion.setReferencia(String.join(" / ", departamento, provincia, distrito).replaceAll("^\s*/\s*|\s*/\s*$", ""));
        }

        DireccionesUsuarios direccionGuardada = direccionesUsuariosRepository.save(direccion);
        Map<String, Object> response = new HashMap<>();
        response.put("id", direccionGuardada.getId());
        response.put("mensaje", "Dirección guardada con éxito");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/direcciones/{id}")
    public ResponseEntity<?> actualizarDireccion(@PathVariable Integer id,
            @RequestBody Map<String, Object> direccionData) {
        DireccionesUsuarios direccion = direccionesUsuariosRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dirección no encontrada"));

        if (direccionData.containsKey("direccion")) {
            direccion.setDireccion((String) direccionData.get("direccion"));
        }
        if (direccionData.containsKey("referencia")) {
            direccion.setReferencia((String) direccionData.get("referencia"));
        }
        if (direccionData.containsKey("es_principal")) {
            direccion.setEs_principal(Boolean.valueOf(String.valueOf(direccionData.get("es_principal"))));
        }

        DireccionesUsuarios direccionGuardada = direccionesUsuariosRepository.save(direccion);
        Map<String, Object> response = new HashMap<>();
        response.put("id", direccionGuardada.getId());
        response.put("mensaje", "Dirección actualizada con éxito");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Integer id) {
        if (!direccionesUsuariosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        direccionesUsuariosRepository.deleteById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Dirección eliminada con éxito");
        return ResponseEntity.ok(response);
    }
}
