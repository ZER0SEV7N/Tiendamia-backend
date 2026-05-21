package com.backend.hiraoka.Models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "marcas_tarjeta")
@Data
public class Marcas_Tarjeta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String logo_url;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;
}
