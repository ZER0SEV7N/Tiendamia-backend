package com.spring.team1.tiendamia.models.RecuperacionPassword;

import java.time.LocalDateTime;

import com.spring.team1.tiendamia.models.Usuario.Usuarios;

import jakarta.persistence.*;
import lombok.Data;

// Esta tabla guarda los tokens temporales que se envian por email
// Cuando el usuario hace clic en el link, se valida este token
@Entity
@Table(name = "tokens_recuperacion")
@Data
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Column(nullable = false)
    private LocalDateTime expiracion; // El token dura 15 minutos

    @Column(nullable = false)
    private Boolean usado = false; // Una vez usado, no se puede reutilizar
}
