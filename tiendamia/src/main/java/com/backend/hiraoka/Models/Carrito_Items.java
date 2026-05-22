package com.backend.hiraoka.Models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "carrito_items")
@Data
public class Carrito_Items {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "variacion_id")
    private Variaciones_Producto variacion;

    private Integer cantidad;

    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(insertable = false, name = "updateAt")
    private LocalDateTime updateAt;

}
