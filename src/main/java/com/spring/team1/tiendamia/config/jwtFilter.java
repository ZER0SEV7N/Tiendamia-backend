package com.spring.team1.tiendamia.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.team1.tiendamia.Models.Usuario.Usuarios;
import com.spring.team1.tiendamia.Repositories.UsuariosRepository;
import com.spring.team1.tiendamia.services.jwtServices;

import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private jwtServices jwtService;
    @Autowired
    private UsuariosRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws IOException, ServletException {

        // Extrae la cabecera llamada "Authorization" de la petición HTTP
        final String authHeader = request.getHeader("Authorization");

        // Si no existe la cabecera, o no empieza con "Bearer ", entonces no es un token válido
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Corta el texto para quedarse únicamente con el token
        final String token = authHeader.substring(7);

        // Valida si el token es real y vigente. Si falla, deniega el paso
        if (!jwtService.validarToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Si el token es válido, extrae el email del cliente
        final String email = jwtService.extraerCorreo(token);

        // Buscamos al usuario en la base de datos por su correo
        Optional<Usuarios> usuarioOpt = userRepository.findByCorreo(email);

        if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get();
            
            // Extraemos el nombre del rol del usuario desde la base de datos
            String nombreRolBD = usuario.getRol().getNombre(); 

            // Le pegamos el prefijo "ROLE_" que Spring Security exige por capricho
            String rolFormateado = "ROLE_" + nombreRolBD.toUpperCase();

            // Creamos la lista de autoridades con el rol formateado
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rolFormateado));

            // Creamos la credencial pasándole las autoridades reales del usuario
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email, null, authorities);

            // Registra al usuario con sus roles en el Contexto de Seguridad
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // Si el token es válido pero el usuario ya no existe en la base de datos, denegamos el paso
            filterChain.doFilter(request, response);
            return;
        }
        
        // Continúa con el camino hacia el Controller
        filterChain.doFilter(request, response);
    }
}
