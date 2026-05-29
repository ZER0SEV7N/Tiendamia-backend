package com.spring.team1.tiendamia.services.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuariosRepository usuariosRepository;

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
}
