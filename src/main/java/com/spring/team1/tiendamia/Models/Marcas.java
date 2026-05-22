package com.spring.team1.tiendamia.Models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "marcas")
@Data
public class Marcas {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String slug;
    private String imagen_logo;
    private String imagen_banner;
    private String descripcion;
    private Boolean destacada = false;
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;
}
