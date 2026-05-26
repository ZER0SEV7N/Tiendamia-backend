package com.spring.team1.tiendamia.repository.productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.Productos;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByIdAndActivoTrue(Integer id);

    List<Productos> findAllByActivoTrue();
}
