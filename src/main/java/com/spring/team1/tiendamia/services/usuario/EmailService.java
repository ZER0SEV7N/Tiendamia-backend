package com.spring.team1.tiendamia.services.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailRecuperacion(String destinatario, String token) throws MessagingException {

        // Link que el frontend usara para mostrar el formulario de nueva contraseña
        String link = "http://localhost:5173/recuperar-password?token=" + token;

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("Recuperación de contraseña - Tiendamia");

        // Email en HTML con diseño simple por ahora
        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 520px; margin: auto; padding: 24px; border: 1px solid #e0e0e0; border-radius: 8px;">
                    <h2 style="color: #1a1a1a;">Recupera tu contraseña</h2>
                    <p style="color: #444;">Recibimos una solicitud para restablecer la contraseña de tu cuenta en <strong>Tiendamia</strong>.</p>
                    <p style="color: #444;">Haz clic en el botón para crear una nueva contraseña. Este enlace expira en <strong>15 minutos</strong>.</p>
                    <a href="%s"
                       style="display: inline-block; margin: 20px 0; padding: 12px 28px;
                              background-color: #1a1a1a; color: #fff; text-decoration: none;
                              border-radius: 6px; font-size: 15px;">
                        Restablecer contraseña
                    </a>
                    <p style="color: #888; font-size: 13px;">Si no solicitaste esto, ignora este correo. Tu contraseña no cambiará.</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin-top: 24px;">
                    <p style="color: #aaa; font-size: 12px;">Tiendamia &copy; 2026</p>
                </div>
                """
                .formatted(link);

        helper.setText(html, true);
        mensaje.saveChanges();
        mailSender.send(mensaje);
    }
}
