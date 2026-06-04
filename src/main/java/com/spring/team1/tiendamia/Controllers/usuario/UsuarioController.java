package com.spring.team1.tiendamia.controllers.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.spring.team1.tiendamia.models.ordenes.DTO.OrdenResponseDto;
import com.spring.team1.tiendamia.models.payload.usuario.UsuarioPerfilDto;
import com.spring.team1.tiendamia.services.usuario.UsuarioService;
import com.spring.team1.tiendamia.util.Response;

//Controlador para endpoints relacionados con el usuario autenticado, su perfil y sus órdenes
@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*") 
public class UsuarioController {

    @Autowired private UsuarioService usuarioService;

    // ─── PERFIL ─────────────────────────────────────────────────────────────
    //Endpoint para obtener el perfil completo del usuario autenticado, incluyendo sus datos personales y sus órdenes
    //GET: /api/usuario/perfil-completo
    @GetMapping("/perfil-completo")
    public ResponseEntity<Response<UsuarioPerfilDto>> getPerfilCompleto(Authentication authentication) {
        UsuarioPerfilDto perfil = usuarioService.obtenerPerfilCompleto(authentication);
        return ResponseEntity.ok(new Response<>(true, "Perfil obtenido correctamente", perfil));
    }

    //Endpoint para actualizar el perfil del usuario autenticado. 
    //Se pueden actualizar los campos: nombres, apellidos, telefono y password
    //PATCH: /api/usuario/perfil-actualizar
    //El request body puede contener cualquiera de los siguientes campos (todos opcionales)
    @PatchMapping("/perfil-actualizar")
    public ResponseEntity<Response<UsuarioPerfilDto>> actualizarPerfil(Authentication authentication, @RequestBody Map<String, Object> payload) {
        Optional<UsuarioPerfilDto> perfilActualizado = usuarioService.actualizarPerfil(authentication, payload);
        return perfilActualizado.map(perfil -> ResponseEntity.ok(new Response<>(true, "Perfil actualizado", perfil)))
                .orElse(ResponseEntity.status(404).body(new Response<>(false, "Perfil no actualizado", null)));
    }

    //─── ORDENES ────────────────────────────────────────────────────────────
    //Endpoint para listar las órdenes del usuario autenticado
    //GET: /api/usuario/ordenes
    //La respuesta incluirá una lista de órdenes con su ID formateado, fecha, estado con colores y total formateado
    @GetMapping("/ordenes")
    public ResponseEntity<Response<List<OrdenResponseDto>>> listarMisOrdenes(Authentication authentication) {
        List<OrdenResponseDto> ordenes = usuarioService.listarMisOrdenes(authentication);
        return ResponseEntity.ok(new Response<>(true, "Órdenes obtenidas correctamente", ordenes));
    }


}