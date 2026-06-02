package com.spring.team1.tiendamia.Controllers.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.Distrito; // <-- Importación correcta de tu entidad
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.repository.usuario.direcciones.DireccionesUsuariosRepository;
import com.spring.team1.tiendamia.repository.usuario.direcciones.DistritoRepository;
import com.spring.team1.tiendamia.services.usuario.UsuarioService;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class DireccionController {

    @Autowired
    private DireccionesUsuariosRepository direccionesUsuariosRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @Autowired
    private UsuarioService usuarioService;

    // 1. GET: Listar direcciones
    @GetMapping("/direcciones")
    public ResponseEntity<?> getDirecciones(Authentication authentication) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);

        List<DireccionesUsuarios> direccionesBD = direccionesUsuariosRepository.findByUsuario_Id(usuario.getId());
        List<Map<String, Object>> listaDirecciones = new ArrayList<>();

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
            listaDirecciones.add(dir);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje", "Direcciones obtenidas con éxito");
        response.put("data", listaDirecciones);

        return ResponseEntity.ok(response);
    }

    // 2. POST: Guardar dirección (MÉTODO BLINDADO)
    @PostMapping("/direcciones")
    public ResponseEntity<?> guardarDireccion(Authentication authentication,
            @RequestBody Map<String, Object> direccionData) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);

        // Intentar resolver id de distrito por `id_distrito` o por nombre `distrito`
        Integer idDistrito = null;
        try {
            if (direccionData.get("id_distrito") != null) {
                idDistrito = Integer.parseInt(direccionData.get("id_distrito").toString());
            } else if (direccionData.get("id_Distrito") != null) {
                idDistrito = Integer.parseInt(direccionData.get("id_Distrito").toString());
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "mensaje", "Error: El id_distrito enviado no es un número válido.",
                    "data", null));
        }

        Distrito distrito = null;
        if (idDistrito != null) {
            Integer finalIdDistrito = idDistrito;
            distrito = distritoRepository.findById(finalIdDistrito)
                    .orElseThrow(() -> new NoSuchElementException("Distrito no encontrado con ID: " + finalIdDistrito));
        } else {
            // permitir recibir el nombre del distrito desde el frontend
            Object dNameObj = direccionData.get("distrito");
            if (dNameObj == null || dNameObj.toString().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "mensaje", "Error: Se requiere 'id_distrito' o el nombre 'distrito' en el JSON",
                        "data", null));
            }
            String nombreDistrito = dNameObj.toString().trim();
            distrito = distritoRepository.findByNombreIgnoreCase(nombreDistrito)
                    .orElseThrow(() -> new NoSuchElementException("Distrito no encontrado: " + nombreDistrito));
        }

        DireccionesUsuarios direccion = new DireccionesUsuarios();
        direccion.setUsuario(usuario);
        direccion.setDistrito(distrito); // <-- Vincula el objeto verificado de forma segura
        direccion.setDireccion((String) direccionData.getOrDefault("direccion", ""));
        direccion.setReferencia((String) direccionData.getOrDefault("referencia", ""));
        direccion.setEs_principal(Boolean.valueOf(String.valueOf(direccionData.getOrDefault("es_principal", false))));

        if (direccion.getReferencia() == null || direccion.getReferencia().isBlank()) {
            String departamento = (String) direccionData.getOrDefault("departamento", "");
            String provincia = (String) direccionData.getOrDefault("provincia", "");
            String distritoNombre = distrito.getNombre();
            direccion.setReferencia(
                    String.join(" / ", departamento, provincia, distritoNombre).replaceAll("^\s*/\s*|\s*/\s*$", ""));
        }

        DireccionesUsuarios direccionGuardada = direccionesUsuariosRepository.save(direccion);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje", "Dirección guardada con éxito");
        response.put("data", Map.of("id", direccionGuardada.getId()));

        return ResponseEntity.ok(response);
    }

    // 3. GET: Obtener por ID
    @GetMapping("/direcciones/{id}")
    public ResponseEntity<?> getDireccion(@PathVariable Integer id) {
        DireccionesUsuarios direccion = direccionesUsuariosRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dirección no encontrada"));

        Map<String, Object> dir = new HashMap<>();
        dir.put("id", direccion.getId());
        dir.put("direccion", direccion.getDireccion());
        dir.put("referencia", direccion.getReferencia());
        dir.put("es_principal", Boolean.TRUE.equals(direccion.getEs_principal()));
        if (direccion.getDistrito() != null) {
            dir.put("distrito", direccion.getDistrito().getNombre());
            if (direccion.getDistrito().getProvincia() != null) {
                dir.put("provincia", direccion.getDistrito().getProvincia().getNombre());
                if (direccion.getDistrito().getProvincia().getDepartamento() != null) {
                    dir.put("departamento", direccion.getDistrito().getProvincia().getDepartamento().getNombre());
                }
            }
        }

        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Dirección encontrada", "data", dir));
    }

    // 4. PUT: Actualizar
    @PutMapping("/direcciones/{id}")
    public ResponseEntity<?> actualizarDireccion(@PathVariable Integer id,
            @RequestBody Map<String, Object> direccionData) {
        DireccionesUsuarios direccion = direccionesUsuariosRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Dirección no encontrada"));

        if (direccionData.containsKey("direccion"))
            direccion.setDireccion((String) direccionData.get("direccion"));
        if (direccionData.containsKey("referencia"))
            direccion.setReferencia((String) direccionData.get("referencia"));
        if (direccionData.containsKey("es_principal")) {
            direccion.setEs_principal(Boolean.valueOf(String.valueOf(direccionData.get("es_principal"))));
        }

        DireccionesUsuarios direccionGuardada = direccionesUsuariosRepository.save(direccion);
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Dirección actualizada con éxito", "data",
                Map.of("id", direccionGuardada.getId())));
    }

    // 5. DELETE: Eliminar
    @DeleteMapping("/direcciones/{id}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Integer id) {
        if (!direccionesUsuariosRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "mensaje", "Dirección no encontrada", "data", null));
        }
        direccionesUsuariosRepository.deleteById(id); // <-- Aquí ocurre el quiebre
        return ResponseEntity.ok(Map.of("mensaje", "Dirección eliminada con éxito"));
    }
}