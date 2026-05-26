package com.spring.team1.tiendamia.Repository.carrito;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.Carrito.Carrito;

@Repository
public interface carritoRepository extends JpaRepository<Carrito, Integer> {

    List<Carrito> findByUsuarioId(Integer idUsuario);
}
