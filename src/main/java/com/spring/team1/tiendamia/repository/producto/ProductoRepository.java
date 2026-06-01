package com.spring.team1.tiendamia.repository.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.Productos;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByIdAndEstadoTrue(Long id);

    Optional<Productos> findBySlug(String slug);

    List<Productos> findAllByEstadoTrue();
}
