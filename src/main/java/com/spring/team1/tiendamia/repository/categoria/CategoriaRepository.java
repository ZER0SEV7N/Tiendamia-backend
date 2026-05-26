package com.spring.team1.tiendamia.repository.categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.team1.tiendamia.Models.productos.Categorias;

@Repository
public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    @Query("SELECT c FROM Categorias c WHERE c.id NOT IN " +
           "(SELECT DISTINCT h.categoriaPadre.id FROM Categorias h WHERE h.categoriaPadre IS NULL)")
    List<Categorias> findByCategoriasPadre(); 

    @Query("SELECT c FROM Categorias c WHERE c.id NOT IN " +
           "(SELECT DISTINCT h.categoriaPadre.id FROM Categorias h WHERE h.categoriaPadre IS NOT NULL)")
    List<Categorias> findCategoriasHija();
}
