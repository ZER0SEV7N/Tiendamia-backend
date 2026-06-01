package com.spring.team1.tiendamia.repository.carrito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.carrito.CarritoDetalle;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {

    CarritoDetalle findByCarritoIdAndVariacionId(Integer idcarrito, Integer idvariacion);
}
