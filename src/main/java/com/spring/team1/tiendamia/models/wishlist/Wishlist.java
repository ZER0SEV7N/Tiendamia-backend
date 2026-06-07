package com.spring.team1.tiendamia.models.wishlist;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.team1.tiendamia.models.productos.Producto;
import com.spring.team1.tiendamia.models.usuario.Usuarios;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wishlist", 
    uniqueConstraints = { 
        @UniqueConstraint(columnNames = { "id_Usuario", "id_Producto" })
    })
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_Usuario", nullable = false)
    private Usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_Producto", nullable = false)
    private Producto producto;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
