package com.spring.team1.tiendamia.controllers.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.models.payload.auth.AuthResponse;
import com.spring.team1.tiendamia.models.payload.auth.CambiarPasswordRequest;
import com.spring.team1.tiendamia.models.payload.auth.LoginRequest;
import com.spring.team1.tiendamia.models.payload.auth.RegisterRequest;
import com.spring.team1.tiendamia.models.payload.auth.SolicitarRecuperacionRequest;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.services.usuario.AuthService;
import com.spring.team1.tiendamia.services.usuario.RecuperacionService;
import com.spring.team1.tiendamia.util.Response;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RecuperacionService recuperacionService;

    // ─── POST /api/auth/login
    // ─────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<Response<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new Response<>(true, "Inicio de sesión exitoso", response));
    }

    // ─── POST /api/auth/register
    // ──────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(true, "Registro exitoso", response));
    }


    // ─── POST /api/auth/google
    // ────────────────────────────────────────────────────────
    @PostMapping("/google")
    public ResponseEntity<Response<AuthResponse>> loginConGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");

        if (idToken == null || idToken.trim().isEmpty()) 
            throw new RuntimeException("El token de Google es obligatorio");
        
        AuthResponse response = authService.loginConGoogle(idToken); 
        return ResponseEntity.ok(new Response<>(true, "Inicio de sesión con Google exitoso", response));
    }

    // ─── POST /api/auth/recuperar-password ───────────────────────────────────────────
    // El usuario manda su correo y le llega un email con el link de recuperación
    @PostMapping("/recuperar-password")
    public ResponseEntity<Response<Object>> solicitarRecuperacion(@RequestBody SolicitarRecuperacionRequest request) { 
        recuperacionService.solicitarRecuperacion(request.getCorreo());
        return ResponseEntity.ok(new Response<>(true, "Te enviamos un correo con las instrucciones", null));
    }

    // ─── POST /api/auth/cambiar-password ─────────────────────────────────────────────
    // El usuario manda el token del email + su nueva contraseña
    @PostMapping("/cambiar-password")
    public ResponseEntity<Response<Object>> cambiarPassword(@RequestBody CambiarPasswordRequest request) {
        recuperacionService.cambiarPassword(request.getToken(), request.getNuevaPassword());
        return ResponseEntity.ok(new Response<>(true, "Contraseña actualizada correctamente.", null));
    }

    // --- Get: /api/auth/perfil
    //Tras loguearse el cliente puede acceder a esta ruta para ver su información básica (correo, nombres, rol)
    @GetMapping("/perfil")
    public ResponseEntity<Response<Usuarios>> obtenerPerfil(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, "No autenticado", null));

            String correo = authentication.getName();
            Usuarios usuario = authService.obtenerUsuarioPorCorreo(correo);
            return ResponseEntity.ok(new Response<>(true, "Perfil obtenido correctamente", usuario));
    }    
}
