package com.spring.team1.tiendamia.services.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.usuario.Roles;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.payload.AuthResponse;
import com.spring.team1.tiendamia.models.payload.LoginRequest;
import com.spring.team1.tiendamia.models.payload.RegisterRequest;
import com.spring.team1.tiendamia.repository.usuario.RolesRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.services.jwtServices;

@Service
public class AuthService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtServices jwtService;

    // ─── LOGIN CON EMAIL Y PASSWORD ──────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {

        //Se busca al usuario por correo
        Usuarios usuario = usuariosRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        //Verificar que la cuenta este activa
        if (!usuario.getActivo()) 
            throw new RuntimeException("La cuenta está desactivada");
        

        //Verificar que tenga password
        if (usuario.getPassword() == null) 
            throw new RuntimeException("Esta cuenta fue creada con Google. Inicia sesión con Google.");
        

        //Verificar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) 
            throw new RuntimeException("Correo o contraseña incorrectos");

        //Generar el token JWT
        String token = jwtService.generarToken(usuario.getCorreo());

        return new AuthResponse(
                token,
                usuario.getCorreo(),
                usuario.getNombres(),
                usuario.getRol().getNombre());
    }

    // ─── REGISTRO DE NUEVO USUARIO ───────────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {

        // Verificar que el correo no este registrado
        if (usuariosRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe una cuenta con ese correo");
        }

        // Obtener el rol USER por defecto (debe existir en la BD)
        Roles rolUser = rolesRepository.findByNombre("USER")
                .orElseThrow(
                        () -> new RuntimeException("Rol USER no encontrado."));

        // Crear el nuevo usuario
        Usuarios nuevoUsuario = new Usuarios();
        nuevoUsuario.setNombres(request.getNombres());
        nuevoUsuario.setApellidos(request.getApellidos());
        nuevoUsuario.setCorreo(request.getCorreo());
        nuevoUsuario.setTelefono(request.getTelefono());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        nuevoUsuario.setRol(rolUser);
        nuevoUsuario.setActivo(true);

        usuariosRepository.save(nuevoUsuario);

        // Generar token JWT, el usuario queda logueado automaticamente al registrarse
        String token = jwtService.generarToken(nuevoUsuario.getCorreo());

        return new AuthResponse(
                token,
                nuevoUsuario.getCorreo(),
                nuevoUsuario.getNombres(),
                rolUser.getNombre());
    }

    // ─── LOGIN / REGISTRO CON GOOGLE ─────────────────────────────────────────────
    // El frontend verifica el token con Google y nos manda: googleId, correo,
    // nombres
    public AuthResponse loginConGoogle(String googleId, String correo, String nombres) {

        // Obtener el rol USER por defecto
        Roles rolUser = rolesRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado."));

        // Buscar si ya existe el usuario por correo
        Usuarios usuario = usuariosRepository.findByCorreo(correo).orElse(null);

        if (usuario == null) {
            // Primera vez que entra con Google - se registra automaticamente
            usuario = new Usuarios();
            usuario.setNombres(nombres);
            usuario.setCorreo(correo);
            usuario.setGoogleId(googleId);
            usuario.setRol(rolUser);
            usuario.setActivo(true);
            // Sin password porque usa Google para autenticarse
            usuariosRepository.save(usuario);

        } else {
            // Ya existe - si no tenia googleId lo vinculamos a su cuenta
            if (usuario.getGoogleId() == null) {
                usuario.setGoogleId(googleId);
                usuariosRepository.save(usuario);
            }

            if (!usuario.getActivo()) {
                throw new RuntimeException("La cuenta está desactivada");
            }
        }

        // Generar token JWT
        String token = jwtService.generarToken(usuario.getCorreo());

        return new AuthResponse(
                token,
                usuario.getCorreo(),
                usuario.getNombres(),
                usuario.getRol().getNombre());
    }

    public Usuarios obtenerUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
