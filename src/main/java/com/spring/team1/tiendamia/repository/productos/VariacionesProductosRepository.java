package com.spring.team1.tiendamia.Repository.productos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.Productos.VariacionesProducto;

@Repository
public interface VariacionesProductosRepository extends JpaRepository<VariacionesProducto, Integer> {
    List<VariacionesProducto> findByProductoIdAndProductoActivoTrue(Integer productoId);
}
