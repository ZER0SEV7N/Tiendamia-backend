package com.spring.team1.tiendamia.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.productos.Productos;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {
    
}
