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
import com.spring.team1.tiendamia.util.response;

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
    public ResponseEntity<response<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new response<>(true, "Inicio de sesión exitoso", response));
    }

    // ─── POST /api/auth/register
    // ──────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<response<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new response<>(true, "Registro exitoso", response));
    }


    // ─── POST /api/auth/google
    // ────────────────────────────────────────────────────────
    @PostMapping("/google")
    public ResponseEntity<response<AuthResponse>> loginConGoogle(@RequestBody Map<String, String> body) {
        
        String googleId = body.get("googleId");
        String correo = body.get("correo");
        String nombres = body.get("nombres");

        if (googleId == null || correo == null || nombres == null) {
            return ResponseEntity.badRequest()
                    .body(new response<>(false, "Faltan campos requeridos: googleId, correo, nombres", null));
        }

        AuthResponse response = authService.loginConGoogle(googleId, correo, nombres);
        return ResponseEntity.ok(new response<>(true, "Inicio de sesión con Google exitoso", response));
    }

    // ─── POST /api/auth/recuperar-password ───────────────────────────────────────────
    // El usuario manda su correo y le llega un email con el link de recuperación
    @PostMapping("/recuperar-password")
    public ResponseEntity<response<Object>> solicitarRecuperacion(@RequestBody SolicitarRecuperacionRequest request) { 
        recuperacionService.solicitarRecuperacion(request.getCorreo());
        return ResponseEntity.ok(new response<>(true, "Te enviamos un correo con las instrucciones", null));
    }

    // ─── POST /api/auth/cambiar-password ─────────────────────────────────────────────
    // El usuario manda el token del email + su nueva contraseña
    @PostMapping("/cambiar-password")
    public ResponseEntity<response<Object>> cambiarPassword(@RequestBody CambiarPasswordRequest request) {
        recuperacionService.cambiarPassword(request.getToken(), request.getNuevaPassword());
        return ResponseEntity.ok(new response<>(true, "Contraseña actualizada correctamente.", null));
    }

    // --- Get: /api/auth/perfil
    //Tras loguearse el cliente puede acceder a esta ruta para ver su información básica (correo, nombres, rol)
    @GetMapping("/perfil")
    public ResponseEntity<response<Usuarios>> obtenerPerfil(Authentication authentication) {
        if (authentication == null || authentication.getName() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new response<>(false, "No autenticado", null));

            String correo = authentication.getName();
            Usuarios usuario = authService.obtenerUsuarioPorCorreo(correo);
            return ResponseEntity.ok(new response<>(true, "Perfil obtenido correctamente", usuario));
    }    
}
