package com.spring.team1.tiendamia.Repository.productos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.Variaciones_Producto;

@Repository
public interface VariacionesProductosRepository extends JpaRepository<Variaciones_Producto, Integer> {
    List<Variaciones_Producto> findByProductoIdAndProductoActivoTrue(Integer productoId);
}
