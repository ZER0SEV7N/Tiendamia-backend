package com.spring.team1.tiendamia.repository.carrito;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.team1.tiendamia.Models.Carrito.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    List<Carrito> findByUsuarioId(Integer idUsuario);
}
