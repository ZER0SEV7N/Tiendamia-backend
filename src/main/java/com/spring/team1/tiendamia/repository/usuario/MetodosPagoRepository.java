package com.spring.team1.tiendamia.Repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.Usuario.MetodosPago;

@Repository
public interface MetodosPagoRepository extends JpaRepository<MetodosPago, Integer> {

}
