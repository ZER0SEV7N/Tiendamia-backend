package com.spring.team1.tiendamia.Controllers.usuario;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.ordenes.Ordenes;
import com.spring.team1.tiendamia.models.ordenes.DTO.OrdenResponseDto;

import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;
import com.spring.team1.tiendamia.repository.ordenes.OrdenRepository;

import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.repository.usuario.direcciones.DireccionesUsuariosRepository;
import com.spring.team1.tiendamia.services.usuario.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class PerfilUsuarioController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private DireccionesUsuariosRepository direccionesRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. GET: Trae el Perfil unificado con sus Direcciones Reales directamente de
    
    @GetMapping("/me")
    public ResponseEntity<?> obtenerPerfilCompleto(Authentication authentication) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);

        List<DireccionesUsuarios> dirEntidades = direccionesRepository.findByUsuario_Id(usuario.getId());

        List<Map<String, Object>> direcciones = new ArrayList<>();
        for (DireccionesUsuarios d : dirEntidades) {
            Map<String, Object> dDto = new HashMap<>();
            dDto.put("id", d.getId());
            dDto.put("direccion", d.getDireccion());
            dDto.put("referencia", d.getReferencia());
            dDto.put("es_principal", Boolean.TRUE.equals(d.getEs_principal()));
            dDto.put("createAt", d.getCreateAt() != null ? d.getCreateAt().toString() : null);
            if (d.getDistrito() != null) {
                dDto.put("distrito", d.getDistrito().getNombre());
                if (d.getDistrito().getProvincia() != null) {
                    dDto.put("provincia", d.getDistrito().getProvincia().getNombre());
                    if (d.getDistrito().getProvincia().getDepartamento() != null) {
                        dDto.put("departamento", d.getDistrito().getProvincia().getDepartamento().getNombre());
                    }
                }
            }
            direcciones.add(dDto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombres", usuario.getNombres());
        response.put("apellidos", usuario.getApellidos());
        response.put("correo", usuario.getCorreo());
        response.put("telefono", usuario.getTelefono());
        response.put("direcciones", direcciones);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> actualizarPerfilMe(Authentication authentication, @RequestBody Map<String, String> payload) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);

        if (payload.containsKey("nombres"))
            usuario.setNombres(payload.get("nombres"));
        if (payload.containsKey("apellidos"))
            usuario.setApellidos(payload.get("apellidos"));
        if (payload.containsKey("telefono"))
            usuario.setTelefono(payload.get("telefono"));

        if (payload.containsKey("password") && payload.get("password") != null && !payload.get("password").isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(payload.get("password")));
        }

        Usuarios usuarioGuardado = usuariosRepository.save(usuario);
        Map<String, Object> result = new HashMap<>();
        result.put("id", usuarioGuardado.getId());
        result.put("nombres", usuarioGuardado.getNombres());
        result.put("apellidos", usuarioGuardado.getApellidos());
        result.put("correo", usuarioGuardado.getCorreo());
        result.put("telefono", usuarioGuardado.getTelefono());

        return ResponseEntity.ok(result);
    }

    // 2. PUT /api/usuario/perfil -> Actualiza nombres, apellidos y telefono
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarPerfil(Authentication authentication, @RequestBody Map<String, String> payload) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);

        if (payload.containsKey("nombres"))
            usuario.setNombres(payload.get("nombres"));
        if (payload.containsKey("apellidos"))
            usuario.setApellidos(payload.get("apellidos"));
        if (payload.containsKey("telefono"))
            usuario.setTelefono(payload.get("telefono"));

        if (payload.containsKey("password") && payload.get("password") != null && !payload.get("password").isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(payload.get("password")));
        }

        Usuarios usuarioGuardado = usuariosRepository.save(usuario);
        return ResponseEntity.ok(usuarioGuardado);
    }

    // 3. GET /api/usuario/ordenes 
    @GetMapping("/ordenes")
    public ResponseEntity<List<OrdenResponseDto>> listarMisOrdenes(Authentication authentication) {
        Usuarios usuario = usuarioService.obtenerUsuarioActual(authentication);
        List<Ordenes> ordenesEntidad = ordenRepository.findByUsuarioIdOrderByIdDesc(usuario.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM, yyyy");

        List<OrdenResponseDto> response = ordenesEntidad.stream().map(o -> {
            OrdenResponseDto dto = new OrdenResponseDto();
            dto.setId("TM-" + String.format("%06d", o.getId()) + "-PE");
            dto.setFecha(o.getCreateAt() != null ? o.getCreateAt().format(formatter) : "Reciente");

            // Lógica de traducción de estados del SQL a colores de Tailwind para los Badges
            String estadoTexto = o.getEstado() != null ? o.getEstado().name().toLowerCase() : "pendiente";
            dto.setEstado(o.getEstado() != null ? o.getEstado().name() : "PENDIENTE");
            if (estadoTexto.equals("entregado")) {
                dto.setEstadoColor("text-green-600 bg-green-50 border-green-200");
            } else if (estadoTexto.equals("cancelado")) {
                dto.setEstadoColor("text-red-600 bg-red-50 border-red-200");
            } else {
                dto.setEstadoColor("text-amber-600 bg-amber-50 border-amber-200");
            }

            dto.setTotal("S/ " + String.format("%.2f", o.getTotal()));

            // Mapear los productos asociados al detalle de la orden
            List<OrdenResponseDto.ProductoDto> prodDtos = new ArrayList<>();
            if (o.getDetalles() != null) {
                o.getDetalles().forEach(d -> {
                    OrdenResponseDto.ProductoDto pDto = new OrdenResponseDto.ProductoDto();
                    if (d.getVariacion() != null && d.getVariacion().getProducto() != null) {
                        pDto.setNombre(d.getVariacion().getProducto().getNombre());
                        pDto.setTiendaOrigen("Tienda Oficial");
                        pDto.setImagen(d.getVariacion().getProducto().getImagen_url() != null ? "📦" : "🛍️");
                    } else {
                        pDto.setNombre("Producto Variante Ref #" + d.getId());
                        pDto.setTiendaOrigen("Amazon EE.UU.");
                        pDto.setImagen("💻");
                    }
                    pDto.setCantidad(d.getCantidad());
                    prodDtos.add(pDto);
                });
            }
            dto.setProductos(prodDtos);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
