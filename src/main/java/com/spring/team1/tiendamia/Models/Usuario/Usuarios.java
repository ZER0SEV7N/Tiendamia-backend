package com.spring.team1.tiendamia.models.Usuario;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Roles rol;

    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String password;

    @Column(name = "google_id")
    private String googleId; // Para login con Google

    private Boolean activo = true;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(insertable = false, name = "updateAt")
    private LocalDateTime updateAt;
}
