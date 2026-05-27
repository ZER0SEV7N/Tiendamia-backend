package com.spring.team1.tiendamia.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.models.usuario.MetodosPago;

@Repository
public interface MetodosPagoRepository extends JpaRepository<MetodosPago, Integer> {

}
