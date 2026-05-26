package com.spring.team1.tiendamia.Models.Ordenes;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.spring.team1.tiendamia.Models.Usuario.Direcciones_usuarios;
import com.spring.team1.tiendamia.Models.Usuario.Usuarios;
import com.spring.team1.tiendamia.Models.payload.EstadoOrden;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ordenes")
@Data
public class Ordenes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
