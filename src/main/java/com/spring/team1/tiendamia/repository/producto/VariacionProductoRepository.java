package com.spring.team1.tiendamia.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.VariacionesProducto;

@Repository
public interface VariacionProductoRepository extends JpaRepository<VariacionesProducto, Integer> {
    Optional<VariacionesProducto> findById(Integer id);
}
