package com.spring.team1.tiendamia.repository.wishlist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.team1.tiendamia.models.wishlist.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    // Obtiene todos los items de la wishlist de un usuario
    List<Wishlist> findByUsuarioId(Integer idUsuario);

    // Buscar un item especifico por usuario y producto
    Optional<Wishlist> findByUsuarioIdAndProductoId(Integer idUsuario, Integer idProducto);

    // Verificar si un producto ya esta en la lista de favoritos(wihslit) del
    // usuario
    boolean existsByUsuarioIdAndProductoId(Integer idUsuario, Integer idProducto);
}
