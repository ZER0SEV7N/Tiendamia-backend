package com.spring.team1.tiendamia.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.team1.tiendamia.Models.payload.AuthResponse;
import com.spring.team1.tiendamia.Models.payload.CambiarPasswordRequest;
import com.spring.team1.tiendamia.Models.payload.LoginRequest;
import com.spring.team1.tiendamia.Models.payload.RegisterRequest;
import com.spring.team1.tiendamia.Models.payload.SolicitarRecuperacionRequest;
import com.spring.team1.tiendamia.Services.usuario.AuthService;
import com.spring.team1.tiendamia.Services.usuario.RecuperacionService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RecuperacionService recuperacionService;

    // ─── POST /auth/login
    // ─────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── POST /auth/register
    // ──────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── POST /auth/google
    // ────────────────────────────────────────────────────────
    @PostMapping("/google")
    public ResponseEntity<?> loginConGoogle(@RequestBody Map<String, String> body) {
        try {
            String googleId = body.get("googleId");
            String correo = body.get("correo");
            String nombres = body.get("nombres");

            if (googleId == null || correo == null || nombres == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Faltan campos requeridos: googleId, correo, nombres"));
            }

            AuthResponse response = authService.loginConGoogle(googleId, correo, nombres);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── POST /auth/recuperar-password ───────────────────────────────────────────
    // El usuario manda su correo y le llega un email con el link de recuperación
    @PostMapping("/recuperar-password")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody SolicitarRecuperacionRequest request) {
        try {
            recuperacionService.solicitarRecuperacion(request.getCorreo());
            return ResponseEntity
                    .ok(Map.of("mensaje", "Te enviamos un correo con las instrucciones para recuperar tu contraseña"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ─── POST /auth/cambiar-password ─────────────────────────────────────────────
    // El usuario manda el token del email + su nueva contraseña
    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestBody CambiarPasswordRequest request) {
        try {
            recuperacionService.cambiarPassword(request.getToken(), request.getNuevaPassword());
            return ResponseEntity
                    .ok(Map.of("mensaje", "Contraseña actualizada correctamente. Ya puedes iniciar sesión."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
