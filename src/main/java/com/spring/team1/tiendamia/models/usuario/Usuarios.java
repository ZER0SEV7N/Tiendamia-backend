package com.spring.team1.tiendamia.models.usuario;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuarios {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Rol")
    private Roles rol;

    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String password;

    @Column(name = "id_Google")
    private String googleId; // Para login con Google

    private Boolean estado = true;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(insertable = false, name = "updateAt")
    private LocalDateTime updateAt;
}
