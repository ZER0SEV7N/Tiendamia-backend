package com.spring.team1.tiendamia.services.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.team1.tiendamia.Models.RecuperacionPassword.TokenRecuperacion;
import com.spring.team1.tiendamia.Models.Usuario.Usuarios;
import com.spring.team1.tiendamia.repository.usuario.TokenRecuperacionRepository;
import com.spring.team1.tiendamia.repository.usuario.UsuariosRepository;

@Service
public class RecuperacionService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private TokenRecuperacionRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // ─── Primeramente el usuario pide recuperar su contraseña
    // ─────────────────────────
    // Recibe el correo, genera un token temporal y envia el email
    public void solicitarRecuperacion(String correo) {
        Usuarios usuario = usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("No existe una cuenta con ese correo"));

        // Verificar que no sea cuenta de Google pura
        if (usuario.getPassword() == null) {
            throw new RuntimeException(
                    "Esta cuenta usa Google para iniciar sesión. No tiene contraseña que recuperar.");
        }

        // Generar token unico aleatorio
        String token = UUID.randomUUID().toString();

        // Guardar el token en la BD con 15 minutos de vigencia
        TokenRecuperacion tokenRecuperacion = new TokenRecuperacion();
        tokenRecuperacion.setToken(token);
        tokenRecuperacion.setUsuario(usuario);
        tokenRecuperacion.setExpiracion(LocalDateTime.now().plusMinutes(15));
        tokenRecuperacion.setUsado(false);
        tokenRepository.save(tokenRecuperacion);

        // Enviar el email con el link de recuperación
        try {
            emailService.enviarEmailRecuperacion(correo, token);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo enviar el correo. Intenta de nuevo más tarde.");
        }
    }

    // ─── Segundo El usuario manda el token + nueva contraseña ────────────────────
    // Valida que el token sea valido, no este vencido ni usado, y actualiza la
    // contraseña
    public void cambiarPassword(String token, String nuevaPassword) {

        // Buscar el token en la BD
        TokenRecuperacion tokenRecuperacion = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o no existe"));

        // Verificar que no haya sido usado ya
        if (tokenRecuperacion.getUsado()) {
            throw new RuntimeException("Este enlace ya fue utilizado. Solicita uno nuevo.");
        }

        // Verificar que no haya expirado
        if (LocalDateTime.now().isAfter(tokenRecuperacion.getExpiracion())) {
            throw new RuntimeException("El enlace ha expirado. Solicita uno nuevo.");
        }

        // Actualizar la contraseña del usuario con BCrypt
        Usuarios usuario = tokenRecuperacion.getUsuario();
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuariosRepository.save(usuario);

        // Marcar el token como usado para que no se pueda reutilizar
        tokenRecuperacion.setUsado(true);
        tokenRepository.save(tokenRecuperacion);
    }
}
