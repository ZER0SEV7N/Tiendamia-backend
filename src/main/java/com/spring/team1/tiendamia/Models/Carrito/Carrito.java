package com.spring.team1.tiendamia.Models.Carrito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.team1.tiendamia.Models.Usuario.Usuarios;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carrito")
@Data
public class Carrito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuario;

    @Column(name = "tarifa", nullable = false)
    private Double tarifa = 0.0;

    @Column(name = "envio", nullable = false)
    private Double envio = 0.0;

    @Column(nullable = false)
    private Double total = 0.0;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CarritoDetalle> detalles = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(insertable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}
