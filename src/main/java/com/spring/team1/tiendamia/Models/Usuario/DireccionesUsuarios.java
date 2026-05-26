package com.spring.team1.tiendamia.Models.Usuario;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "direcciones_usuario")
@Data
public class DireccionesUsuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private String referencia;

    @Column(name = "es_principal")
    private Boolean es_principal = false;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

}
