package com.spring.team1.tiendamia.models.wishlist;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.spring.team1.tiendamia.models.productos.Productos;
import com.spring.team1.tiendamia.models.usuario.Usuarios;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wishlist", uniqueConstraints = {
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
    private Productos producto;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
}
