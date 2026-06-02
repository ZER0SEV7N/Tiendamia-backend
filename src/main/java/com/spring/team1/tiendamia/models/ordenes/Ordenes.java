package com.spring.team1.tiendamia.models.ordenes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.spring.team1.tiendamia.models.payload.orden.EstadoOrden;
import com.spring.team1.tiendamia.models.usuario.Usuarios;
import com.spring.team1.tiendamia.models.usuario.ubicacion.DireccionesUsuarios;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orden")
@Data
public class Ordenes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_Direccion")
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
