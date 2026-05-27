package com.spring.team1.tiendamia.models.usuario;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @JoinColumn(name = "id_Usuario")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_Distrito")
    private Distrito distrito;

    private String direccion;
    private String referencia;

    @Column(name = "es_principal")
    private Boolean es_principal = false;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "updateAt")
    private LocalDateTime updateAt;

}
