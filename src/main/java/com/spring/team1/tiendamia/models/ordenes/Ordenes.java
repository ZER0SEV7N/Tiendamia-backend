package com.spring.team1.tiendamia.models.ordenes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.spring.team1.tiendamia.models.usuario.DireccionesUsuarios;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.payload.EstadoOrden;

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
    private DireccionesUsuarios direccion;

    private Double total;
    private String metodoPago;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    private String id_transaccion_pasarela;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenDetalle> detalles = new ArrayList<>();
}
