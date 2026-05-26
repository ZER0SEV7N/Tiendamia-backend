package com.spring.team1.tiendamia.Repository.productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.spring.team1.tiendamia.Models.Productos.Productos;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByIdAndActivoTrue(Integer id);

    List<Productos> findAllByActivoTrue();
}
