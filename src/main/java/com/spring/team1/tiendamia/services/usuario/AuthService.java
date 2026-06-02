package com.spring.team1.tiendamia.services.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.models.usuario.Roles;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.spring.team1.tiendamia.models.payload.auth.AuthResponse;
import com.spring.team1.tiendamia.models.payload.auth.LoginRequest;
import com.spring.team1.tiendamia.models.payload.auth.RegisterRequest;
import com.spring.team1.tiendamia.repository.usuario.RolesRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;
import com.spring.team1.tiendamia.services.jwtServices;

import java.util.Collections;

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
        if (!usuario.getEstado()) 
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
        nuevoUsuario.setEstado(true);

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
    public AuthResponse loginConGoogle(String idTokenString) {
        try {
            //Verificar el token con Google
            String clientId = "614554963628-o7k5bf9vnqlmpv11jcbqksm7vvbk4t3e.apps.googleusercontent.com";

            //El GoogleIdTokenVerifier se encarga de verificar la firma del token y que no haya sido modificado
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();
            
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if(idToken == null) 
                throw new RuntimeException("Token de Google no válido");
            
            //Extraer la información del token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String correo = payload.getEmail();
            String nombres = (String) payload.get("name");
            String googleId = payload.getSubject();

            Roles rolUser = rolesRepository.findByNombre("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado."));

            Usuarios usuario = usuariosRepository.findByCorreo(correo).orElse(null);

            //Si el usuario no existía, lo creamos automáticamente con la info de Google
            if (usuario == null) {
                usuario = new Usuarios();
                usuario.setNombres(nombres);
                usuario.setCorreo(correo);
                usuario.setGoogleId(googleId);
                usuario.setRol(rolUser);
                usuario.setEstado(true);
                usuariosRepository.save(usuario);
            } else {
                //Regla de negocio: Si el usuario ya existía pero es ADMIN, no permitimos login con Google
                if ("ADMIN".equalsIgnoreCase(usuario.getRol().getNombre())) 
                    throw new RuntimeException("Acceso denegado: Las cuentas administrativas deben usar credenciales corporativas.");
                
                if (!usuario.getEstado()) 
                    throw new RuntimeException("La cuenta está desactivada");
                
                //Vincular cuenta de Google si ya existía el correo pero no el GoogleId
                if (usuario.getGoogleId() == null) {
                    usuario.setGoogleId(googleId);
                    usuariosRepository.save(usuario);
                }
            }

            String token = jwtService.generarToken(usuario.getCorreo());

            return new AuthResponse(
                    token,
                    usuario.getCorreo(),
                    usuario.getNombres(),
                    usuario.getRol().getNombre());

        } catch (Exception e) {
            //Captura errores de red con Google o el RuntimeException que lanzamos arriba
            throw new RuntimeException(e.getMessage());
        }
    }

    public Usuarios obtenerUsuarioPorCorreo(String correo) {
        return usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
