package com.spring.team1.tiendamia.repository.ordenes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.Ordenes.Ordenes;

@Repository
public interface OrdenRepository extends JpaRepository<Ordenes, Integer> {

}
