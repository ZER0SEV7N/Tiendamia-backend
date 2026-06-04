package com.spring.team1.tiendamia.services.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.mapper.UsuarioMapper;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;
import com.spring.team1.tiendamia.repository.ordenes.OrdenRepository;
import com.spring.team1.tiendamia.repository.usuario.DireccionUsuarioRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.models.ordenes.Ordenes;
import com.spring.team1.tiendamia.models.ordenes.DTO.OrdenResponseDto;
import com.spring.team1.tiendamia.models.payload.usuario.UsuarioPerfilDto;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired private UsuariosRepository usuariosRepository;
    @Autowired private DireccionUsuarioRepository direccionRepository;
    @Autowired private OrdenRepository ordenRepository;
    @Autowired private UsuarioMapper usuarioMapper;
    @Autowired private PasswordEncoder passwordEncoder;

        /**
         * Obtiene el usuario autenticado actualmente desde el objeto Authentication
         * El authentication.getName() devuelve el correo (principal) del usuario
         * 
     * @param authentication El objeto de autenticación de Spring Security
     * @return El usuario autenticado
     * @throws RuntimeException Si no hay autenticación o el usuario no existe
     */

    public Usuarios obtenerUsuarioActual(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String correo = authentication.getName();
        return usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + correo));
    }

    /**
     * Obtiene un usuario por su ID
     * 
     * @param id El ID del usuario
     * @return El usuario con ese ID
     * @throws RuntimeException Si el usuario no existe
     */
    public Usuarios obtenerUsuarioPorId(Integer id) {
        return usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
    }

    /**
     * Obtiene un usuario por su correo
     * 
     * @param correo El correo del usuario
     * @return El usuario con ese correo
     * @throws RuntimeException Si el usuario no existe
     */
    public Usuarios obtenerUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + correo));
    }

    //Metodo para obtener el perfil completo del usuario.
    @Transactional(readOnly = true)
    public UsuarioPerfilDto obtenerPerfilCompleto(Authentication auth) {
        Usuarios usuario = obtenerUsuarioActual(auth);
        List<DireccionesUsuarios> direcciones = direccionRepository.findByUsuarioId(usuario.getId());
        return usuarioMapper.aUsuarioPerfilDTO(usuario, direcciones);
    }

    //Metodo para actualizar el perfil del usuario (nombres, apellidos, telefono)
    @Transactional
    public Optional<UsuarioPerfilDto> actualizarPerfil(Authentication auth, Map<String, Object> payload) {
        Usuarios usuario = obtenerUsuarioActual(auth);

        //Actualizar solo los campos que vengan en el payload (nombres, apellidos, telefono, password)
        if(payload.containsKey("nombres")) usuario.setNombres((String) payload.get("nombres"));
        if(payload.containsKey("apellidos")) usuario.setApellidos((String) payload.get("apellidos"));
        if(payload.containsKey("telefono")) usuario.setTelefono((String) payload.get("telefono"));

        if(payload.containsKey("password") && payload.get("password") != null) {
            String raw = (String) payload.get("password");
            
            if(!raw.isBlank()) usuario.setPassword(passwordEncoder.encode(raw));
        }
        Usuarios usuarioGuardado = usuariosRepository.save(usuario);
        List<DireccionesUsuarios> direcciones = direccionRepository.findByUsuarioId(usuarioGuardado.getId());
        return Optional.of(usuarioMapper.aUsuarioPerfilDTO(usuarioGuardado, direcciones));
    }

    //Metodo para obtener las ordenes del usuario
    @Transactional(readOnly = true)
    public List<OrdenResponseDto> listarMisOrdenes(Authentication auth) {
        Usuarios usuario = obtenerUsuarioActual(auth);
        List<Ordenes> ordenes = ordenRepository.findByUsuarioIdOrderByIdDesc(usuario.getId());
        return ordenes.stream()
                .map(usuarioMapper::aOrdenResponseDto)
                .collect(Collectors.toList());
    }
}
