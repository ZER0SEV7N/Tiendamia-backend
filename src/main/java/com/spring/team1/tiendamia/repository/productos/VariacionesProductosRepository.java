package com.spring.team1.tiendamia.repository.productos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.VariacionesProducto;

@Repository
public interface VariacionesProductosRepository extends JpaRepository<VariacionesProducto, Integer> {
    List<VariacionesProducto> findByProductoIdAndProductoActivoTrue(Integer productoId);
}
