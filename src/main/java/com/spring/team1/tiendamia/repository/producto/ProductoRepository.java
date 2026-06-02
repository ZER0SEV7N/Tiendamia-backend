package com.spring.team1.tiendamia.repository.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByIdAndEstadoTrue(Long id);

    Optional<Producto> findBySlug(String slug);

    List<Producto> findAllByEstadoTrue();
}
