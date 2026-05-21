package com.backend.hiraoka.Models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.backend.hiraoka.Models.payload.EstadoOrden;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ordenes")
@Data
public class Ordenes {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "direccion_id")
    private Direcciones_usuarios direccion;

    private Double total;
    private String metodoPago;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    private String id_transaccion_pasarela;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;
}
