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
@Table(name = "carrito_items")
@Data
public class Carrito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuarios usuarios;

    @Column(name = "tarifa", nullable = false)
    private Double tarifa = 0.0;

    @Column(name = "envio", nullable = false)
    private Double envio = 0.0;

    @Column(nullable = false)
    private Double total = 0.0;

    //Relacion uno a muchos con carrito detalle
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true) 
    @JsonManagedReference
    private List<CarritoDetalle> detalles = new ArrayList<>();

    //Timestamps
    @CreationTimestamp
    @Column(updatable = false, name = "createAt")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(insertable = false, name = "updateAt")
    private LocalDateTime updateAt;
}
